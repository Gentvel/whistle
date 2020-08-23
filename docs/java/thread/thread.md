---
title: 【多线程】 篇一 多线程基础
date: 2020-08-05
sidebar: auto
categories:
 - java
prev: false
next: false
---


## 一、新建线程
一个java程序从main()方法开始执行，然后按照既定的代码逻辑运行，看似像没有其他线程参与，但实际java程序天生就是一个多线程程序。
1. 分发处理发送给JVM信号的线程
2. 调用对象[finalize()](../jvm/garbage.html#二、判断可触及性)方法的线程
3. 清除[Reference](../jvm/garbage.html#三、引用)的线程
4. main线程，用户线程的入口

### 1.1 创建线程的三种方法
- 通过继承Thread类，重写run方法
- 通过实现Runable接口
- 通过实现Callable接口
::: details 
```java
public class CreateThread {

    static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("Extends Thread Start...");
        }
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            System.out.println("Implements Runnable start...");
        }
    }

    static class MyCallable implements Callable<String>{

        @Override
        public String call() throws Exception {
            System.out.println("Implements Callable start..");
            return "hello thread";
        }
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        MyRunnable myRunnable = new MyRunnable();
        new Thread(myRunnable).start();
        FutureTask<String> stringFutureTask = new FutureTask<>(new MyCallable());
        new Thread(stringFutureTask).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(stringFutureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        //java 8 lambada
        new Thread(()-> System.out.println("JDK 8 -lambada Thread start")).start();
        new Thread(new FutureTask<String>(()->{
            System.out.println("lambda Callable Thread start");
            return "Hello World";
        })).start();
    }

}
```
:::




- 由于java不能多继承可以实现多个接口，因此，在创建线程的时候尽量多考虑采用实现接口的形式
- 实现callable接口，可以利用`FutureTask(Callable callable)`将callable进行包装然后FeatureTask提交给ExecutorService返回的是异步执行的结果
- 另外由于FeatureTask也实现了Runable接口也可以利用上面第二种方式（实现Runable接口）来新建线程
:::tip
可以通过Executors将Runable转换成Callable，具体方法是：`Callable callable(Runnable task, T result)`， `Callable callable(Runnable task)`。
:::

start()方法是具体实现创建线程的方法，而run()方法只是执行方法内容
::: details
```java
public synchronized void start() {
  /**
    * This method is not invoked for the main method thread or "system"
    * group threads created/set up by the VM. Any new functionality added
    * to this method in the future may have to also be added to the VM.
    *
    * A zero status value corresponds to state "NEW".
    */
  if (threadStatus != 0)
      throw new IllegalThreadStateException();

  /* Notify the group that this thread is about to be started
    * so that it can be added to the group's list of threads
    * and the group's unstarted count can be decremented. */
  group.add(this);

  boolean started = false;
  try {
      start0();
      started = true;
  } finally {
      try {
          if (!started) {
              group.threadStartFailed(this);
          }
      } catch (Throwable ignore) {
          /* do nothing. If start0 threw a Throwable then
            it will be passed up the call stack */
      }
  }
}
private native void start0();
```
:::
由于Thread类也是实现了Runnable接口，所以其实启动线程的方法只有一个，那就是start()方法。

## 二、线程的操作方法

操作线程有很多方法，这些方法可以从一种状态过渡到另一种状态

### 2.1 线程的休眠
使当前线程sleep，线程不会释放锁，处于等待状态。
```java
static void threadSleep() throws InterruptedException {
    System.out.println("Thread sleep 2 seconds");
    Thread.sleep(2000);
}
```

### 2.2 线程的加入

如果当前程序为多线程，加入当前线程A需要插入线程B来完成工作，并要求取回线程B完成后的结果，然后再执行线程A。那么此时可以使用join()方法。
当某个线程使用join()加入到另外一个线程时，另一个线程会等待该线程执行完成后再继续。
```java
public static void threadJoin() throws InterruptedException {
    AtomicInteger speed = new AtomicInteger(10);
    Thread breakerThread = new Thread(()->{
        System.out.println("Breaker Start! ");
        do {
            System.out.println("Breaker Running! Current Speed: " + (speed.getAndDecrement()));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (speed.get() > 0);

        System.out.println("Breaker Stop ! ");
    });
    Thread engineThread  = new Thread(()->{
        System.out.println("Engine Start!");
        do {
            System.out.println("Engine Running! Current Speed: " + (speed.getAndIncrement()));

            try {
                breakerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (speed.get() <= 20);
        System.out.println("Engine Stop!");
    });

    engineThread.start();
    breakerThread.start();
}
```
engine线程和breaker线程同时启动，当engine线程启动时，开启breaker进程，只有当breaker进程执行完才会执行engine线程。

```java
public final synchronized void join(final long millis)
    throws InterruptedException {
        if (millis > 0) {
            if (isAlive()) {
                final long startTime = System.nanoTime();
                long delay = millis;
                do {
                    wait(delay);
                } while (isAlive() && (delay = millis -
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)) > 0);
            }
        } else if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            throw new IllegalArgumentException("timeout value is negative");
        }
    }

        /**
     * Tests if this thread is alive. A thread is alive if it has
     * been started and has not yet died.
     *
     * @return  {@code true} if this thread is alive;
     *          {@code false} otherwise.
     */
    public final native boolean isAlive();

```

<!-- 从源码可以看出，底层是调用wait()方法，锁的是当前当前对象。 -->




## 二、线程状态的基本操作
![线程状态改变](./img/ThreadStatusChange.png)

除了新建一个线程外，线程在生命周期内还有需要基本操作，而这些操作会成为线程间一种通信方式，比如使用中断（interrupted）方式通知实现线程间的交互等等

![线程状态](./img/ThreadStatus.png)