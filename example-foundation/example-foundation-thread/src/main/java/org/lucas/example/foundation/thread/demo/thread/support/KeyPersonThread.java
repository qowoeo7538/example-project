package org.lucas.example.foundation.thread.demo.thread.support;

/**
 * @create: 2018-01-23
 * @description:
 */
public class KeyPersonThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + "左突右杀隋军[" + i + "]次");
        }
    }
}
