package com.whistle.code.thread.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 编写10个线程，第一个线程从1加到10，第二个线程从11加20…第十个线程从91加到100，最后再把10个线程结果相加。
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Practice05 {
    private static int number = 0;

    private static Object lock = new Object();

    private static int increment(int startNumber) throws InterruptedException {
        synchronized (lock) {
            while (startNumber > number) {
                lock.wait();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + ++number);
            }

            lock.notifyAll();
        }
        return number;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            FutureTask<Integer> integerFutureTask = new FutureTask<>(() -> increment(finalI * 10 ));
            new Thread(integerFutureTask).start();
            Integer integer = integerFutureTask.get();
            sum+=integer;
            System.out.println(Thread.currentThread().getName()+"  get return :"+integer+"  sum :"+sum);
        }
        System.out.println(sum);
    }
}
