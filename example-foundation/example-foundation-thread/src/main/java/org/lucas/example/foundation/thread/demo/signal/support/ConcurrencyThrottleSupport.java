package org.lucas.example.foundation.thread.demo.signal.support;

/**
 * 关于Spring源码中的并发量控制测试
 */
public class ConcurrencyThrottleSupport {

    /**
     * 并发量设置
     */
    private int concurrencyLimit = 5;

    /**
     * 锁对象
     */
    private transient final Object monitor = new Object();

    /**
     * 实际已经运行的并发数
     */
    private int concurrencyCount = 0;

    /**
     * 任务运行前,通过调用该方法,控制并发量
     */
    public void beforeAccess() {
        if (this.concurrencyLimit > 0) {
            synchronized (this.monitor) {
                while (this.concurrencyCount >= this.concurrencyLimit) {
                    try {
                        System.out.println(Thread.currentThread().getName() + "队列已满,等待其他任务唤醒");
                        // wait操作释放锁,否则其它线程进入不了 afterAccessNotifyAll 方法
                        this.monitor.wait();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                this.concurrencyCount++;
            }
        }
    }

    /**
     * 任务运行完毕,通过调用该方法释放wait()的线程
     */
    protected void afterAccessNotifyAll() {
        synchronized (this.monitor) {
            this.concurrencyCount--;
            if (this.concurrencyLimit >= 0 && "Thread-0".equals(Thread.currentThread().getName())) {
                System.out.println(Thread.currentThread().getName() + "唤醒睡眠线程");
                System.out.println(Thread.currentThread().getName() + "调用完毕后运行线程个数:" + concurrencyCount);
                this.monitor.notifyAll();
            }
        }
    }

    /**
     * 任务运行完毕,通过调用该方法释放wait()的线程
     */
    protected void afterAccess() {
        if (this.concurrencyLimit >= 0) {
            synchronized (this.monitor) {
                this.concurrencyCount--;
                System.out.println(Thread.currentThread().getName() + "任务已完成,唤醒其它等待线程");
                this.monitor.notify();
            }
        }
    }
}
