package com.whistle.code.thread.keyword;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Visibility {
    private static int count = 0;

    public static void main(String[] args) {
        new Thread(()->{
            count++;
            System.out.println(count);
        }).start();
        System.out.println(Thread.currentThread().getName()+"  c"+count);
    }

}
