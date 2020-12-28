package com.whistle.code.thread.practice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程轮流打印数字，一直到100
 * <p>
 * 保证可见性，原子性
 *
 * 如果不用AtomicInteger怎么实现？
 *
 *
 * 如果不用synchronized关键字怎么实现？
 *
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Practice02 {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    private static Object lock = new Object();

    private static int num;


    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static Condition condition = reentrantLock.newCondition();



    public static void main(String[] args) throws InterruptedException {
        //incrementThread("a").start();
        //incrementThread("b").start();
//        System.out.println("---------------------------------");
//        incrementThread2("a");
//        incrementThread2("b");
        incrementThread3("a");
        incrementThread3("b");
    }


    private static void incrementThread3(String name){

        new Thread(()->{
            try {
                reentrantLock.lock();
                while (num<=100){
                    System.out.println(Thread.currentThread().getName()+" : "+num++);
                    condition.signal();
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        },name).start();
    }


    private static void incrementThread2(String name){
        new Thread(()->{
            synchronized (lock){
                while (num<=100){
                    System.out.println(Thread.currentThread().getName()+" : "+num++);
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notifyAll();
            }
        },name).start();
    }

    private static Thread incrementThread(String name) {
        return new Thread(() -> {
            synchronized (atomicInteger) {
                while (atomicInteger.get() <= 100) {
                    System.out.println(Thread.currentThread().getName() + " : " + atomicInteger.getAndIncrement());
                    atomicInteger.notify();
                    try {
                        atomicInteger.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                atomicInteger.notifyAll();
            }
        }, name);

    }
}
