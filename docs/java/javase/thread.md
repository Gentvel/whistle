---
title: 【Java基础】系列七 多线程基础
date: 2020-08-05
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./io
next: ./serializable
---

## 一、概念
### 1.1 并发编程的好处
- 多核的CPU的背景下，催生了并发编程的趋势，通过并发编程的形式可以将多核CPU的计算能力发挥到极致，性能得到提升。
- 面对复杂业务模型，并行程序会比串行程序更适应业务需求，而并发编程更能吻合这种业务拆分 。
### 1.2 并发编程坏处
- **频繁的上下文切换**  
时间片是CPU分配给各个线程的时间，因为时间非常短，所以CPU不断通过切换线程，让我们觉得多个线程是同时执行的，时间片一般是几十毫秒。而每次切换时，需要保存当前的状态起来，以便能够进行恢复先前状态，而这个切换时非常损耗性能，过于频繁反而无法发挥出多线程编程的优势。通常减少上下文切换可以采用无锁并发编程，CAS算法，使用最少的线程和使用协程。
::: tip 
- **无锁并发编程** 可以参照concurrentHashMap锁分段的思想，不同的线程处理不同段的数据，这样在多线程竞争的条件下，可以减少上下文切换的时间。
- **CAS算法** 利用Atomic下使用CAS算法来更新数据，使用了乐观锁，可以有效的减少一部分不必要的锁竞争带来的上下文切换
- **使用最少线程**避免创建不需要的线程，比如任务很少，但是创建了很多的线程，这样会造成大量的线程都处于等待状态
- **协程**在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换
:::

- **线程安全**  
多线程编程中最难以把握的就是临界区线程安全问题，稍微不注意就会出现死锁的情况，一旦产生死锁就会造成系统功能不可用。
::: tip 可以用如下方式避免死锁的情况：

- 避免一个线程同时获得多个锁；
- 避免一个线程在锁内部占有多个资源，尽量保证每个锁只占用一个资源；
- 尝试使用定时锁，使用lock.tryLock(timeOut)，当超时等待时当前线程不会阻塞；
- 对于数据库锁，加锁和解锁必须在一个数据库连接里，否则会出现解锁失败的情况
:::
所以，如何正确的使用多线程编程技术有很大的学问，比如如何保证线程安全，如何正确理解由于JMM内存模型在原子性，有序性，可见性带来的问题，比如数据脏读，DCL等这些问题。

## 二、应该了解的概念
### 2.1 同步VS异步
同步和异步通常用来形容一次方法调用。同步方法调用一开始，调用者必须等待被调用的方法结束后，调用者后面的代码才能执行。而异步调用，指的是，调用者不用管被调用方法是否完成，都会继续执行后面的代码，当被调用的方法完成后会通知调用者。比如，在超时购物，如果一件物品没了，你得等仓库人员跟你调货，直到仓库人员跟你把货物送过来，你才能继续去收银台付款，这就类似同步调用。而异步调用了，就像网购，你在网上付款下单后，什么事就不用管了，该干嘛就干嘛去了，当货物到达后你收到通知去取就好。
### 2.2 并发与并行
并发和并行是十分容易混淆的概念。并发指的是多个任务交替进行，而并行则是指真正意义上的“同时进行”。实际上，如果系统内只有一个CPU，而使用多线程时，那么真实系统环境下不能并行，只能通过切换时间片的方式交替进行，而成为并发执行任务。真正的并行也只能出现在拥有多个CPU的系统中。
### 2.3 阻塞和非阻塞
阻塞和非阻塞通常用来形容多线程间的相互影响，比如一个线程占有了临界区资源，那么其他线程需要这个资源就必须进行等待该资源的释放，会导致等待的线程挂起，这种情况就是阻塞，而非阻塞就恰好相反，它强调没有一个线程可以阻塞其他线程，所有的线程都会尝试地往前运行。
### 2.4 临界区
临界区用来表示一种公共资源或者说是共享数据，可以被多个线程使用。但是每个线程使用时，一旦临界区资源被一个线程占有，那么其他线程必须等待。

## 三、新建线程
一个java程序从main()方法开始执行，然后按照既定的代码逻辑运行，看似像没有其他线程参与，但实际java程序天生就是一个多线程程序。
1. 分发处理发送给JVM信号的线程
2. 调用对象[finalize()](../jvm/garbage.html#二、判断可触及性)方法的线程
3. 清除[Reference](../jvm/garbage.html#三、引用)的线程
4. main线程，用户线程的入口

### 3.1 创建线程的三种方法
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

## 四、线程状态的基本操作
![线程状态改变](./img/ThreadStatusChange.png)

除了新建一个线程外，线程在生命周期内还有需要基本操作，而这些操作会成为线程间一种通信方式，比如使用中断（interrupted）方式通知实现线程间的交互等等

![线程状态](./img/ThreadStatus.png)