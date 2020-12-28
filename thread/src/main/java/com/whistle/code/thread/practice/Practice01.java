package com.whistle.code.thread.practice;

import java.util.concurrent.TimeUnit;

/**
 * 求线程a执行完才开始线程b, 线程b执行完才开始线程
 * @author Gentvel
 * @version 1.0.0
 */
public class Practice01 {

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            for (int i = 30; i > 0; i--) {
                System.out.println(Thread.currentThread().getName()+" 门票还有"+ i +"张");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "a");
        a.start();
        Thread b = new Thread(() -> {
            try {
                a.join();
                for (int i = 30; i > 0; i--) {
                    System.out.println(Thread.currentThread().getName()+" 门票还有"+ i +"张");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "b");
        b.start();
    }
}
