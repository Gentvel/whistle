package com.whistle.code.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class ExecutorsTest {
    public static void main(String[] args) throws InterruptedException {
        /*
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
            }
        });
        executorService.execute(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
            }
        });

        TimeUnit.SECONDS.sleep(3);
        executorService.execute(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
            }
        });*/

       /* ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        executorService1.execute(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService1.execute(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService1.execute(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
        /*ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            System.out.println(Thread.currentThread().getName());
        });*/

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.schedule(()->{
            System.out.println(Thread.currentThread().getName());
        },1,TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName());
        },0,2,TimeUnit.SECONDS);

    }
}
