---
title: 【多线程】 篇 CAS(Compare And Swap)
date: 2020-08-23
sidebar: auto
categories:
- java
tags:
- thread
prev: false
next: false
---

## 一、介绍
CAS(Compare And Swap) 比较交换。Doug lea 在同步组件中大量使用cas技术鬼斧神工地实现java多线程并发操作。整个AQS同步组件，Atomic原子类操作等等都是以CAS实现的。
CAS交换比较过程（V.A.B）
- V.一个内存地址存放的实际值。
- A.旧的预期值。
- B.即将更新的值。

**当且仅当预期值A和内存值V相同时，将内存值修改为B并返回true，否则什么都不做，并返回false。**

### 1.1 CAS VS synchronized
synchronized 线程获取锁是一种**悲观**策略，即假设每一次执行临界区代码都会产生冲突，所以当前线程获取到锁的之后会阻塞其他线程获取该锁。
CAS(无锁操作) 获取锁是一种**乐观**策略，假设所有线程访问共享资源的时候不会出现冲突，所以出现冲突时就不会阻塞其他线程的操作，而是重试当前操作直到没有冲突为止。


## 二、CAS原理
探究 CAS 原理，查看AtomicInteger 类结构
```java
public class AtomicInteger extends Number implements java.io.Serializable {
   /*
     * This class intended to be implemented using VarHandles, but there
     * are unresolved cyclic startup dependencies.
     */
    private static final Unsafe U = Unsafe.getUnsafe();
    private static final long VALUE
        = U.objectFieldOffset(AtomicInteger.class, "value");

    private volatile int value;
}
```


- Unsafe 是 CAS 的核心类，由于 Java 方法无法直接访问底层系统，需要通过本地（native）方法来访问，Unsafe 相当于一个后门，基于该类可以直接操作特定内存的数据。

- 变量 valueOffset 表示该变量值在内存中的偏移地址，因为 Unsafe 就是根据内存偏移地址获取数据的原值。

- 变量 value 用 volatile 修饰，保证了多线程之间的内存可见性。

```java
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}

/**
    * Atomically adds the given value to the current value of a field
    * or array element within the given object {@code o}
    * at the given {@code offset}.
    *
    * @param o object/array to update the field/element in
    * @param offset field/element offset
    * @param delta the value to add
    * @return the previous value
    * @since 1.8
    */
@HotSpotIntrinsicCandidate
public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
        v = getIntVolatile(o, offset);
    } while (!weakCompareAndSetInt(o, offset, v, v + delta));
    return v;
}
```

`v = getIntVolatile(o, offset)`：根据对象 o 和对象中该变量地址 offset，获取变量的值 v。

`this.compareAndSwapInt(o, offset, v, v + delta);`

根据对象 o 和对象中该变量地址 offset 获取变量当前的值 v
比较 v 跟 内存中的oldValue，如果 vv==oldValue，则 v=v + delta 并返回 true。这步操作就是比较和替换操作，是原子性的。
如果 v!=oldValue，则返回 false，再去自旋循环到下一次调用 weakCompareAndSetInt 方法。
可见，getAndIncrement()的原子性是通过 weakCompareAndSetInt()中的第二步比较和替换保证的，那么 compareAndSwapInt()又是怎么保证原子性的呢？

compareAndSwapInt 方法是 JNI（Java Native InterfaceJAVA 本地调用），java 通过 C 来调用 CPU 底层指令实现的。

compareAndSwapInt 方法中的比较替换操作之前插入一个 lock 前缀指令，这个指令能过确保后续操作的原子性。

lock 前缀指令确保后续指令执行的原子性：

在Pentium及之前的处理器中，带有lock前缀的指令在执行期间会锁住总线，使得其它处理器暂时无法通过总线访问内存，很显然，这个开销很大。
在新的处理器中，Intel使用缓存锁定来保证指令执行的原子性，缓存锁定将大大降低lock前缀指令的执行开销。
CPU 提供了两种方法来实现多处理器的原子操作：总线加锁和缓存加锁。

- 总线加锁：总线加锁就是就是使用处理器提供的一个 LOCK#信号，当一个处理器在总线上输出此信号时，其他处理器的请求将被阻塞住，那么该处理器可以独占使用共享内存。但是这种处理方式显得有点儿霸道，不厚道，他把 CPU 和内存之间的通信锁住了，在锁定期间，其他处理器都不能其他内存地址的数据，其开销有点儿大。所以就有了缓存加锁。

- 缓存加锁：其实针对于上面那种情况我们只需要保证在同一时刻对某个内存地址的操作是原子性的即可。缓存加锁就是缓存在内存区域的数据如果在加锁期间，当它执行锁操作写回内存时，处理器不在输出 LOCK#信号，而是修改内部的内存地址，利用缓存一致性协议来保证原子性。缓存一致性机制可以保证同一个内存区域的数据仅能被一个处理器修改，也就是说当 CPU1 修改缓存行中的 i 时使用缓存锁定，那么 CPU2 就不能同时缓存了 i 的缓存行。

**比较替换操作过程分析**

1. 假设线程 A 和线程 B 同时调用 a.getAndIncrement()-->getAndIncrement()-->getAndAddInt()，AtomicInteger 里面的 value 原始值为 3，即主内存中 AtomicInteger 的 value 为 3，且线程 A 和线程 B 各自持有一份 value 的副本，值为 3。

2. 线程 A 通过 getIntVolatile(var1, var2)拿到 value 值 3，线程 B 也通过 getIntVolatile(var1, var2)拿到 value 值 3，线程 A 和线程 B 同时调用 compareAndSwapInt()。

3. 线程 A 执行 compareAndSwapInt()方法比较和替换时，其他 CPU 无法访问该变量的内存，所以线程 B 不能进行比较替换。线程 A 成功修改内存值为 4，返回 true，执行结束。

4. 线程 B 恢复，执行 compareAndSwapInt()方法比较和替换，发现内存的实际值 4 跟自己期望值 3 不一致，说明该值已经被其它线程提前修改过了，返回 false，自旋进入 while 循环，再通过 getIntVolatile(var1, var2)方法获取 value 值 4，执行 compareAndSwapInt()比较替换，直到成功。

## 三、CAS问题

### 3.1 ABA 问题
CAS 需要检查操作值有没有发生改变，如果没有发生改变则更新。但是存在这样一种情况：如果一个值原来是 A，变成了 B，然后又变成了 A，那么在 CAS 检查的时候会认为没有改变，但是实质上它已经发生了改变，这就是 ABA 问题。

解决方案可以沿袭数据库中常用的乐观锁方式，添加一个版本号可以解决。原来的变化路径 A->B->A 就变成了 1A->2B->3A。

在 java 1.5 后的 atomic 包中提供了 AtomicStampedReference 来解决 ABA 问题，解决思路就是这样的。

### 3.2 自旋时间过长
使用 CAS 时非阻塞同步，也就是说不会将线程挂起，会自旋（无非就是一个死循环）进行下一次尝试，如果自旋 CAS 长时间地不成功，则会给 CPU 带来非常大的开销。

优化：限制 CAS 自旋的次数，例如 BlockingQueue 的 SynchronousQueue。

### 3.3 只能保证一个共享变量的原子操作
当对一个共享变量执行操作时 CAS 能保证其原子性，如果对多个共享变量进行操作，CAS 就不能保证其原子性。

解决方案：把多个变量整成一个变量

- 利用对象整合多个共享变量，即一个类中的成员变量就是这几个共享变量，然后将这个对象做 CAS 操作就可以保证其原子性。atomic 中提供了 AtomicReference 来保证引用对象之间的原子性。

- 利用变量的高低位，如 JDK 读写锁 ReentrantReadWriteLock 的 state，高 16 位用于共享模式 ReadLock，低 16 位用于独占模式 WriteLock。