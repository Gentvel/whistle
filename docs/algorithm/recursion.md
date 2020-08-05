---
title: 【算法】 递归算法详解
date: 2020-08-04
sidebar: auto
categories:
 - algorithm
tags:
- recursion
prev: ./
next: false
---





### 二、递归的三大步骤

编写递归函数的步骤，可以分解为三个。

### 递归第一个步骤：明确函数要做什么

对于递归，一个最重要的事情就是要明确这个函数的功能。这个函数要完成一样什么样的事情，是完全由程序员来定义的，当写一个递归函数的时候，先不要管函数里面的代码是什么，而要先明确这个函数是实现什么功能的。

比如，定义了一个函数，这个函数是用来计算n的阶乘的。
```java
// 计算n的阶乘（假设n不为0）
int f(int n) {
    // 先不管内部实现逻辑
}
```
这样，就完成了第一个步骤：明确递归函数的功能。

### 递归第二个步骤：明确递归的结束（退出递归）条件

所谓递归，就是会在函数的内部逻辑代码中，调用这个函数本身。因此必须在函数内部明确递归的结束（退出）递归条件，否则函数会一直调用自己形成死循环。意思就是说，需要有一个条件（标识符参数）去引导递归结束，直接将结果返回。要注意的是，这个标识符参数需要是可以预见的，对于函数的执行返回结果也是可以预见的。

比如在上面的计算n的阶乘的函数中，当n=1的时候，肯定能知道f(n)对应的结果是1，因为1的阶乘就是1，那么我们就可以接着完善函数内部的逻辑代码，即将第二元素（递归结束条件）加进代码里面。
```java
// 计算n的阶乘（假设n不为0）
int f(int n) {
    if (n == 1) {
        return 1;
    }
}
```
当然了，当n=2的时候，也可以知道n的阶乘是2，那么也可以把n=2作为递归的结束条件。
```java
// 计算n的阶乘（假设n>=2）
int f(int n) {
    if (n == 2) {
        return 2;
    }
}
```
这里就可以看出，递归的结束条件并不局限，只要递归能正常结束，任何结束条件都是允许的，但是要注意一些逻辑上的细节。比如说上面的n==2的条件就需要n>2，否则当n=1的时候就会被漏掉，可能导致递归不能正常结束。完善一下就是当n<=2的时候，f(n)都会等于n。

```java
// 计算n的阶乘（假设n不为0）
int f(int n) {
    if (n <= 2) {
        return n;
    }
}
```
这样，就完成了第二步骤：明确递归的退出条件。
### 递归的第三个步骤：找到函数的等价关系式

递归的第三个步骤就是要不断地缩小参数的范围，缩小之后就可以通过一些辅助的变量或操作使原函数的结果不变。比如在上面的计算n的阶乘的函数中，要缩小f(n)的范围，就可以让f(n)=n* f(n-1)，这样范围就从n变成了n-1，范围变小了，直到范围抵达n<=2退出递归。并且为了维持原函数不变，我们需要让f(n-1)乘上n。说白了，就是要找到一个原函数的等价关系式。在这里，f(n)的等价关系式为n*f(n-1)，即f(n)=n*f(n-1)。

```java
// 计算n的阶乘（假设n不为0）
int f(int n) {
    if (n <= 2) {
        return n;
    }
    // 把n打出来看一下，你就能明白递归的原理了
    System.out.println(n);
    // 加入f(n)的等价操作逻辑
    return n * f(n - 1);
}
```
到这里f(n)的功能就基本实现了。每次写递归函数的时候，强迫自己跟着这三个步骤走，能达到事半功倍的效果。另外也可以看出，第三个步骤几乎是最难的一个步骤。