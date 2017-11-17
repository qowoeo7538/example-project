package org.shaw.thread.concurrent.exchanger.impl;

import org.shaw.util.thread.impl.SecurityTask;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2017-11-17
 * @description:
 */
public class ExchangerImpl {

    private static volatile boolean _isDone = false;

    public class ExchangerProducer extends SecurityTask {
        private Exchanger<Integer> exchanger;
        private int data = 1;

        public ExchangerProducer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        protected void runTask() {
            while (!Thread.interrupted() && !_isDone) {
                for (int i = 1; i <= 3; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        data = i;
                        System.out.println("producer change before: " + data);
                        data = exchanger.exchange(data);
                        System.out.println("producer change after: " + data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                _isDone = true;
            }
        }
    }

    public class ExchangerConsumer extends SecurityTask {
        private Exchanger<Integer> exchanger;
        private int data = 0;

        public ExchangerConsumer(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }


        @Override
        protected void runTask() {
            while (!Thread.interrupted() && !_isDone) {
                data = 0;
                System.out.println("consumer change before : " + data);
                try {
                    TimeUnit.SECONDS.sleep(1);
                    data = exchanger.exchange(data);
                    System.out.println("consumer change after : " + data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
