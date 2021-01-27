---
title: 【Java基础】系列五 异常
date: 2020-08-18
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./reflection
next: ./io
---

<center>

![exception](./img/exception.png)

</center>

## 一、异常与错误
在Java中，所有的异常都有一个共同的祖先java.lang包中的Throwable类。Throwable有两个重要的子类：Exception（异常）和Error(错误)，二者都是java异常处理的重要子类，各自都包含大量子类

Error(错误)：程序无法处理的错误，标识运行应用程序中比较严重的问题。多数错误与代码编写者执行的操作无关，而表示代码运行时 JVM（Java 虚拟机）出现的问题。例如，Java 虚拟机运行错误（Virtual MachineError），当 JVM 不再有继续执行操作所需的内存资源时，将出现 OutOfMemoryError。这些异常发生时，Java 虚拟机（JVM）一般会选择线程终止。

这些错误表示故障发生于虚拟机自身、或者发生在虚拟机试图执行应用时，如 Java 虚拟机运行错误（Virtual MachineError）、类定义错误（NoClassDefFoundError）等。这些错误是不可查的，因为它们在应用程序的控制和处理能力之 外，而且绝大多数是程序运行时不允许出现的状况。对于设计合理的应用程序来说，即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状况。在 Java 中，错误通过 Error 的子类描述。

Exception（异常）:是程序本身可以处理的异常。Exception 类有一个重要的子类 RuntimeException。RuntimeException 异常由 Java 虚拟机抛出。NullPointerException（要访问的变量没有引用任何对象时，抛出该异常）、ArithmeticException（算术运算异常，一个整数除以 0 时，抛出该异常）和 ArrayIndexOutOfBoundsException （下标越界异常）。

::: tip 异常和错误的区别
异常能被程序本身处理，错误是无法处理。
:::

## 二、try-catch-finally
- try 块： 用于捕获异常。其后可接零个或多个 catch 块，如果没有 catch 块，则必须跟一个 finally 块。
- catch 块： 用于处理 try 捕获到的异常。
- finally 块： 无论是否捕获或处理异常，finally 块里的语句都会被执行。当在 try 块或 catch 块中遇到 return 语句时，finally 语句块将在方法返回之前被执行。
在以下 4 种特殊情况下，finally 块不会被执行：

- 在 finally 语句块第一行发生了异常。 因为在其他行，finally 块还是会得到执行
- 在前面的代码中用了 System.exit(int)已退出程序。 exit 是带参函数 ；若该语句在异常语句之后，finally 会执行
- 程序所在的线程死亡。
- 关闭 CPU。

::: warning 注意
当 try 语句和 finally 语句中都有 return 语句时，在方法返回之前，finally 语句的内容将被执行，并且 finally 语句的返回值将会覆盖原始的返回值。
:::
如下：
:::details 点击查看
```java
public class Test {
    public static int f(int value) {
        try {
            return value * value;
        } finally {
            if (value == 2) {
                return 0;
            }
        }
    }
}
```
:::

## 三、try-with-resources 
- 适用范围（资源的定义）： 任何实现 `java.lang.AutoCloseable`或者`java.io.Closeable`的对象
- 关闭资源和final的执行顺序： 在 `try-with-resources` 语句中，任何 `catch` 或 `finally` 块在声明的资源关闭后运行

::: tip 《Effecitve Java》中明确指出
面对必须要关闭的资源，我们总是应该优先使用 `try-with-resources` 而不是`try-finally`。随之产生的代码更简短，更清晰，产生的异常对我们也更有用。`try-with-resources`语句让我们更容易编写必须要关闭的资源的代码，若采用`try-finally`则几乎做不到这点。
:::

当然多个资源需要关闭的时候，使用 try-with-resources 实现起来也非常简单，如果你还是用try-catch-finally可能会带来很多问题。

通过使用分号分隔，可以在try-with-resources块中声明多个资源。

::: details 
```java
try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File("test.txt")));
             BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File("out.txt")))) {
            int b;
            while ((b = bin.read()) != -1) {
                bout.write(b);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

```
:::