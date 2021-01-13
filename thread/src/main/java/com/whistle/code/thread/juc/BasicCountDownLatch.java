package com.whistle.code.thread.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的作用是让当前线程等待其它线程完成一组操作后才能执行，否则就一直等待。
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicCountDownLatch {
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(5);
    public static void main(String[] args) throws InterruptedException {
        Thread boss = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " running");
            try {
                COUNT_DOWN_LATCH.await();
                System.out.println(Thread.currentThread().getName() + "meeting start");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "boss");
        boss.start();

        for (int i = 0; i <COUNT_DOWN_LATCH.getCount(); i++) {
            new Thread(()->{
                COUNT_DOWN_LATCH.countDown();
                System.out.println(Thread.currentThread().getName() + "coming still wait " + COUNT_DOWN_LATCH.getCount() + " members");
            },"employee"+i).start();
        }
    }
}
