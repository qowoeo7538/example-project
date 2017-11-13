package org.shaw.thread.future;

import org.shaw.thread.future.impl.RealService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @create: 2017-11-13
 * @description:
 */
public class FutureDemo {
    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        FutureTask<String> futureTask = new FutureTask<>(new RealService("hello,world"));
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
        newFixedThreadPool.submit(futureTask);
        // 表示正在处理其他逻辑,或者业务
        try {
            Thread.sleep(1000);
            System.out.println("最后结果-->" + futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        Long end = System.currentTimeMillis();
        Long useTime = end - start;
        System.out.println("程序运行了-->" + useTime + "毫秒");
    }
}
