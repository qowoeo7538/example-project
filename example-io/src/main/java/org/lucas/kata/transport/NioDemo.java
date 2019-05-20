package org.lucas.kata.transport;

import org.lucas.kata.transport.impl.NioClient;
import org.lucas.kata.transport.impl.NioService;
import org.lucas.core.task.ExampleThreadExecutor;

/**
 * @create: 2017-12-15
 * @description: NIO测试
 */
public class NioDemo {

    /**
     * 服务端host地址
     */
    private final static String hostname = "127.0.0.1";

    /**
     * 服务端端口
     */
    private final static int port = 9092;

    /**
     * 传输文件
     */
    private final static String srcFile = "C:\\Users\\john\\Desktop\\coverage-error.log";

    public static void main(String[] args) {
        NioService nioService = new NioService(hostname, port);
        NioClient nioClient = new NioClient(hostname, port);
        ExampleThreadExecutor.execute(() -> nioService.mySetup());

        ExampleThreadExecutor.execute(() -> nioClient.sendFile(srcFile));

    }
}