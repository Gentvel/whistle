---
title: 【JAVA】JVM 篇三 垃圾回收
date: 2020-07-29
sidebar: auto
categories:
- java
tags:
- jvm
prev: ./runtime
next: ./parameter
---

## 一、垃圾回收算法

### 1.1 引用计数法(Reference Counting)
对于一个对象A，只要有任何对象引用了A对象，那么A对象的计数就加一，当引用失效时，A对象的计数就减一。如果对象A的引用计数器的值为0时，那么对象就不可能再被使用。  
存在的问题
1. 无法处理循环引用的情况。因此，在JVM中的垃圾回收器没有使用这种算法。
2. 引用计数器每次产生引用或者消除的时候，都要作增加或减少的操作，对系统性能有一定的影响。

### 1.2 标记清除法(Mark-Sweep)
将垃圾回收分为两个阶段：标记阶段和清除阶段。
在标记阶段，首先通过根节点，标记所有从根节点开始的可达对象。因此，未标记的对象就是未被引用的垃圾对象。
然后执行清除阶段将垃圾对象回收。
标记清除法可能产生的最大问题就是空间碎片，空间不连续的问题。

### 1.3 复制算法(Copying)
将原有的内存空间分为两块，每次只是使用其中一块，在垃圾回收时，将正在使用的内存中的存活对象复制到未使用的内存块中，之后，清除正在使用的内存块中的所有对象，交换两个内存的角色，完成垃圾回收。这种算法应用于新生代中的ServivorFrom和ServivorTo这两个区中
如果系统中的垃圾对象很多，复制算法需要复制的存活对象数量就会相对比较少。因此，在真正需要垃圾回收的时候，复制算法的效率是很高的。又由于对象是在垃圾回收过程中，统一复制到另一片新的内存空间中，所以回收后的内存空间是没有碎片的。

### 1.4 标记压缩法(Mark-Compact)
复制算法的高效性是建立在存活对象少、垃圾对象多的前提下。而在老年代中，更多的是大部分对象都是存活的对象。不适用复制算法。
标记压缩法是一种老年代的回收算法。它在标记清除法的基础上做出了一些优化。和标记清除法一样，标记压缩法也是从根节点开始，对所有的可达对象进行标记动作。但之后的动作不是简单的清除垃圾对象，而是将所有存活的对象压缩到内存的一端。之后，清理边界外的所有的空间。这种方法既避免了碎片的产生，又不需要相同的两块内存空间。

### 1.5 分代算法(Generational Collecting)
根据内存根据对象的特点和不同堆区的特性，使用不同的回收算法，提高垃圾回收的效率

<center>

![分代算法](./img/gen.png)

</center>

虚拟机可能存在一种叫做卡表(Card Table)的数据结构。卡表作为一个比特位的集合，每个比特位可以用来表示老年代某一区域的所有对象是否持有新生代对象的引用。这样在新生代GC时，可以不用花大量时间扫描老年代对象来确定每一个对象的引用关系，而可以先扫描卡表，只有当卡表的标记位为1时，才需要扫描给定区域的老年代对象，而卡表位为0的所在区域的老年对象，一定不含有新生代对象的应用。

### 1.6 分区算法 (Region)
分代算法按照对象的生命周期长短分为两个部分，分区算法将整个堆空间划分为连续的不同小区间，每个小区间都独立使用、独立回收。这种算法的好处是可以控制一次回收多少个小区间。
一般来说，在相同条件下，堆空间越大，一次GC的时间就越长，从而产生的停顿也就越长。为了控制GC产生的停顿时间，将一块大的内存空间分割成为多个小块，根据目标的停顿时间，每次合理地回收若干个小区间，而不是整个堆空间，从而减少一次GC所产生的停顿。

## 二、判断可触及性
- **可触及的**:从根节点开始，可以到达这个对象。
- **可复活的**：对象的所有应用都被释放，但是对象有可能在`finalize()`函数中复活
- **不可触及的**：对象的`finalize()`函数被调用，并且没有复活，那么就进入不可触及状态，不可触及的对象不可能被复活，因为`finalize()`函数只会被调用一次。
```
    public static CanReliveObj obj;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("CanReliveObj finalize Called");
        obj=this;
    }

    @Override
    public String toString() {
        return "I am CanReliveObj";
    }

    public static void main(String[] args) throws InterruptedException {
        obj = new CanReliveObj();
        obj=null;
        System.gc();
        Thread.sleep(1000);
        if(obj==null){
            System.out.println("obj is null");
        }else{
            System.out.println("obj is usable");
        }
        System.out.println(" Second Gc");
        obj = null;
        System.gc();
        Thread.sleep(1000);
        if(obj==null){
            System.out.println("obj is null");
        }else{
            System.out.println("obj is usable");
        }
    }
```

输出
```
CanReliveObj finalize Called
obj is usable
 Second Gc
obj is null
```

可以看到在第一次设置obj为null时，进行GC，对象被复活了，再次释放对象并进行GC后发现obj对象才真正被回收。  
这是因为在第一次GC时，在finalize()函数调用之前，虽然系统中的应用被清除，但是作为实例方法finalize()，对象的this引用依然会被传入方法内部，引用外泄，对象就会复活，此时，对象又变为不可触及状态。而finalize()函数只会被调用一次，因此，在第二次清除对象时，对象就无法再复活，会被回收。

:::warning
不推荐使用finalize()函数释放资源，因为使用该函数可能发送引用外泄，在无意中复活对象；并且该函数是由系统调用的，调用时间是不明确的，最好使用try-catch-finally或try-resource释放资源
:::

## 三、引用
java中提供了四个级别的引用：强引用、软引用、弱引用和虚引用。
除了强引用外，其他3种引用均可以在`java.lang.ref`包中找到它们的身影。
### 3.1 强引用
new的对象就是强引用，强引用具备以下特点：
- 强引用可以直接访问目标对象。
- 强引用所指向的对象在任何时候都不会被系统回收，虚拟机宁愿抛出OOM异常，也不会回收强引用所指向对象。
- 强引用可能导致内存泄漏。

### 3.2 软引用-可被回收的引用
一个对象只持有软引用，那么当堆空间不足时，就会被回收。软引用使用`java.lang.ref.SoftReference`类实现。