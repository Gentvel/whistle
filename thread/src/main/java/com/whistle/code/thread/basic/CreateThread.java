package com.whistle.code.thread.basic;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class CreateThread {

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("I am Runnable interface");
            System.out.println("run...");
        }).start();

        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("I am Callable Interface");
            return "Hello Thread";
        });
        new Thread(futureTask).start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
