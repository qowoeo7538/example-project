package org.shaw.compiler.support;

import org.shaw.util.ClassUtils;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JdkCompiler extends AbstractCompiler {

    /** 默认类加载器类名 */
    private final static String APP_CLASSLOADER = "sun.misc.Launcher$AppClassLoader";

    /** 类加载器 */
    private final ClassLoaderImpl classLoader;

    private final JavaFileManagerImpl javaFileManager;

    /** java编译器 */
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    /** 诊断监听器 */
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

    /** 编译相关参数 */
    private volatile List<String> options;

    public JdkCompiler() {
        options = new ArrayList<>();
        options.add("-source");
        options.add("1.6");
        options.add("-target");
        options.add("1.6");
        /**
         * 标准的java文件管理器(java编译器需要)
         * 作用:
         *   1) 用于构建编译器的读写功能 (可能会减少对文件系统的扫描和jar文件读写的开销)
         *   2) 在多个编译任务之间共享
         */
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        final ClassLoader loader = ClassUtils.getDefaultClassLoader();
        /**
         * 1) 是 URLClassLoader 加载器的实例(通过指向目标文件加载类)
         * 2) 不是默认的类加载器
         */
        if (loader instanceof URLClassLoader
                && (!APP_CLASSLOADER.equals(loader.getClass().getName()))) {
            try {
                URLClassLoader urlClassLoader = (URLClassLoader) loader;
                List<File> files = new ArrayList<>();
                // 获取加载资源的路径
                for (URL url : urlClassLoader.getURLs()) {
                    files.add(new File(url.getFile()));
                }
                // 将加载的文件与文件管理器关联
                manager.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        // 根据安全策略, 获取类加载器(默认的安全策略拥有全部权限)
        classLoader = AccessController.doPrivileged((PrivilegedAction<ClassLoaderImpl>) () ->
                new ClassLoaderImpl(loader)
        );
        javaFileManager = new JavaFileManagerImpl(manager, classLoader);
    }

    @Override
    protected Class<?> doCompile(String name, String source) throws Throwable {

        return null;
    }

    /**
     * 类加载器实现
     */
    private final class ClassLoaderImpl extends ClassLoader {

        /**
         * key: 类名
         * value: JavaFileObject java文件
         */
        private final Map<String, JavaFileObject> classes = new HashMap<>();

        ClassLoaderImpl(final ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        /**
         * 根据类全名, 将 {@code JavaFileObject} 定义的对象加载到内存中
         *
         * @param name 类全名
         * @return Class
         * @throws ClassNotFoundException
         */
        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            // 获取java源代码和class文件
            JavaFileObject file = classes.get(name);
            if (file != null) {
                // 获取源代码的字节
                byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
                // 将字节转换成类的实例
                return defineClass(name, bytes, 0, bytes.length);
            }
            try {
                return ClassUtils.forName(name, getClass().getClassLoader());
            } catch (ClassNotFoundException nf) {
                return super.findClass(name);
            }
        }

        /**
         * 防止并发加载类
         *
         * @param name    类全名
         * @param resolve 是否加载该类引用的其它类
         * @return Class
         * @throws ClassNotFoundException
         */
        @Override
        protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        /**
         * 通过 class 文件的 InputStream 对象
         *
         * @param name class 文件路径
         * @return InputStream
         */
        @Override
        public InputStream getResourceAsStream(final String name) {
            if (name.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                // 获取类全名
                String qualifiedClassName = name.substring(0,
                        name.length() - ClassUtils.CLASS_FILE_SUFFIX.length()).replace('/', '.');
                // 通过类全名获取 JavaFileObjectImpl
                JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
                if (file != null) {
                    return new ByteArrayInputStream(file.getByteCode());
                }
            }
            return super.getResourceAsStream(name);
        }
    }

    /**
     * JavaFileObject 重写
     */
    private static final class JavaFileObjectImpl extends SimpleJavaFileObject {

        /** 源代码 */
        private final CharSequence source;

        /** 源代码字节 */
        private ByteArrayOutputStream bytecode;

        JavaFileObjectImpl(final String name, final Kind kind) {
            super(ClassHelper.toURI(name), kind);
            source = null;
        }

        public JavaFileObjectImpl(final String baseName, final CharSequence source) {
            super(ClassHelper.toURI(baseName + ClassUtils.JAVA_FILE_SUFFIX), Kind.SOURCE);
            this.source = source;
        }

        public JavaFileObjectImpl(URI uri, Kind kind) {
            super(uri, kind);
            source = null;
        }

        /**
         * 获取源代码
         *
         * @return CharSequence
         * @throws UnsupportedOperationException
         */
        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
            if (source == null) {
                throw new UnsupportedOperationException("source == null");
            }
            return source;
        }

        /**
         * 获取源代码字节流
         *
         * @return InputStream
         */
        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(getByteCode());
        }

        /**
         * 重构 {@link #bytecode} 的输入流
         *
         * @return OutputStream
         */
        @Override
        public OutputStream openOutputStream() {
            return bytecode = new ByteArrayOutputStream();
        }

        /**
         * 获取源代码字节
         *
         * @return byte[]
         */
        public byte[] getByteCode() {
            return bytecode.toByteArray();
        }
    }

    /**
     * 文件管理器
     */
    private static final class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

        /** 类加载器 */
        private final ClassLoaderImpl classLoader;

        /**
         * KEY:   URI
         * VALUE: JavaFileObject
         */
        private final Map<URI, JavaFileObject> fileObjects = new HashMap<>();

        public JavaFileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
            super(fileManager);
            this.classLoader = classLoader;
        }

        /**
         * 根据定位获取 FileObject
         *
         * @param location     位置
         * @param packageName  包名
         * @param relativeName 相对名称
         * @return FileObject
         * @throws IOException
         */
        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
            FileObject o = fileObjects.get(uri(location, packageName, relativeName));
            if (o != null) {
                return o;
            }
            return super.getFileForInput(location, packageName, relativeName);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, JavaFileObject.Kind kind, FileObject outputFile)
                throws IOException {
            JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
            classLoader.add(qualifiedName, file);

        }

        /**
         * 拼接 URI 路径
         */
        private URI uri(Location location, String packageName, String relativeName) {
            return ClassHelper.toURI(location.getName() + '/' + packageName + '/' + relativeName);
        }
    }
}
