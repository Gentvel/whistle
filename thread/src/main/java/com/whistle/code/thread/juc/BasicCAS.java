package com.whistle.code.thread.juc;

import com.whistle.code.thread.keyword.Atomicity;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS，Compare And Swap，
 * 即比较并交换。Doug lea 大神在同步组件中大量使用 CAS 技术鬼斧神工地实现了 Java 多线程的并发操作。
 * 整个 AQS 同步组件、Atomic 原子类操作等等都是以 CAS 实现的。可以说 CAS 是整个 J.U.C 的基石。
 *
 * CAS 比较交换的过程 CAS(V,A,B)：
 * V-一个内存地址存放的实际值、A-旧的预期值、B-即将更新的值，
 * 当且仅当预期值 A 和内存值 V 相同时，将内存值修改为 B 并返回 true，否则什么都不做，并返回 false。
 *
 *
 * CAS VS synchronized
 * synchronized 是线程获取锁是一种悲观锁策略，
 * 即假设每一次执行临界区代码都会产生冲突，所以当前线程获取到锁的之后会阻塞其他线程获取该锁。
 *
 * CAS（无锁操作）是一种乐观锁策略，它假设所有线程访问共享资源的时候不会出现冲突，
 * 所以出现冲突时就不会阻塞其他线程的操作，而是重试当前操作直到没有冲突为止。
 *
 *
 * @see com.whistle.code.thread.keyword.Atomicity 在此问题中，为了解决原子性问题有两种方法如下代码
 *
 * @see Atomicity#increment(); 将此方法加上synchronized 关键字
 *
 * @see Atomicity#count; 将此属性改为AtomicInteger，将increment()方法体改为atomicinteger.getandincrement();
 *
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicCAS {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    private static int increment(){
        return atomicInteger.incrementAndGet();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 30; j++) {
                    increment();
                    System.out.println(Thread.currentThread().getName()+" : "+atomicInteger.get());
                }
            },"Thread-"+i
            ).start();
        }
    }

}
