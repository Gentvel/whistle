---
title: 【多线程】 篇九  读写锁
date: 2020-10-19
sidebar: auto
categories:
 - 多线程
tags:
- 多线程
prev: ./fair
next: ./stampedlock
---

## 简介
在并发编程中解决线程安全的问题，通常使用的都是java提供的关键字synchronized或者重入锁ReentrantLock。它们都是独占式获取锁，也就是在同一时刻只有一个线程能够获取锁。

但是在大多数场景下，大部分时间都是读取共享资源，对共享资源的写操作很少。**然而读服务不存在数据竞争问题，如果一个线程在读时禁止其他线程读势必会导致性能降低。**

针对这种读多写少的情况，java还提供了另外一个实现Lock接口的ReentrantReadWriteLock(读写锁)。读写锁允许共享资源在同一时刻可以被多个读线程访问，但是在写线程访问时，所有的读线程和其他的写线程都会被阻塞。

测试：
```java
public class BasicReentrantReadWriteLock {

    public static void main(String[] args) {
        Data data = new Data();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                data.put(Math.random());
            }).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                data.get();
            }).start();
        }
        data.toString();
    }


    private static class Data {
        private Object data;
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

        public void get() {
            reentrantReadWriteLock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "读取数据");
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + "读取数据完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
        }

        public void put(Object o) {
            reentrantReadWriteLock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + "开始写数据");
            this.data = o;
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + "写入完成" + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }

        @Override
        public String toString() {
            return reentrantReadWriteLock.toString();
        }
    }
}
```

## 类结构
```java
public class ReentrantReadWriteLock implements ReadWriteLock, java.io.Serializable {
    // 属性
    private final ReentrantReadWriteLock.ReadLock readerLock; // 读锁
    private final ReentrantReadWriteLock.WriteLock writerLock; // 写锁
    final Sync sync; // 锁的主体AQS
    
    // 内部类
    abstract staticclass Sync extends AbstractQueuedSynchronizer {}
    static final class FairSync extends Sync {}
    static final class NonfairSync extends Sync {}
    public static class ReadLock implements Lock, java.io.Serializable {}
    public static class WriteLock implements Lock, java.io.Serializable {}
	
    // 构造
    public ReentrantReadWriteLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
        readerLock = new ReadLock(this);
        writerLock = new WriteLock(this);
    }
}
```

ReentrantReadWriteLock与ReentrantLock一样，其锁主体依然是Sync，读写锁其实就是两个属性：readerLock、writerLock。

一个ReentrantReadWriteLock对象都对应着读锁和写锁两个锁，而这两个锁是通过同一个sync（AQS）实现的。

## 记录读写锁状态
我们知道AQS.state使用来表示同步状态的。ReentrantLock中，state=0表示没有线程占用锁，state>0时state表示线程的重入次数。但是读写锁ReentrantReadWriteLock内部维护着两个锁，需要用state这一个变量维护多种状态，应该怎么办呢？

读写锁采用“按位切割使用”的方式，**将state这个int变量分为高16位和低16位，高16位记录读锁状态，低16位记录写锁状态**，并通过位运算来快速获取当前的读写锁状态。
```java
abstract static class Sync extends AbstractQueuedSynchronizer {
	// 将state这个int变量分为高16位和低16位，高16位记录读锁状态，低16位记录写锁状态
    static final int SHARED_SHIFT   = 16;
    static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
    static final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
    static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;
    
    /**
     * 获取读锁的状态，读锁的获取次数(包括重入)
     * c无符号补0右移16位，获得高16位
     */
    static int sharedCount(int c)    { return c >>> SHARED_SHIFT; }
    
    /**
     * 获取写锁的状态，写锁的重入次数
     * c & 0x0000FFFF，将高16位全部抹去，获得低16位
     */
    static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }
}
```

### 记录获取锁的线程
线程获取写锁后，和重入锁一样，将AQS.exclusiveOwnerThread置为当前线程。但是读锁是共享的，可以多个线程同时获取读锁，那么如何记录获取读锁的多个线程以及每个线程的重入情况呢？

**sycn中提供了一个HoldCounter类，类似计数器，用于记录一个线程读锁的重入次数。将HoldCounter通过ThreadLocal与线程绑定。**

```java
abstract static class Sync extends AbstractQueuedSynchronizer {
    // 这个嵌套类的实例用来记录每个线程持有的读锁数量(读锁重入)
    static final class HoldCounter {
        int count = 0;// 读锁重入次数
        finallong tid = getThreadId(Thread.currentThread());// 线程 id
    }

    // ThreadLocal 的子类
    static final class ThreadLocalHoldCounter
        extends ThreadLocal<HoldCounter> {
        public HoldCounter initialValue() {
            returnnew HoldCounter();
        }
    }
    
    // 组合使用上面两个类，用一个 ThreadLocal 来记录当前线程持有的读锁数量
    private transient ThreadLocalHoldCounter readHolds;
 
    private transient HoldCounter cachedHoldCounter;// 记录"最后一个获取读锁的线程"的读锁重入次数，用于缓存提高性能
    private transient Thread firstReader = null;// 第一个获取读锁的线程(并且其未释放读锁)
    private transient int firstReaderHoldCount;// 第一个获取读锁的线程重入的读锁数量
}
```
:::tip
属性cachedHoldCounter、firstReader、firstReaderHoldCount都是为了提高性能
:::

## 读锁
### 获取读锁
```java
/**
 * rwl.readLock().lock()-->ReadLock.lock()
 */
public void lock() {
    sync.acquireShared(1);
}

/**
 * ReadLock.lock()-->AQS.acquireShared(int)
 */
public final void acquireShared(int arg) {
    if (tryAcquireShared(arg) < 0)
        doAcquireShared(arg);
}

/**
 * 尝试获取读锁，获取到锁返回1，获取不到返回-1
 */
protected final int tryAcquireShared(int unused) {
    Thread current = Thread.currentThread();
    int c = getState();
    /*
     * 根据锁的状态判断可以获取读锁的情况：
	 * 1. 读锁写锁都没有被占用
	 * 2. 只有读锁被占用
	 * 3. 写锁被自己线程占用
	 * 总结一下，只有在其它线程持有写锁时，不能获取读锁，其它情况都可以去获取。
     */
    if (exclusiveCount(c) != 0 && // 写锁被占用
        getExclusiveOwnerThread() != current) // 持有写锁的不是当前线程
        return -1;
    
    int r = sharedCount(c);
    if (!readerShouldBlock() && // 检查AQS队列中的情况，看是当前线程是否可以获取读锁，下文有详细讲解。
        r < MAX_COUNT &&		// 读锁的标志位只有16位，最多只能有2^16-1个线程获取读锁或重入
        compareAndSetState(c, c + SHARED_UNIT)) {// 在state的第17位加1，也就是将读锁标志位加1
    	/*
    	 * 到这里已经获取到读锁了
    	 * 以下是修改记录获取读锁的线程和重入次数，以及缓存firstReader和cachedHoldCounter
    	 */
        if (r == 0) {
            firstReader = current;
            firstReaderHoldCount = 1;
        } elseif (firstReader == current) {
            firstReaderHoldCount++;
        } else {
            HoldCounter rh = cachedHoldCounter;
            if (rh == null || rh.tid != getThreadId(current))
                cachedHoldCounter = rh = readHolds.get();
            elseif (rh.count == 0)
                readHolds.set(rh);
            rh.count++;
        }
        return 1;
    }
    
    /*
     * 到这里
     * 没有获取到读锁，因为上面代码获取到读锁的话已经在上一个if里返回1了
     * 锁的状态是满足获取读锁的，因为不满足的上面返回-1了
     * 所以没有获取到读锁的原因：AQS队列不满足获取读锁条件，或者CAS失败，或者16位标志位满了
     * 像CAS失败这种原因，是一定要再尝试获取的，所以这里再次尝试获取读锁，fullTryAcquireShared()方法下文有详细讲解
     */
    return fullTryAcquireShared(current);
}

/**
 * 再次尝试获取读锁
 */
final int fullTryAcquireShared(Thread current) {
    HoldCounter rh = null;
    for (;;) {// 注意这里是循环
        int c = getState();
        if (exclusiveCount(c) != 0) {
        	// 仍然是先检查锁状态：在其它线程持有写锁时，不能获取读锁，返回-1
            if (getExclusiveOwnerThread() != current)
                return -1;
        } elseif (readerShouldBlock()) {
        	/*
        	 * exclusiveCount(c) == 0 写锁没有被占用
        	 * readerShouldBlock() == true，AQS同步队列中的线程在等锁，当前线程不能抢读锁
        	 * 既然当前线程不能抢读锁，为什么没有直接返回呢？
        	 * 因为这里还有一种情况是可以获取读锁的，那就是读锁重入。
        	 * 以下代码就是检查如果不是重入的话，return -1，不能继续往下获取锁。
        	 */
            if (firstReader == current) {
            	// assert firstReaderHoldCount > 0;
            } else {
                if (rh == null) {
                    rh = cachedHoldCounter;
                    if (rh == null || rh.tid != getThreadId(current)) {
                        rh = readHolds.get();
                        if (rh.count == 0)
                            readHolds.remove();
                    }
                }
                if (rh.count == 0)
                    return -1;
            }
        }
        
        if (sharedCount(c) == MAX_COUNT)
            thrownew Error("Maximum lock count exceeded");
        
        // CAS修改读锁标志位，修改成功表示获取到读锁；CAS失败，则进入下一次for循环继续CAS抢锁
        if (compareAndSetState(c, c + SHARED_UNIT)) {
        	/*
        	 * 到这里已经获取到读锁了
        	 * 以下是修改记录获取读锁的线程和重入次数，以及缓存firstReader和cachedHoldCounter
        	 */
            if (sharedCount(c) == 0) {
                firstReader = current;
                firstReaderHoldCount = 1;
            } elseif (firstReader == current) {
                firstReaderHoldCount++;
            } else {
                if (rh == null)
                    rh = cachedHoldCounter;
                if (rh == null || rh.tid != getThreadId(current))
                    rh = readHolds.get();
                elseif (rh.count == 0)
                    readHolds.set(rh);
                rh.count++;
                cachedHoldCounter = rh; // cache for release
            }
            return1;
        }
    }
}
```

只有在其它线程持有写锁时，不能获取读锁，其它情况都可以去获取。

AQS队列中的情况，如果是公平锁，同步队列中有线程等锁时，当前线程是不可以先获取锁的，必须到队列中排队。

读锁的标志位只有16位，最多只能有2^16-1个线程获取读锁或重入

试想一种情况：当线程1持有写锁时，线程2、线程3、线程4、线程5...来获取读锁是获取不到的，只能排进同步队列。当线程1释放写锁时，唤醒线程2来获取锁。因为读锁是共享锁，当线程2获取到读锁时，线程3也应该被唤醒来获取读锁。

setHeadAndPropagate()方法就是在一个线程获取读锁之后，唤醒它之后排队获取读锁的线程的。该方法可以保证线程2获取读锁后，唤醒线程3获取读锁，线程3获取读锁后，唤醒线程4获取读锁，直到遇到后继节点是要获取写锁时才结束。
```java
private void setHeadAndPropagate(Node node, int propagate) {
    Node h = head;
    setHead(node);// 因为node获取到锁了，所以设置node为head
    if (propagate > 0 || h == null || h.waitStatus < 0 ||
        (h = head) == null || h.waitStatus < 0) {
        Node s = node.next;
        if (s == null || s.isShared())// node后继节点线程要获取读锁，此时node就是head
            doReleaseShared();// 唤醒head后继节点（也就是node.next）获取锁
    }
}
```

### 读锁释放
```java
/**
 * rwl.readLock().unlock()-->ReadLock.unlock()
 */
public void unlock() {
    sync.releaseShared(1);
}

/**
 * sync.releaseShared(1)-->AQS.releaseShared(int)
 */
public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {// 当前线程释放读锁，下文介绍
    	/*
    	 * 到这里，已经没有任何线程占用锁，调用doReleaseShared()唤醒之后获取写锁的线程
    	 * 如果同步队列中还有线程在排队，head后继节点的线程一定是要获取写锁，因为线程持有读锁时会把它之后要获取读锁的线程全部唤醒
    	 */
        doReleaseShared();// 唤醒head后继节点获取锁
        returntrue;
    }
    returnfalse;
}

/**
 * 释放读锁
 * 当前线程释放读锁之后，没有线程占用锁，返回true
 */
protected final boolean tryReleaseShared(int unused) {
    Thread current = Thread.currentThread();
    // 处理firstReader、cachedHoldCounter、readHolds获取读锁线程及读锁重入次数
    if (firstReader == current) {
        // assert firstReaderHoldCount > 0;
        if (firstReaderHoldCount == 1)
            firstReader = null;
        else
            firstReaderHoldCount--;
    } else {
        HoldCounter rh = cachedHoldCounter;
        if (rh == null || rh.tid != getThreadId(current))
            rh = readHolds.get();
        int count = rh.count;
        if (count <= 1) {
            readHolds.remove();
            if (count <= 0)
                throw unmatchedUnlockException();
        }
        --rh.count;
    }
    
    for (;;) {
        int c = getState();
        int nextc = c - SHARED_UNIT;// state第17位-1，也就是读锁状态标志位-1
        if (compareAndSetState(c, nextc))// CAS设置state，CAS失败自旋进入下一次for循环
            return nextc == 0;// state=0表示没有线程占用锁，返回true
    }
}
```

大多数业务场景，都是读多写少的，采用互斥锁性能较差，所以提供了读写锁。读写锁允许共享资源在同一时刻可以被多个读线程访问，但是在写线程访问时，所有的读线程和其他的写线程都会被阻塞。

一个ReentrantReadWriteLock对象都对应着读锁和写锁两个锁，而这两个锁是通过同一个sync（AQS）实现的。

读写锁采用“按位切割使用”的方式，将state这个int变量分为高16位和低16位，高16位记录读锁状态，低16位记录写锁状态。

读锁获取时，需要判断当时的写锁没有被其他线程占用即可，锁处于的其他状态都可以获取读锁。

## 写锁

### 写锁获取
```java
public void lock() {
    sync.acquire(1);
}


public final void acquire(int arg) {
    if (!tryAcquire(arg) && // 写锁实现了获取锁的方法，下文详细讲解
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) // 获取锁失败进入同步队列，等待被唤醒，AQS一文中重点讲过
        selfInterrupt();
}

/**
 * ReentrantReadWriteLock.Sync.tryAcquire(int)
 */
protected final boolean tryAcquire(int acquires) {
    Thread current = Thread.currentThread();
    int c = getState();
    int w = exclusiveCount(c);// 写锁标志位
    
    // 进到这个if里，c!=0表示有线程占用锁
    // 当有线程占用锁时，只有一种情况是可以获取写锁的，那就是写锁重入
    if (c != 0) {
        /*
         * 两种情况返回false
         * 1.(c != 0 & w == 0)
         * c!=0表示标志位!=0，w==0表示写锁标志位==0，总的标志位不为0而写锁标志位(低16位)为0，只能是读锁标志位(高16位)不为0
         * 也就是有线程占用读锁，此时不能获取写锁，返回false
         *
         * 2.(c != 0 & w != 0 & current != getExclusiveOwnerThread())
         * c != 0 & w != 0 表示写锁标志位不为0，有线程占用写锁
         * current != getExclusiveOwnerThread() 占用写锁的线程不是当前线程
         * 不能获取写锁，返回false
         */
        if (w == 0 || current != getExclusiveOwnerThread())
            returnfalse;
        // 重入次数不能超过2^16-1
        if (w + exclusiveCount(acquires) > MAX_COUNT)
            thrownew Error("Maximum lock count exceeded");
        
        /*
         * 修改标志位
         * 这里修改标志位为什么没有用CAS原子操作呢？
         * 因为到这里肯定是写锁重入了，写锁是独占锁，不会有其他线程来捣乱。
         */
        setState(c + acquires);
        return true;
    }
    
    /*
     * 到这里表示锁是没有被线程占用的，因为锁被线程占用的情况在上个if里处理并返回了
     * 所以这里直接检查AQS队列情况，没问题的话CAS修改标志位获取锁
     */
    if (writerShouldBlock() || // 检查AQS队列中的情况，看是当前线程是否可以获取写锁
        !compareAndSetState(c, c + acquires)) // 修改写锁标志位
        returnfalse;
    setExclusiveOwnerThread(current);// 获取写锁成功，将AQS.exclusiveOwnerThread置为当前线程
    return true;
}
```
先分析一下可以获取写锁的条件：

- 当前锁的状态，没有线程占用锁（读写锁都没被占用） 2）线程占用写锁时，线程再次来获取写锁，也就是重入

- AQS队列中的情况，如果是公平锁，同步队列中有线程等锁时，当前线程是不可以先获取锁的，必须到队列中排队。

- 写锁的标志位只有16位，最多重入2^16-1次。

### 写锁释放
```java
public void unlock() {
    sync.release(1);
}

/**
 * 释放写锁，如果释放之后没有线程占用写锁，唤醒队列中的线程来获取锁
 */
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);// 唤醒head的后继节点去获取锁
        returntrue;
    }
    return false;
}

/**
 * 释放写锁，修改写锁标志位和exclusiveOwnerThread
 * 如果这个写锁释放之后，没有线程占用写锁了，返回true
 */
protected final boolean tryRelease(int releases) {
    if (!isHeldExclusively())
        thrownew IllegalMonitorStateException();
    int nextc = getState() - releases;
    boolean free = exclusiveCount(nextc) == 0;
    if (free)
        setExclusiveOwnerThread(null);
    setState(nextc);
    return free;
}
```

## 锁降级

读写锁支持锁降级。锁降级就是写锁是可以降级为读锁的，但是需要遵循获取写锁、获取读锁、释放写锁的次序。

为什么要支持锁降级？
支持降级锁的情况：线程A持有写锁时，线程A要读取共享数据，线程A直接获取读锁读取数据就好了。

如果不支持锁降级会怎么样？

线程A持有写锁时，线程A要读取共享数据，但是线程A不能获取读锁，只能等待释放写锁。

当线程A释放写锁之后，线程A获取读锁要和其他线程抢锁，如果另一个线程B抢到了写锁，对数据进行了修改，那么线程B释放写锁之后，线程A才能获取读锁。线程B获取到读锁之后读取的数据就不是线程A修改的数据了，也就是脏数据。
tryAcquireShared()方法中，当前线程占用写锁时是可以获取读锁的，如下：
```java
protected final int tryAcquireShared(int unused) {
    Thread current = Thread.currentThread();
    int c = getState();
    /*
     * 根据锁的状态判断可以获取读锁的情况：
     * 1. 读锁写锁都没有被占用
     * 2. 只有读锁被占用
     * 3. 写锁被自己线程占用
     * 总结一下，只有在其它线程持有写锁时，不能获取读锁，其它情况都可以去获取。
     */
    if (exclusiveCount(c) != 0 && // 写锁被占用
        getExclusiveOwnerThread() != current) // 持有写锁的不是当前线程
        return -1;
	...
```

不支持锁升级

持有写锁的线程，去获取读锁的过程称为锁降级；持有读锁的线程，在没释放的情况下不能去获取写锁的过程称为锁升级。
 
读写锁是不支持锁升级的。
## 应用
读写锁多用于解决读多写少的问题，最典型的就是缓存问题。如下是官方给出的应用示例：
```java
class CachedData {
    Object data;
    volatile boolean cacheValid;
    // 读写锁实例
    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    void processCachedData() {
        // 获取读锁
        rwl.readLock().lock();
        if (!cacheValid) { // 如果缓存过期了，或者为 null
            // 释放掉读锁，然后获取写锁 (后面会看到，没释放掉读锁就获取写锁，会发生死锁情况)
            rwl.readLock().unlock();
            rwl.writeLock().lock();

            try {
                if (!cacheValid) { // 重新判断，因为在等待写锁的过程中，可能前面有其他写线程执行过了
                    data = ...
                    cacheValid = true;
                }
                // 获取读锁 (持有写锁的情况下，是允许获取读锁的，称为 “锁降级”，反之不行。)
                rwl.readLock().lock();
            } finally {
                // 释放写锁，此时还剩一个读锁
                rwl.writeLock().unlock(); // Unlock write, still hold read
            }
        }

        try {
            use(data);
        } finally {
            // 释放读锁
            rwl.readLock().unlock();
        }
    }
}
```