package org.shaw.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by joy on 17-2-16.
 * <p>
 * 可重入锁作用演示（ ReentrantLock 和synchronized 都是 可重入锁）
 * 可重入锁，也叫做递归锁，指的是同一线程 外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响.
 */
public class ReentrantLockDemo implements Runnable {
    Lock lock = new ReentrantLock();

    public void get() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId());
            set();
        } finally {
            lock.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        get();
    }

}
