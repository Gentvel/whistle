package com.whistle.code.thread.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier要做的事情是让一组线程到达一个屏障时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程一起执行。
 *
 * CountDownLatch用于一个线程等待若干个其他线程执行完任务之后才执行，强调一个线程等待，这个线程会阻塞。
 * 而CyclicBarrier用于一组线程互相等待至某个状态，然后这一组线程再同时执行，强调的是多个线程互等，这多个线程阻塞，等大家都完成，再携手共进。
 *
 * CountDownLatch是不能复用的，而CyclicBarrier是可以复用的。使用reset()方法将屏障重置为初始状态之后就可以复用。
 *
 * CyclicBarrier提供了更多的方法，能够通过getNumberWaiting()获取阻塞线程的数量，通过isBroken()方法可以知道阻塞的线程是否被中断。
 *
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicCyclicBarrier {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(100,()->{
        System.out.println("starting");
    });

    public static void main(String[] args) {
        System.out.println("inter place...");
        for (int i = 0; i < cyclicBarrier.getParties(); i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"coming ,current waiting "+cyclicBarrier.getNumberWaiting());
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" running");
            },"astro"+i).start();
        }
    }
}
