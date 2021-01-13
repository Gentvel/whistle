package com.whistle.code.thread.juc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer
 * 属性
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#head;// 同步队列头节点
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tail;// 同步队列尾节点
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#state;// 当前锁的状态：0代表没有被占用，大于0代表锁已被线程占用(锁可以重入，每次重入都+1)
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#exclusiveOwnerThread; // 继承自AbstractOwnableSynchronizer 持有当前锁的线程
 * 方法
 * // 锁状态
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#getState()// 返回同步状态的当前值；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#setState(int newState)// 设置当前同步状态；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#compareAndSetState(int expect, int update)// 使用CAS设置当前状态，保证状态设置的原子性；
 *
 * // 独占锁
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire(int arg)// 独占式获取同步状态，如果获取失败则插入同步队列进行等待；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#acquireInterruptibly(int arg)// 与acquire(int arg)相同，但是该方法响应中断；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquireNanos(int arg,long nanos)// 在acquireInterruptibly基础上增加了超时等待功能，在超时时间内没有获得同步状态返回false;
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#release(int arg)// 独占式释放同步状态，该方法会在释放同步状态之后，将同步队列中头节点的下一个节点包含的线程唤醒；
 *
 * // 共享锁
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#acquireShared(int arg)// 共享式获取同步状态，与独占式的区别在于同一时刻有多个线程获取同步状态；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#acquireSharedInterruptibly(int arg)// 在acquireShared方法基础上增加了能响应中断的功能；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquireSharedNanos(int arg, long nanosTimeout)// 在acquireSharedInterruptibly基础上增加了超时等待的功能；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#releaseShared(int arg)// 共享式释放同步状态；
 *
 * // AQS使用模板方法设计模式
 * // 模板方法，需要子类实现获取锁/释放锁的方法
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquire(int arg)// 独占式获取同步状态；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryRelease(int arg)// 独占式释放同步状态；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquireShared(int arg)// 共享式获取同步状态；
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryReleaseShared(int arg)// 共享式释放同步状态；
 *
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node 同步队列节点内部类
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#prev;// 当前节点/线程的前驱节点
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#next;// 当前节点/线程的后继节点
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#thread;// 每一个节点对应一个线
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#waitStatus;// 节点状态
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#CANCELLED =  1;// 节点状态：此线程取消了争抢这个锁
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#SIGNAL = -1;// 节点状态：当前node的后继节点对应的线程需要被唤醒(表示后继节点的状态)
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#CONDITION = -2;// 节点状态：当前节点进入等待队列中
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#PROPAGATE = -3;// 节点状态：表示下一次共享式同步状态获取将会无条件传播下
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#nextWaiter;// 共享模式/独占模式
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#SHARED = new Node();// 共享模式
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.Node#EXCLUSIVE = null;// 独占模式
 *
 *
 * 入队操作
 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#addWaiter(AbstractQueuedSynchronizer.Node)
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicAQS {
    private BasicLockSupport lockSupport = new BasicLockSupport();
}
