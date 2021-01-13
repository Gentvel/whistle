---
title: 【多线程】 篇  公平锁与非公平锁Fair and NoFair
date: 2020-12-25
sidebar: auto
categories:
- java
tags:
- thread
prev: false
next: false
---

## 简介
为什么需要公平锁？  
CPU会根据不同的调度算法进行线程调度，将时间片分派给线程，那么就可能存在一个问题：某个线程可能一直得不到CPU分配的时间片，这个线程将一直处于等待状态不能执行任务。也就是该线程会一直处于饥饿状态。

为什么出现饥饿？

1. 高优先级线程吞噬低优先级线程的CPU时间
    每个线程都有独自的线程优先级，优先级越高的线程获取的CPU时间片越多，在处于并发状态下的线程包括一个低优先级的线程和多个高优先级的线程，那么这个低优先级的线程将有可能会因为获取不到CPU的时间片而一直处于饥饿状态
2. 线程被永久堵塞在一个等待进入同步块的状态
    当同步锁被占用，线程处于BLOCKED状态等待锁。当锁被释放，处于BLOCKED状态的线程都去抢锁，抢到锁的线程可以被执行，未抢到锁的线程继续在BLOCKED状态阻塞。问题在于这个抢锁的过程中，到底哪个线程能抢到锁是没有任何保障的，这就以为着理论上是会有一个线程会一直抢不到锁，那么它将会永远阻塞下去，导致饥饿。
3. 线程在一个对象上等待，但一直没有被唤醒。
    当一个线程调用Object.wait()之后被唤醒，知道被Object.notify()唤醒。而这个方法是随机选取一个线程唤醒的，不能保证哪个线程会获得唤醒。因此如果多个线程都在一个对象的wait()上阻塞，在没有调用足够多的Object.notify()时，理论上是会有一个线程因为一直得不到唤醒而处于WAITING状态，从而导致饥饿


怎么解决饥饿？  

解决饥饿的方案被称之为公平性，即所有线程能公平地获得运行机会。
公平性针对获取锁而言的，如果一个锁是公平的，那么锁的获取顺序就应该符合请求上的绝对时间顺序，满足 FIFO。

## 公平锁与非公平锁的实现

首先，使用重入锁实现同步的过程如下：

1. A线程调用lock()加锁，判断state=0，直接能获取到锁，设置state=1，exclusiveOwnerThread=A线程
2. B线程调用lock()加锁，判断state=1,exclusiveOwnerThread=A线程，锁已经被持有，那么此时线程B会封装成Node节点加入同步队列中等待。此时A线程执行同步代码，B线程阻塞等待
3. A线程调用unlock()释放锁，判断exclusiveOwnerThread=A线程，可以释放锁。设置state-1，exclusiveOwnerThread=null。state变为0时，唤醒AQS同步队列中的head的后继节点，也就是B线程。
4. B线程被唤醒，获取锁

获取锁的调用为： lock()->acquire()->tryAcquire()->acquire()

acquire()是父类 AQS 的方法，公平锁与非公平锁都一样，不同之处在于 lock()和 tryAcquire()。

lock()方法源码：

```java
// 公平锁FairSync
final void lock() {
    acquire(1);
}

// 非公平锁NonfairSync
final void lock() {
    // 在调用acquire()方法获取锁之前，先CAS抢锁
    if (compareAndSetState(0, 1)) // state=0时，CAS设置state=1
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}
```
可以看到，非公平锁在调用 acquire()方法获取锁之前，先利用 CAS 将 state 修改为 1，如果成功就将 exclusiveOwnerThread 设置为当前线程。
state 是锁的标志，利用 CAS 将 state 从 0 修改为 1 就代表获取到了该锁。

所以非公平锁和公平锁的不同之处在于lock()之后，**公平锁直接调用 acquire()方法，而非公平锁先利用 CAS 抢锁，如果 CAS 获取锁失败再调用 acquire()方法。**


那么，非公平锁先利用 CAS 抢锁到底有什么作用呢？

回忆一下释放锁的过程 AQS.release()方法：

1. state 改为 0，exclusiveOwnerThread 设置为 null
2. 唤醒 AQS 队列中 head 的后继结点线程去获取锁

如果在线程 2 在线程 1 释放锁的过程中调用 lock()方法获取锁，

**对于公平锁：线程 2 只能先加入同步队列的队尾，等队列中在它之前的线程获取、释放锁之后才有机会去抢锁。这也就保证了公平，先到先得。**

**对于非公平锁：线程 1 释放锁过程执行到一半，“①state 改为 0，exclusiveOwnerThread 设置为 null”已经完成，此时线程 2 调用 lock()，那么 CAS 就抢锁成功。这种情况下线程 2 是可以先获取非公平锁而不需要进入队列中排队的，也就不公平了。**

tryAcquire()方法源码：
```java
// 公平锁
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {// state==0表示没有线程占用锁
        if (!hasQueuedPredecessors() && // AQS队列中没有结点时，再去获取锁
            compareAndSetState(0, acquires)) { // CAS获取锁
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {// 重入
        int nextc = c + acquires;
        if (nextc < 0)
            thrownew Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

// 非公平锁
protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}

final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {// state==0表示没有线程占用锁
        if (compareAndSetState(0, acquires)) {// CAS获取锁
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {// 重入
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            thrownew Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

两个 tryAcquire()方法只有一行代码不同，公平锁多了一行!hasQueuedPredecessors()。hasQueuedPredecessors()方法是判断 AQS 队列中是否还有结点，如果队列中没有结点返回 false。

公平锁的 tryAcquire()：如果 AQS 同步队列中仍然有线程在排队，即使这个时刻没有线程占用锁时，当前线程也是不能去抢锁的，这样可以保证先来等锁的线程先有机会获取锁。

非公平锁的 tryAcquire()：**只要当前时刻没有线程占用锁，不管同步队列中是什么情况，当前线程都可以去抢锁。**如果当前线程抢到了锁，对于那些早早在队列中排队等锁的线程就是不公平的了。

::: tip 总结
非公平锁和公平锁只有两处不同：
- lock()方法：
    公平锁直接调用 acquire()，当前线程到同步队列中排队等锁。
    非公平锁会先利用 CAS 抢锁，抢不到锁才会调用 acquire()。
- tryAcquire()方法：
    公平锁在同步队列还有线程等锁时，即使锁没有被占用，也不能获取锁。
    非公平锁不管同步队列中是什么情况，直接去抢锁。
:::


非公平锁有可能导致线程永远无法获取到锁，造成饥饿现象。而公平锁保证线程获取锁的顺序符合请求上的时间顺序，满足 FIFO，可以解决饥饿问题。

公平锁为了保证时间上的绝对顺序，需要频繁的上下文切换，性能开销较大。而非公平锁会降低一定的上下文切换，有更好的性能，可以保证更大的吞吐量，这也是 **ReentrantLock 默认选择的是非公平锁**的原因。