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
        Thread breakerThread = new Thread(() -> {
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
        Thread engineThread = new Thread(() -> {
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


    public void testDaemon() {
        Thread thread = new Thread(()->{
            for (int i = 0; i < 10000000; i++) {
                System.out.println(" I am Daemon");
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(" I m not daemon running");
            }
        }).start();
    }


    public void testYield() {
        Thread thread = new Thread(() -> {
            int i = 30;
            while (i > 0) {
                System.out.println("I am yield !");
                i--;
                Thread.yield();
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);

        Thread run = new Thread(()->{
            int i = 30;
            while (i>0){
                System.out.println(" I am run ");
                i--;
            }
        });
        run.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        run.start();
    }



}
