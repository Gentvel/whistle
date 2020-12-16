package com.whistle.code.thread.juc;

/**
 * 可重入锁
 * @see java.util.concurrent.locks.ReentrantLock
 * @see java.util.concurrent.locks.ReentrantLock.Sync
 * @see java.util.concurrent.locks.ReentrantLock.FairSync
 * @see java.util.concurrent.locks.ReentrantLock.NonfairSync
 *
 * ReentrantLock用内部类Sync来管理锁，所以真正的获取锁和释放锁是由Sync的实现类来控制的。
 *
 * Sync有两个实现，分别为NonfairSync（非公平锁）和FairSync（公平锁）
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicReentrantLock {
}
