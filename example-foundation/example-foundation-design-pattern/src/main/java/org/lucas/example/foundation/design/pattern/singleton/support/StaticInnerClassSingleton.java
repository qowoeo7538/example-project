package org.lucas.example.foundation.design.pattern.singleton.support;

/**
 * 加载类时,其内部类并不会被加载,只有当内部类的
 * 某个静态成员(静态域、构造器、静态方法)被调用
 * 时才会加载
 */
public class StaticInnerClassSingleton {

    private static boolean initialized = false;

    private StaticInnerClassSingleton() {
        synchronized (StaticInnerClassSingleton.class) {
            // 初始化的时候进行一次判断,防止反射序列化
            if (!initialized) {
                initialized = true;
            } else {
                throw new RuntimeException("单例被破坏");
            }
        }
    }

    private static class SingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 一旦你实现了序列化接口，那么它们不再保
     * 持单例了因为readObject()方法一直返回
     * 一个新的对象就像java的构造方法一样可以
     * 通过使用readResolve()方法来避免此事发生
     */
    private Object readResolve() {
        return getInstance();
    }

}
