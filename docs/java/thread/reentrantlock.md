---
title: 【多线程】 篇 ReentrantLock
date: 2020-12-24
sidebar: auto
categories:
- java
tags:
- thread
prev: false
next: false
---

## 简介
ReentrantLock是Lock中用到最多的，与synchronized具有相同的功能和内存语义，不同的是synchronized同步块执行完成或者遇到异常是锁会自动释放，而lock必须调用unlock()方法释放锁。ReentrantLock为了保证在获取到锁之后，最终能够被释放，在finally块中释放锁。

## 类结构
```java
publi cclass ReentrantLock implements Lock, java.io.Serializable {
    private final Sync sync;
    abstract static class Sync extends AbstractQueuedSynchronizer {}
    static final class FairSync extends Sync {}
    static final class NonfairSync extends Sync {}
}
```
ReentrantLock用内部类Sync来管理锁，所以真正的获取锁和释放锁是由Sync的实现类来控制的。

Sync有两个实现，分别为NonfairSync（非公平锁）和FairSync（公平锁），以FairSync为例来讲解ReentrantLock，之后会专门分析公平锁和非公平锁。

## 获取锁
```java
/**
 * ReentrantLock.FairSync.tryAcquire(int)
 * 实现了AQS的抢锁方法，抢锁成功返回true
 * 获取锁成功的两种情况：
 * 1.没有线程占用锁，且AQS队列中没有其他线程等锁，且CAS修改state成功。
 * 2.锁已经被当前线程持有，直接重入。
 */
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();// AQS的state (FairSync extends Sync extends AQS)
    if (c == 0) {// state==0表示当前没有线程占用锁
        if (!hasQueuedPredecessors() && // AQS同步队列中没有其他线程等锁的话，当前线程可以去抢锁，此方法下文有详解
            compareAndSetState(0, acquires)) {// CAS修改state，修改成功表示获取到了锁
            setExclusiveOwnerThread(current);// 抢锁成功将AQS.exclusiveOwnerThread置为当前线程
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        /*
         * AQS.exclusiveOwnerThread是当前线程，表示锁已经被当前线程持有，这里是锁重入
         * 重入一次将AQS.state加1
         */
        int nextc = c + acquires;
        if (nextc < 0)
            thrownew Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}

/**
 * AbstractQueuedSynchronizer.hasQueuedPredecessors()
 * 判断AQS同步队列中是否还有其他线程在等锁
 * 返回true表示当前线程不能抢锁，需要到同步队列中排队；返回false表示当前线程可以去抢锁
 * 三种情况：
 * 1.队列为空不需要排队， head==tail，直接返回false
 * 2.head后继节点的线程是当前线程，就算排队也轮到当前线程去抢锁了，返回false
 * 3.其他情况都返回true，不允许抢锁
 */
public final boolean hasQueuedPredecessors() {
    Node t = tail;
    Node h = head;
    Node s;
    return h != t && // head==tail时队列是空的，直接返回false
        ((s = h.next) == null || s.thread != Thread.currentThread());// head后继节点的线程是当前线程，返回false
}
```
## 释放锁
```java
/**
 * AbstractQueuedSynchronizer.release(int)-->ReentrantLock.Sync.tryRelease(int)
 * 释放重入锁。只有锁彻底释放，其他线程可以来竞争锁才返回true
 * 锁可以重入，state记录锁的重入次数，所以state可以大于1
 * 每执行一次tryRelease()将state减1，直到state==0，表示当前线程彻底把锁释放
 */
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        thrownew IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```

## 重入
线程T获取到锁，AQS.state=1，AQS.exclusiveOwnerThread置为线程T。

线程T没释放锁之前再次调用lock()加锁，判断AQS.exclusiveOwnerThread==线程T，就可以直接执行不会阻塞，此时AQS.state加1。

此时线程T再次调用lock()加锁，继续重入，AQS.state再加1，此时state==2。

线程T执行完部分同步代码，调用unlock()解锁，AQS.state减1，此时state==1，线程T还持有该锁，其他线程还无法来竞争锁。

线程T执行完所有同步代码，调用unlock()解锁，AQS.state减1，此时state==0，线程将锁释放，允许其他线程来竞争锁。
