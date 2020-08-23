package com.whistle.code.currency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * code - com.whistle.code.currency - ThreadSleep
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.currency.ThreadSleep
 */
public class OperateThread {

    static void threadSleep() throws InterruptedException {
        System.out.println("Thread sleep 2 seconds");
        Thread.sleep(2000);
    }

    public static void threadJoin() throws InterruptedException {
        AtomicInteger speed = new AtomicInteger(10);
        Thread breakerThread = new Thread(()->{
            System.out.println("Breaker Start! ");
            do {
                System.out.println("Breaker Running! Current Speed: " + (speed.getAndDecrement()));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (speed.get() > 0);

            System.out.println("Breaker Stop ! ");
        });
        Thread engineThread  = new Thread(()->{
            System.out.println("Engine Start!");
            do {
                System.out.println("Engine Running! Current Speed: " + (speed.getAndIncrement()));

                try {
                    breakerThread.join(200);
//                    Thread.sleep(100);
//                    breakerThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (speed.get() <= 20);
            System.out.println("Engine Stop!");
        });

        engineThread.start();
        breakerThread.start();
    }

}
