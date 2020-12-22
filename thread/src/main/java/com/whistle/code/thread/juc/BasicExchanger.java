package com.whistle.code.thread.juc;

import java.util.concurrent.Exchanger;

/**
 * Exchanger提供了一个交换的同步点，在这个同步点两个线程能够交换数据。
 *
 * 具体交换数据是通过exchange()方法来实现的，如果一个线程先执行exchange方法，那么它会同步等待另一个线程也执行exchange方法，这个时候两个线程就都达到了同步点，两个线程就可以交换数据。
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicExchanger {
    private static Exchanger<String> exchanger = new Exchanger<>();
    private static String money = "$100";
    private static String goods = "phone";

    public static void main(String[] args) {
        System.out.println("starting change");
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" i am ready "+ goods);
            try {
                String exchange = exchanger.exchange(goods);
                System.out.println(Thread.currentThread().getName()+ "i am get money : "+exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"sale").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"i am ready "+ money);
            try {
                String exchange = exchanger.exchange(money);
                System.out.println(Thread.currentThread().getName()+"i get good: "+exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"buy").start();
    }
}
