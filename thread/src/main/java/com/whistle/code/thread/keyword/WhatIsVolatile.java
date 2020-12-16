package com.whistle.code.thread.keyword;

/**
 * volatile 是java虚拟机提供的轻量级的同步机制（不保证原子性，保证可见性，保证有序性）
 *
 * 1、线程读取i
 * 2、temp = i + 1 3、i = temp 当 i=5 的时候A,B两个线程同时读入了 i 的值，
 * 3、A线程执行了 temp = i + 1的操作，要注意，此时的 i 的值还没有变化，
 * 4、B线程也执行了 temp = i + 1的操作，注意，此时A，B两个线程保存的 i 的值都是5，temp 的值都是6，
 * 5、A线程执行了 i = temp （6）的操作，此时i的值会立即刷新到主存并通知其他线程保存的 i 值失效，
 * 6、此时B线程需要重新读取 i 的值那么此时B线程保存的 i 就是6，
 * 7、同时B线程保存的 temp 还仍然是6， 然后B线程执行 i=temp （6），所以导致了计算结果比预期少了1
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class WhatIsVolatile {
    public static int number = 0;
    public static void main(String[] args) {
        //内存可见性问题
        new Thread(() -> {
            //工作内存获取number
            System.out.println(Thread.currentThread().getName() + " Current Number: " + number);
            //工作内存操作number
            number++;
            System.out.println(Thread.currentThread().getName() + " Current Number: " + number);
        }, "child-thread").start();

        while (number == 0) {
            //循环将会一直持续，因为child-thread线程的操作number对主线程不可见
        }
        System.out.println(Thread.currentThread().getName() + " Current Number: " + number);
    }
}


