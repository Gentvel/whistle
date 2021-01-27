---
title: 【多线程】 篇十一 线程池基础
date: 2020-11-03
sidebar: auto
categories:
 - 多线程
tags:
- 多线程
prev: ./reentrantreadwritelock
next: ./javapool
---

## 概述
**什么是线程池？**   
线程池其实就是一种多线程处理形式，处理过程中可以将任务添加到队列中，然后在创建线程后自动启动这些任务。

**为什么要使用线程池？**  
线程池可以根据系统的需求和硬件环境灵活的控制线程的数量，且可以对所有线程进行统一的管理和控制，从而提高系统运行的效率，降低系统运行压力

**线程池有哪些优势？**  
1. 线程和任务分离，提升线程重用性
2. 控制线程并发数量，降低服务器压力，统一管理所有线程
3. 提升系统响应速度，减少系统创建线程、线程调度和销毁线程带来的开销

## 线程池构造函数

```java
    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     *        核心线程数，即使它们处于空闲状态也要保留在池中的线​​程数，除非设置了allowCoreThreadTimeOut参数
     *         当任务提交到线程池中，如果核心线程数没有达到当前定义的数量，那么就会新建一个核心线程，
     *         如果达到核心线程数时，首先将线程加入到任务队列中，当任务队列也满了时，可以通过下面的最大线程数控制创建非核心线程
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     *        控制服务器能运行在最大的一个线程压力数
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     *        最大空闲时间，当设置了这个参数时，空闲的线程将不再保留到线程池中，超过这个时间就销毁
     * @param unit the time unit for the {@code keepAliveTime} argument
     *        最大空闲时间的时间单位
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     *        任务队列，当线程数量达到核心线程时，新的任务将会加入任务队列
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     *        线程工厂，允许自定义创建线程的过程
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     *        饱和处理机制- 拒绝策略，当任务达到最大线程数量和任务队列也满时，处理当前任务的机制
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,//核心线程数
                              int maximumPoolSize,//最大线程数
                              long keepAliveTime,//最大空闲时间
                              TimeUnit unit,//最大空闲时间单位
                              BlockingQueue<Runnable> workQueue,//任务队列
                              ThreadFactory threadFactory,//线程工厂
                              RejectedExecutionHandler handler) {//拒绝策略
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```

## 参数设计分析
- 核心线程数：  
    核心线程数要根据任务的处理时长和每秒产生的任务数量来确定。**0.8\*系统平均每秒产生任务数/(1/单个任务执行时间)** 假设执行一个任务需要0.5秒钟，系统每秒能产生20个任务，如果想要在一秒内处理完这些任务，就需要10【20/（1/0.5）】个核心线程数，但是系统不一定每次都能产生20个任务，所以根据二八定理，将百分之二十的任务提交到任务队列中执行，将核心线程数设置成8就行。
- 任务队列长度：  
    任务队列长度一般设计为：**核心线程数/单个任务执行时间*2**即可 10/0.5\*2=40个
- 最大线程数  
    最大线程数需要系统每秒产生最大任务数量和核心线程数决定，**(最大任务数-任务队列长度)*单个任务执行时间**如果上述环境中，系统每秒最大任务数为100个，那么100-40=60\*0.5=30
- 最大空闲时间  
    这个参数的设计可以参考系统运行环境和硬件来确定，设置一个合理的值即可

## ExecutorService
ExecutorService是java提供的线程池接口。
```java
public interface ExecutorService extends Executor {

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     * 
     * <p>This method does not wait for previously submitted tasks to
     * complete execution.  Use {@link #awaitTermination awaitTermination}
     * to do that.
     * 
     * @throws SecurityException if a security manager exists and
     *         shutting down this ExecutorService may manipulate
     *         threads that the caller is not permitted to modify
     *         because it does not hold {@link
     *         java.lang.RuntimePermission}{@code ("modifyThread")},
     *         or the security manager's {@code checkAccess} method
     *         denies access.
     */

    void shutdown();

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks, and returns a list of the tasks
     * that were awaiting execution.
     *
     * <p>This method does not wait for actively executing tasks to
     * terminate.  Use {@link #awaitTermination awaitTermination} to
     * do that.
     *
     * <p>There are no guarantees beyond best-effort attempts to stop
     * processing actively executing tasks.  For example, typical
     * implementations will cancel via {@link Thread#interrupt}, so any
     * task that fails to respond to interrupts may never terminate.
     *
     * @return list of tasks that never commenced execution
     * @throws SecurityException if a security manager exists and
     *         shutting down this ExecutorService may manipulate
     *         threads that the caller is not permitted to modify
     *         because it does not hold {@link
     *         java.lang.RuntimePermission}{@code ("modifyThread")},
     *         or the security manager's {@code checkAccess} method
     *         denies access.
     */
    List<Runnable> shutdownNow();

    /**
     * Returns {@code true} if this executor has been shut down.
     *
     * @return {@code true} if this executor has been shut down
     */
    boolean isShutdown();

    /**
     * Returns {@code true} if all tasks have completed following shut down.
     * Note that {@code isTerminated} is never {@code true} unless
     * either {@code shutdown} or {@code shutdownNow} was called first.
     *
     * @return {@code true} if all tasks have completed following shut down
     */
    boolean isTerminated();

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return {@code true} if this executor terminated and
     *         {@code false} if the timeout elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     */
    boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Submits a value-returning task for execution and returns a
     * Future representing the pending results of the task. The
     * Future's {@code get} method will return the task's result upon
     * successful completion.
     *
     * <p>
     * If you would like to immediately block waiting
     * for a task, you can use constructions of the form
     * {@code result = exec.submit(aCallable).get();}
     *
     * <p>Note: The {@link Executors} class includes a set of methods
     * that can convert some other common closure-like objects,
     * for example, {@link java.security.PrivilegedAction} to
     * {@link Callable} form so they can be submitted.
     *
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * Submits a Runnable task for execution and returns a Future
     * representing that task. The Future's {@code get} method will
     * return the given result upon successful completion.
      提交Runnable接口的任务如果成功执行返回至给定的result参数
     */
    <T> Future<T> submit(Runnable task, T result);

    /**
    提交任务
     */
    Future<?> submit(Runnable task);

    /**
      执行所有任务
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException;

    /**
      执行所有任务，返回list封装所有结果
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
      执行给定线程中的其中一个并返回结果
     */
    <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException;

    /**
      执行给定线程中的其中一个
     */
    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

`shutdown` 启动一次顺序关闭，执行以前提交的任务，但不接受新任务  
`shutdownNow`停止所有正在执行的任务，暂停处理正在等待的任务，并返回等待执行的任务的列表

## Executors
Executors能是java提供的创建线程池的方法，提供了4种线程池的创建
### CachedThreadPool
```java
/**
  * Creates a thread pool that creates new threads as needed, but
  * will reuse previously constructed threads when they are
  * available.  These pools will typically improve the performance
  * of programs that execute many short-lived asynchronous tasks.
  * Calls to {@code execute} will reuse previously constructed
  * threads if available. If no existing thread is available, a new
  * thread will be created and added to the pool. Threads that have
  * not been used for sixty seconds are terminated and removed from
  * the cache. Thus, a pool that remains idle for long enough will
  * not consume any resources. Note that pools with similar
  * properties but different details (for example, timeout parameters)
  * may be created using {@link ThreadPoolExecutor} constructors.
  *
  * @return the newly created thread pool
  */
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());
}
```
可以看到CachedThreadPool种的核心线程数为0，但是最大线程数为2的32次方，这是一个很大的数量，为了控制系统不会无限的创建线程造成OOM将最大空闲时间设置为60秒

```java
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
});
```
以上代码会在执行时创建第一个线程，执行完后线程挂起等待60秒，如果60秒内没有新的任务将会被挂起等待，期间有任务再使用已经创建的线程。
这种线程池适合执行时间短，但是量大的情况。

###  FixedThreadPool
```java
/**
* Creates a thread pool that reuses a fixed number of threads
* operating off a shared unbounded queue.  At any point, at most
* {@code nThreads} threads will be active processing tasks.
* If additional tasks are submitted when all threads are active,
* they will wait in the queue until a thread is available.
* If any thread terminates due to a failure during execution
* prior to shutdown, a new one will take its place if needed to
* execute subsequent tasks.  The threads in the pool will exist
* until it is explicitly {@link ExecutorService#shutdown shutdown}.
*
* @param nThreads the number of threads in the pool
* @return the newly created thread pool
* @throws IllegalArgumentException if {@code nThreads <= 0}
*/
public static ExecutorService newFixedThreadPool(int nThreads) {
  return new ThreadPoolExecutor(nThreads, nThreads,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>());
}

/**
  * Creates a {@code LinkedBlockingQueue} with a capacity of
  * {@link Integer#MAX_VALUE}.
  */
public LinkedBlockingQueue() {
    this(Integer.MAX_VALUE);
}

/**
  * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
  *
  * @param capacity the capacity of this queue
  * @throws IllegalArgumentException if {@code capacity} is not greater
  *         than zero
  */
public LinkedBlockingQueue(int capacity) {
    if (capacity <= 0) throw new IllegalArgumentException();
    this.capacity = capacity;
    last = head = new Node<E>(null);
}

/**
  * Linked list node class.
  */
static class Node<E> {
    E item;

    /**
      * One of:
      * - the real successor Node
      * - this Node, meaning the successor is head.next
      * - null, meaning there is no successor (this is the last node)
      */
    Node<E> next;

    Node(E x) { item = x; }
}
```
可以看出FixedThreadPool使用的是单向链表的线程队列，这个队列的最大空间为整数最大值，而且最大空闲时间为0，也就是空闲线程不会被回收
```java
ExecutorService executorService1 = Executors.newFixedThreadPool(2);
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
});
```

先执行第一二个任务，等执行完后再选择一个线程执行第三个任务，线程将不会被回收。

### SingleThreadPool
```java
/**
  * Creates an Executor that uses a single worker thread operating
  * off an unbounded queue. (Note however that if this single
  * thread terminates due to a failure during execution prior to
  * shutdown, a new one will take its place if needed to execute
  * subsequent tasks.)  Tasks are guaranteed to execute
  * sequentially, and no more than one task will be active at any
  * given time. Unlike the otherwise equivalent
  * {@code newFixedThreadPool(1)} the returned executor is
  * guaranteed not to be reconfigurable to use additional threads.
  *
  * @return the newly created single-threaded Executor
  */
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
```

创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
可以看出，使用的任务队列和FiexdThreadPool是一样的单向队列

### ScheduledThreadPool
```java
/**
  * Creates a new {@code ScheduledThreadPoolExecutor} with the
  * given core pool size.
  *
  * @param corePoolSize the number of threads to keep in the pool, even
  *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
  * @throws IllegalArgumentException if {@code corePoolSize < 0}
  */
public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE,
          DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
          new DelayedWorkQueue());
}
```
一个定长线程池，支持定时及周期性任务执行

```java
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
scheduledExecutorService.schedule(()->{
    System.out.println(Thread.currentThread().getName());
},1,TimeUnit.SECONDS);
scheduledExecutorService.scheduleAtFixedRate(()->{
    System.out.println(Thread.currentThread().getName());
},1,2,TimeUnit.SECONDS);
```

有两种方法，一种是schedule只支持延时执行当前的任务，另一种scheduleAtFixedRate支持每隔多少个时间单位后执行，第二个参数是第一次执行延迟时间，第三个参数是隔多少时间单位使用

### BlockingQueue
缓冲队列BlockingQueue简介：

BlockingQueue是双缓冲队列。BlockingQueue内部使用两条队列，允许两个线程同时向队列一个存储，一个取出操作。在保证并发安全的同时，提高了队列的存取效率。

常用的几种BlockingQueue：

- ArrayBlockingQueue（int i）:规定大小的BlockingQueue，其构造必须指定大小。其所含的对象是FIFO顺序排序的。

- LinkedBlockingQueue（）或者（int i）:大小不固定的BlockingQueue，若其构造时指定大小，生成的BlockingQueue有大小限制，不指定大小，其大小有Integer.MAX_VALUE来决定。其所含的对象是FIFO顺序排序的。

- PriorityBlockingQueue（）或者（int i）:类似于LinkedBlockingQueue，但是其所含对象的排序不是FIFO，而是依据对象的自然顺序或者构造函数的Comparator决定。

- SynchronousQueue（）:特殊的BlockingQueue，对其的操作必须是放和取交替完成。默认采用非公平锁的形式切换线程的使用