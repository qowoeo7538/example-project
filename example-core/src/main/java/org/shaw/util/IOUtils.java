package org.shaw.util;

import org.shaw.util.support.ReadProcess;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * @create: 2017-12-12
 * @description: Nio工具类
 */
public class IOUtils {

    /**
     * 通道处理工具类
     *
     * @param channel     通道
     * @param readProcess 处理对象
     * @throws IOException
     */
    public static void channelRead(ScatteringByteChannel channel, ReadProcess readProcess) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (channel.read(buffer) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                readProcess.onProcess(buffer);
            }
            buffer.clear();
        }
    }

    /**
     * @param buffer      缓冲区
     * @param readProcess 处理对象
     */
    public static void bufferProcess(ByteBuffer buffer, ReadProcess readProcess) {

    }

    /**
     * char转换byte[]
     *
     * @param chars
     * @param encode
     * @return
     */
    public static byte[] getChartoBytes(char chars, String encode) {
        Charset cs = Charset.forName(encode);
        //分配缓冲区
        CharBuffer cb = CharBuffer.allocate(1);
        //放入缓冲区
        cb.put(chars);
        //为一系列通道写入或相对获取 操作做好准备
        cb.flip();
        //将此 charset 中的字符串编码成字节的便捷方法。
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * @param fileName
     * @return 以字节为单位返回文件大小;
     */
    public static long getFileSize(String fileName) throws Exception {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        // return file.length();
        return fileChannel.size();
    }

    /**
     * 计算文件的哈希值
     *
     * @param file
     * @param hashType "MD5"，"SHA1","SHA-1"，"SHA-256"，"SHA-384"，"SHA-512"
     * @return
     */
    public static String getFileMD5(File file, String hashType) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance(hashType);
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存临时文件
     *
     * @param sourceFile
     * @param tempFile
     * @throws Exception
     */
    public static void saveTempFile(File sourceFile, File tempFile) throws Exception {
        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        byte[] buf = new byte[10 * 1024];
        int readLeng = 0;
        while ((readLeng = inputStream.read(buf)) != -1) {
            fileOutputStream.write(buf, 0, readLeng);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        inputStream.close();
    }

    /**
     * 文件字节转字符串
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String byteToString(File file) throws Exception {
        StringBuilder returnDatas = new StringBuilder();
        FileInputStream fileInputStream = new FileInputStream(file);
        BASE64Encoder base64 = new BASE64Encoder();
        byte[] buf = new byte[10 * 1024];
        int readLenth = 0;
        while ((readLenth = fileInputStream.read(buf)) != -1) {
            byte[] copyByte = new byte[readLenth - 1];
            for (int i = 0; i < copyByte.length; i++) {
                copyByte[i] = buf[i];
            }
            returnDatas.append(base64.encode(copyByte));
        }
        fileInputStream.close();
        return returnDatas.toString();
    }

    /**
     * 字符串转文件
     *
     * @param str
     * @param file
     * @throws Exception
     */
    public static void StringTobyte(String str, File file) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BASE64Decoder base64 = new BASE64Decoder();
        fileOutputStream.write(base64.decodeBuffer(str));
        fileOutputStream.close();
    }
}