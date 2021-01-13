package com.whistle.code.thread.test;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class ReadWriteLock {
    public static void main(String[] args) {
        final Data data = new Data();

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        data.get();
                    }
                }
            }, "读锁线程-" + i).start();

        }

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        data.put(new Random().nextInt(10000));
                    }
                }
            }, "写锁线程-" + i).start();

        }

    }
}

class Data {
    private Object data = 0;// 共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据。
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public void get() {
        rwl.readLock().lock();// 上读锁，其他线程只能读不能写
        System.out.println(Thread.currentThread().getName() + " 开始读取数据");

        try {
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println(Thread.currentThread().getName() + " 读取数据完成 " + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwl.readLock().unlock(); // 释放读锁
        }
    }

    public void put(Object data) {
        rwl.writeLock().lock();// 上写锁，不允许其他线程读也不允许写
        System.out.println(Thread.currentThread().getName() + " 开始写数据");

        try {
            Thread.sleep((long) (Math.random() * 1000));
            this.data = data;
            System.out.println(Thread.currentThread().getName() + " 写数据完成 " + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwl.writeLock().unlock();// 释放写锁
        }
    }
}