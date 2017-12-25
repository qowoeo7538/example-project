package org.shaw.nio.server.demo;

import org.shaw.nio.server.IMessageProcessor;
import org.shaw.nio.server.Message;

import java.io.UnsupportedEncodingException;

/**
 * @create: 2017-12-21
 * @description:
 */
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 38\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body>Hello World!</body></html>";
        byte[] httpResponseBytes = httpResponse.getBytes("UTF-8");

        IMessageProcessor messageProcessor = (request, writeProxy) -> {
            System.out.println("Message Received from socket: " + request.socketId);

            Message response = writeProxy.getMessage();
            response.socketId = request.socketId;
            response.writeToMessage(httpResponseBytes);
        };
    }
}
