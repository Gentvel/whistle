---
title: 线性表
date: 2020-08-06
sidebar: auto
prev: false
next: false
---

## 线性结构说明
线性表是由n(n>=0)个类型相同的数据元素a0,a1,…,an-1组成的有限的序列，在数学中记作(a0,a1,…,an-1)，其中ai的数据类型可以是基本数据类型(int,float等)、字符或类。n代表线性表的元素个数，也称其为长度(Length)。若n=0，则为空表；若n > 0，则ai(0 < i < n-1)有且仅有一个前驱(Predecessor)元素ai-1和一个后继(Successor)元素ai+1，a0没有前驱元素，ai没有后继元素。


- [顺序表](./sequence)



## 结构定义
数据结构为了解决人与计算机之间的数据交互，这些操作无非就是增删改查，先定义一个接口规范线性结构
:::details 点击查看细节
```java
public interface List<T> {
    /**
     * List 大小
     */
    int size();

    /**
     * 是否为空
     */
    boolean isEmpty();
    /**
     * 增加
     */
    boolean add(T t);

    /**
     * 删除
     */
    T remove(int index);

    /**
     *修改
     */
    boolean set(int index,T t);

    /**
     * 查询
     */
    T get(int index);
}
```
:::