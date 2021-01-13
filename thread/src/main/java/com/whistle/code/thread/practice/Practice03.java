package com.whistle.code.thread.practice;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程，一个线程打印1~ 52，另一个线程打印A~Z，打印顺序是12A34B...5152Z
 * <p>
 * 交替运行的线程一般考虑synchronized的wait和notify，
 * ReentrantLock的Condition中的await和signal
 * LockSupport中的park和unpark
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Practice03 {
    private static Object lock = new Object();

    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static Condition condition = reentrantLock.newCondition();


    public static void main(String[] args) {
        numThread1("num").start();
        charThread1("char").start();



    }

    private static Thread charThread1(String name) {
        AtomicInteger num = new AtomicInteger(65);
        return new Thread(() -> {
            reentrantLock.lock();
            try {
                while (num.get() <= 90) {

                    System.out.println(Thread.currentThread().getName() + " : " + Character.toString(num.getAndIncrement()));
                    condition.signal();
                    if (num.get() > 90) {
                        condition.signalAll();
                    } else
                        condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }, name);
    }

    private static Thread numThread1(String name) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return new Thread(() -> {
            reentrantLock.lock();
            try {
                while (atomicInteger.get() <= 52) {
                    System.out.println(Thread.currentThread().getName() + " : " + (atomicInteger.getAndIncrement()));
                    System.out.println(Thread.currentThread().getName() + " : " + (atomicInteger.getAndIncrement()));
                    condition.signal();
                    if (atomicInteger.get() > 52) {
                        condition.signalAll();
                    } else {
                        condition.await();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }, name);
    }


    private static Thread charThread(String name) {
        AtomicInteger atomicInteger = new AtomicInteger(65);
        return new Thread(() -> {
            synchronized (lock) {
                while (atomicInteger.get() <= 90) {
                    System.out.println(Thread.currentThread().getName() + " : " + Character.toString(atomicInteger.getAndIncrement()));
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notifyAll();
            }
        }, name);
    }


    private static Thread numThread(String name) {
        AtomicInteger num = new AtomicInteger(1);
        return new Thread(() -> {
            synchronized (lock) {
                while (num.get() <= 52) {
                    System.out.println(Thread.currentThread().getName() + " : " + (num.getAndIncrement()));
                    System.out.println(Thread.currentThread().getName() + " : " + (num.getAndIncrement()));
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notifyAll();
            }
        }, name);
    }


}
