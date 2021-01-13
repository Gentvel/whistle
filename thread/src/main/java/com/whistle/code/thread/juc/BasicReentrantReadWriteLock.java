package com.whistle.code.thread.juc;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicReentrantReadWriteLock {

    public static void main(String[] args) {
        Data data = new Data();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                data.put(Math.random());
            }).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                data.get();
            }).start();
        }
        data.toString();
    }


    private static class Data {
        private Object data;
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

        public void get() {
            reentrantReadWriteLock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "读取数据");
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + "读取数据完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
        }

        public void put(Object o) {
            reentrantReadWriteLock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + "开始写数据");
            this.data = o;
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + "写入完成" + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }

        @Override
        public String toString() {
            return reentrantReadWriteLock.toString();
        }
    }
}
