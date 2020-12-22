package com.whistle.code.thread.basic;

import java.util.concurrent.FutureTask;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread is = new Thread(() -> {
            System.out.println("is");
        });
        is.start();
        is.join();
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("os");
            return "s";
        });
        new Thread(futureTask).start();
    }
}
