package com.whistle.code.thread.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("线程测试")
public class BasicThread {
    @Test
    @DisplayName("线程创建测试")
    public void createThread() throws ExecutionException, InterruptedException {

        new Thread(() -> {
            System.out.println("hello I am new Runnable Thread");
            System.out.println("Good bye I am out");
        }).start();
        FutureTask<String> stringFutureTask = new FutureTask<>(() -> {
            System.out.println("Hello I am new Callable Thread");
            System.out.println("Good bye I am out too");
            return "Good bye";
        });
        new Thread(stringFutureTask).start();
        System.out.println(stringFutureTask.get());
    }

    @Test
    @DisplayName("测试Interrupt")
    public void testInterrupt() {
        Thread thread = new Thread(() -> {
            System.out.println("Iam Thread" + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupt");
                e.printStackTrace();
            }
        }, "Thread 1");
        thread.start();
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("I am " + Thread.currentThread().getName());
            thread.interrupt();
            System.out.println(thread.isInterrupted());
            return "Hello world";
        });
        if (thread.isAlive()) {
            new Thread(futureTask, "FutureTask").start();
        }
        while (Thread.activeCount() > 2) {
            //
        }
        System.out.println("stop");
    }
    /*
    join()方法的底层是利用wait()方法实现的。
    可以看出，join方法是一个同步方法，当主线程调用t1.join()方法时，主线程先获得了t1对象的锁，随后进入方法，调用了t1对象的wait()方法，
    使主线程进入了t1对象的等待池，此时，A线程则还在执行，并且随后的t2.start()还没被执行，
    因此，B线程也还没开始。等到A线程执行完毕之后，主线程继续执行，走到了t2.start()，B线程才会开始执行。
     */
    @Test
    @DisplayName("测试join")
    public void testJoin() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("I am " + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-1");
        thread.start();
        //调用下面方法的线程将被挂起
        thread.join();
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("I am " + Thread.currentThread().getName());
            return "Hello";
        });
        new Thread(futureTask).start();
    }

    /**
     * 生产者消费者问题，使用wait/notify
     */
    private int storeSize = 30;
    private List<String> list = new LinkedList<>();

    @Test
    @DisplayName("测试wait and notify")
    public void testWaitAndNotify(){
        for (int i = 0; i < 5; i++) {
            new Thread(this::produce,"producer"+i).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(this::consumer,"consumer"+i).start();
        }

        while (Thread.activeCount()>2){

        }
    }

    /**
     * 生产货物
     */
    private void produce(){

        synchronized (list){
            while (true){
                if(list.size()==storeSize){
                    System.out.println("store is full...wait ");
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    list.notifyAll();
                }
                while (list.size()<storeSize){
                    String s = "good"+ (int) (Math.random() * 10);
                    list.add(s);
                    System.out.println(Thread.currentThread().getName()+" product a goods: "+s +" current goods count: "+ list.size());
                }
            }
        }
    }

    private void consumer(){
        synchronized (list){
            while (true){
                if(list.size()==0){
                    System.out.println(Thread.currentThread().getName()+" none goods ...wait");
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                while (list.size()>0){
                    String remove = list.remove(0);
                    System.out.println(Thread.currentThread().getName()+" get goods "+remove+" current goods :"+ list.size());
                    list.notifyAll();
                }
            }
        }
    }



    /*
    关键字synchronized可以修饰方法或者以同步块的形式来进行使用，它主要确保多个线程在同一个时刻，只能有一个线程处于方法或者同步块中，
    它保证了线程对变量访问的可见性和排他性。
    注意：

    每个对象都可以做为锁，但一个对象做为锁时，应该被多个线程共享，这样才显得有意义，在并发环境下，一个没有共享的对象作为锁是没有意义的。
    无论synchronized关键字加在方法上还是对象上，它取得的锁都是对象，而不是把一段代码或函数当作锁。
    如果一个对象有多个synchronized方法，只要一个线程访问了其中的一个synchronized方法，其它线程不能同时访问这个对象中任何一个synchronized方法 。
    但不同的对象实例的 synchronized方法是不相干扰的。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法;
     */

    private int testInt = 100;
    @Test
    @DisplayName("测试synchronized")
    public void testSynchronize(){
        for (int i = 0; i<10;i++) {
            new Thread(()->{
                synchronized (BasicThread.class){
                    testInt--;
                }
            }).start();
        }
        while (Thread.activeCount()>2){
            //ignoring
        }
        System.out.println(testInt);
    }

    private Object lock1 = new Object();
    private Object lock2 = new Object();


    @Test
    @DisplayName("测试死锁")
    public void testDeadLock(){
        new Thread(()->{
            synchronized (lock1){
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2){
                    System.out.println(Thread.currentThread().getName()+"is right");
                }
            }
        },"Thread -1").start();

        FutureTask<String> vFutureTask = new FutureTask<>(() -> {
            synchronized (lock2) {
                TimeUnit.SECONDS.sleep(3);
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + "is ok");
                }
            }
            return "ok";
        });
        new Thread(vFutureTask,"FutureTask").start();
        while (Thread.activeCount()>2){

        }
    }


}
