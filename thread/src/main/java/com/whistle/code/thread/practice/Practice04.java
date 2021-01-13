package com.whistle.code.thread.practice;

/**
 * 编写一个程序，启动三个线程，三个线程的ID分别是A，B，C；，每个线程将自己的ID值在屏幕上打印5遍，打印顺序是ABCABC...
 * @author Gentvel
 * @version 1.0.0
 */
public class Practice04 {
    private static Object lock = new Object();

    private static int num = 1;

    private static void print(int printNum){
        synchronized (lock){
            while (num%3!=printNum){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            num++;
            lock.notifyAll();
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                print(1);
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                print(2);
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                print(0);
            }
        },"C").start();

    }



}
