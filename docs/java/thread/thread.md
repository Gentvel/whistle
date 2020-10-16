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

### 2.1 线程的优先级
每个线程执行时都有一个优先级的属性，优先级高的线程可以获得较多的执行机会，而优先级较低的线程则获得较少的执行机会。

操作系统采用时分的形式调用运行的线程，操作系统会分出一个个时间片，线程会分配到若干时间片，当线程的时间片用完了就会发生线程调度，并等待着下次分配。线程分配到的时间片多少也就决定了线程使用处理器资源的多少，而线程优先级就是决定线程需要多或者少分配一些处理器资源的线程属性。

```java

/**
* The minimum priority that a thread can have.
*/
public static final int MIN_PRIORITY = 1;

/**
* The default priority that is assigned to a thread.
*/
public static final int NORM_PRIORITY = 5;

/**
* The maximum priority that a thread can have.
*/
public static final int MAX_PRIORITY = 10;

```
可以看出线程最大的优先级为10，而最小优先级为1，在使用中最好使用Thread中已经定义的常量。设置线程优先级的方法为`setPriority()`

### 2.2 线程的守护

Daemon 线程是一种支持型线程，在后台守护一些系统服务，比如 JVM 的垃圾回收、内存管理等线程都是守护线程。

与之对应的就是用户线程，用户线程就是系统的工作线程，它会完成整个系统的业务操作。

用户线程结束后就意味着整个系统的任务全部结束了，因此系统就没有对象需要守护的了，守护线程自然而然就会退出。所以当一个 Java 应用只有守护线程的时候，虚拟机就会自然退出。设置线程为守护线程的方法是`setDaemon()`


```java


public void testDaemon() {
    Thread thread = new Thread(()->{
        for (int i = 0; i < 10000000; i++) {
            System.out.println(" I am Daemon");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    thread.setDaemon(true);
    thread.start();
    new Thread(() -> {
        for (int i = 0; i < 5; i++) {
            System.out.println(" I m not daemon running");
        }
    }).start();
}
```
输出结果如下：
```shell
I m not daemon running
 I am Daemon
 I m not daemon running
 I m not daemon running
 I m not daemon running
 I m not daemon running
Running time: 3 ms.
 I am Daemon
```
当测试线程执行完后就只剩下守护线程，所以系统就退出了。
:::warning 注意
如果在not daemon线程加上`Thread.sleep(millis)`有可能在该线程没执行完就结束了，因为等待的线程不在就绪状态，操作系统认为当前线程队列已经不存在需要执行的非守护线程，于是终止了进程。
:::

### 2.3 线程的休眠
使当前线程sleep，线程不会释放锁，处于阻塞状态。
```java
static void threadSleep() throws InterruptedException {
    System.out.println("Thread sleep 2 seconds");
    Thread.sleep(2000);
}
```
### 2.4 线程的礼让

yield 方法是 Thread 的静态方法，yield 方法让当前正在执行的线程进入到就绪状态，让出 CPU 资源给其他的线程。
```java
Thread thread = new Thread(() -> {
    int i = 30;
    while (i > 0) {
        System.out.println("I am yield !");
        i--;
        Thread.yield();
    }
});
thread.setPriority(Thread.MAX_PRIORITY);

Thread run = new Thread(()->{
    int i = 30;
    while (i>0){
        System.out.println(" I am run ");
        i--;
    }
});
run.setPriority(Thread.MIN_PRIORITY);
thread.start();
run.start();
```
:::tip 注意
yield 方法只是让当前线程暂停一下，重新进入就绪线程池中，让系统的线程调度器重新调度器重新调度一次，完全可能出现这样的情况：当某个线程调用 yield()方法之后，线程调度器又将其调度出来重新进入到运行状态执行。
:::
### 2.5 线程的中断
中断代表线程状态，每个线程都关联了一个中断状态，用boolean值表示，初始值为false。中断一个线程，其实就是设置了这个线程的中断状态boolean值为true。
:::warning 注意
中断只是一种状态，处于中断状态的线程不一定要停止运行。
:::

Thread类线程中断的方法：
```java
// 设置一个线程的中断状态为true
public void interrupt() {}

// 检测线程中断状态，处于中断状态返回true
public boolean isInterrupted() {}

// 静态方法，检测调用这个方法的线程是否已经中断，处于中断状态返回true
// 注意：这个方法返回中断状态的同时，会将此线程的中断状态重置为false
public static boolean interrupted() {}

```
自动感知中断
以下方法会自动感知中断：
Object类的wait(),wait(long),wait(long,int)  
Thread类的join(),join(long),join(long,int),sleep(long),sleep(long,int)

当一个线程处于sleep、wait、join这三种状态之一时，如果此时线程中断状态为true，
那么就会抛出一个InterruptedException的异常，并将中断状态重新设置为false

```java
publicclass Test1 {
    public static void main(String[] args) throws InterruptedException {
        MyThread thread = new MyThread();
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}

class MyThread extends Thread {
    int i = 0;

    @Override
    public void run() {
        while (true) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("中断异常被捕获了");
                return;
            }
            i++;
        }
    }
}
```
MyThread 线程一直循环打印数字，3s 之后主线程将 MyThread 线程中断，MyThread 线程处于 sleep 状态会自动感应中断，抛出 InterruptedException 异常，线程结束执行。
### 2.6 线程的加入

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
 
### 2.7 线程的阻塞与唤醒


## 三、线程状态的基本操作
![线程状态改变](./img/ThreadStatusChange.png)

除了新建一个线程外，线程在生命周期内还有需要基本操作，而这些操作会成为线程间一种通信方式，比如使用中断（interrupted）方式通知实现线程间的交互等等

![线程状态](./img/ThreadStatus.png)