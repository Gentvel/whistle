---
title: 【JAVA】JVM 篇五 常用虚拟机参数
date: 2020-07-29
sidebar: auto
categories:
- java
tags:
- jvm
prev: ./garbage
next: false
---

## 一、调试参数

### 1.1 打印GC日志
`-XX:+PrintGC` :[GC 4000K -> 200K(15000K),0.11 secs]  
表示在GC前，堆空间使用量为4MB，GC后，堆空间用量200K，当前可用对空间为15MB，最后为本次GC花费的时间

如果需要打印更加详细的信息，则可以使用 `-XX:+PrintGCDetails`  
如果需要更全面的堆信息，则可以使用`-XX:+PrintHeapAtGC`  
如果需要系统内的引用，则可以使用`-XX:+PrintReferenceGC`

### 1.2 类加载/卸载
使用参数`-verbose:class`跟踪类的加载和卸载
使用参数`-XX:+TraceClassLoading`跟踪类加载
使用参数`-XX:+TraceClassUnloading`跟踪类卸载

### 1.3 JVM内存
`-Xmx`   Java Heap最大值，默认值为物理内存的1/4，最佳设值应该视物理内存大小及计算机内其他内存开销而定；

`-Xms`   Java Heap初始值，Server端JVM最好将-Xms和-Xmx设为相同值，开发测试机JVM可以保留默认值；

`-Xmn`   Java Heap Young区大小，不熟悉最好保留默认值；

`-Xss`   每个线程的Stack大小，不熟悉最好保留默认值；

### 1.4 JVM内存信息
```
Runtime.getRuntime().maxMemory(); //最大可用内存，对应-Xmx
Runtime.getRuntime().freeMemory(); //当前JVM空闲内存
Runtime.getRuntime().totalMemory(); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
```
关于maxMemory()，freeMemory()和totalMemory()：  

maxMemory()为JVM的最大可用内存，可通过-Xmx设置，默认值为物理内存的1/4，设值不能高于计算机物理内存；
totalMemory()为当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和，会随着JVM使用内存的增加而增加；
freeMemory()为当前JVM空闲内存，因为JVM只有在需要内存时才占用物理内存使用，所以freeMemory()的值一般情况下都很小，而 JVM实际可用内存并不等于freeMemory()，而应该等于maxMemory()-totalMemory()+freeMemory()。及其 设置JVM内存分配。

## 二、参数说明
其一是标准参数（-），所有的JVM实现都必须实现这些参数的功能，而且向后兼容；
其二是非标准参数（-X），默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；
其三是非Stable参数（-XX），此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用；

### 2.1 标准参数
```
-verbose:class 
 #输出jvm载入类的相关信息，当jvm报告说找不到类或者类冲突时可此进行诊断。
-verbose:gc 
 #输出每次GC的相关情况。
-verbose:jni 
 #输出native方法调用的相关情况，一般用于诊断jni调用错误信息。
```

### 2.2 非标准参数又称为扩展参数

```

-Xms512m  设置JVM促使内存为512m。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存。

-Xmx512m ，设置JVM最大可用内存为512M。

-Xmn200m：设置年轻代大小为200M。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小。持久代一般固定大小为64m，所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。

-Xss128k：

设置每个线程的堆栈大小。JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。更具应用的线程所需内存大小进行调整。在相同物理内 存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。

-Xloggc:file
 与-verbose:gc功能类似，只是将每次GC事件的相关情况记录到一个文件中，文件的位置最好在本地，以避免网络的潜在问题。
 若与verbose命令同时出现在命令行中，则以-Xloggc为准。
-Xprof
 跟踪正运行的程序，并将跟踪数据在标准输出输出；适合于开发环境调试。
```

### 2.3 非Stable参数
参数及其默认值|	描述
-|-
-XX:-DisableExplicitGC|	禁止调用System.gc()；但jvm的gc仍然有效
-XX:+MaxFDLimit	|最大化文件描述符的数量限制
-XX:+ScavengeBeforeFullGC	|新生代GC优先于Full GC执行
-XX:+UseGCOverheadLimit	|在抛出OOM之前限制jvm耗费在GC上的时间比例
-XX:-UseConcMarkSweepGC|	对老生代采用并发标记交换算法进行GC
-XX:-UseParallelGC	|启用并行GC
-XX:-UseParallelOldGC|	对Full GC启用并行，当-XX:-UseParallelGC启用时该项自动启用
-XX:-UseSerialGC|	启用串行GC
-XX:+UseThreadPriorities|	启用本地线程优先级

:::tip
串行（SerialGC）是jvm的默认GC方式，一般适用于小型应用和单处理器，算法比较简单，GC效率也较高，但可能会给应用带来停顿；  
并行（ParallelGC）是指GC运行时，对应用程序运行没有影响，GC和app两者的线程在并发执行，这样可以最大限度不影响app的运行；  
并发（ConcMarkSweepGC）是指多个线程并发执行GC，一般适用于多处理器系统中，可以提高GC的效率，但算法复杂，系统消耗较大；
:::

## 三、常用参数列表
### 3.1 性能调优参数列表

参数及其默认值|描述
-|-
-XX:LargePageSizeInBytes=4m|	设置用于Java堆的大页面尺寸
-XX:MaxHeapFreeRatio=70	|GC后java堆中空闲量占的最大比例
-XX:MaxNewSize=size	|新生成对象能占用内存的最大值
-XX:MaxPermSize=64m	|老生代对象能占用内存的最大值
-XX:MinHeapFreeRatio=40|	GC后java堆中空闲量占的最小比例
-XX:NewRatio=2	|新生代内存容量与老生代内存容量的比例
-XX:NewSize=2.125m|	新生代对象生成时占用内存的默认值
-XX:ReservedCodeCacheSize=32m	|保留代码占用的内存容量
-XX:ThreadStackSize=512|	设置线程栈大小，若为0则使用系统默认值
-XX:+UseLargePages|	使用大页面内存

### 3.2 调试参数列表
参数及其默认值|	描述
-|-
-XX:-CITime	|打印消耗在JIT编译的时间
-XX:ErrorFile=./hs_err_pid.log	|保存错误日志或者数据到文件中
-XX:-ExtendedDTraceProbes	|开启solaris特有的dtrace探针
-XX:HeapDumpPath=./java_pid.hprof	|指定导出堆信息时的路径或文件名
-XX:-HeapDumpOnOutOfMemoryError	|当首次遭遇OOM时导出此时堆中相关信息
-XX:	|出现致命ERROR之后运行自定义命令
-XX:OnOutOfMemoryError=	|当首次遭遇OOM时执行自定义命令
-XX:-PrintClassHistogram	|遇到Ctrl-Break后打印类实例的柱状信息，与jmap -histo功能相同
-XX:-PrintConcurrentLocks	|遇到Ctrl-Break后打印并发锁的相关信息，与jstack -l功能相同
-XX:-PrintCommandLineFlags	|打印在命令行中出现过的标记
-XX:-PrintCompilation	|当一个方法被编译时打印相关信息
-XX:-PrintGC	|每次GC时打印相关信息
-XX:-PrintGC Details	|每次GC时打印详细信息
-XX:-PrintGCTimeStamps|	打印每次GC的时间戳
-XX:-TraceClassLoading	|跟踪类的加载信息
-XX:-TraceClassLoadingPreorder|	跟踪被引用到的所有类的加载信息
-XX:-TraceClassResolution	|跟踪常量池
-XX:-TraceClassUnloading|	跟踪类的卸载信息
-XX:-TraceLoaderConstraints	|跟踪类加载器约束的相关信息

