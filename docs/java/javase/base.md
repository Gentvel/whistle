---
title: 【Java基础】系列一 数据类型与关键字
date: 2020-08-05
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./
next: ./oop
---

## 一、基本数据类型
基本数据类型有:

类型|位数
-|-
byte|8
short|16
int|32
long|64
float|32
double|64
char|16
boolean|~

boolean只有两个值：true和false

基本类型都有对应的包装类，基本类型与对应的包装类型之间的赋值使用自动装箱与拆箱完成
::: details 点击查看代码
```java
Integer x = 2; //装箱 调用了Integer.valueOf(2)
int y = x; //拆箱 调用了x.intValue()
```
:::

[Autoboxing and Unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)

### 基本类型缓存池
`new Integer(100)`和`Integer.valueOf(100)`的区别在于：
- `new Integer(100)`每次都会创建一个新的对象
- `Integer.valueOf(100)` 会使用缓存池中的对象，多次调用会取得同一个对象的引用



::: details 点击查看Integer.valueOf()代码
```java
@HotSpotIntrinsicCandidate
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```
:::
```java
Integer x = new Integer(123); //此构造方法已被弃用
Integer y = new Integer(123);
System.out.println(x == y);    // false
Integer z = Integer.valueOf(123);
Integer k = Integer.valueOf(123);
System.out.println(z == k);   // true
```
将上述代码改成
```java
Integer x = new Integer(123); //此构造方法已被弃用
Integer y = new Integer(123);
System.out.println(x == y);    // false
Integer z = Integer.valueOf(200);
Integer k = Integer.valueOf(200);
System.out.println(z == k);   // false
```

第二句输出就会变成false，这是由于在Java 8以后，Integer缓存池大小默认为-128~127
::: details 点击查看代码
```java
Integer x = 2; //装箱 调用了Integer.valueOf(2)
int y = x; //拆箱 调用了x.intValue()
```
:::

- [Autoboxing and Unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)
- [深入解析装箱和拆箱](https://www.cnblogs.com/dolphin0520/p/3780005.html)

`new Integer(100)`和`Integer.valueOf(100)`的区别在于：
- `new Integer(100)`每次都会创建一个新的对象
- `Integer.valueOf(100)` 会使用缓存池中的对象，多次调用会取得同一个对象的引用

::: details 点击查看代码
``` java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue =
            VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            try {
                int i = parseInt(integerCacheHighPropValue);
                i = Math.max(i, 127);
                // Maximum array size is Integer.MAX_VALUE
                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
            } catch( NumberFormatException nfe) {
                // If the property cannot be parsed into an int, ignore it.
            }
        }
        high = h;

        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }
    private IntegerCache() {}
}
```
:::

编译器会在自动装箱的过程中调用valueOf()方法，因此多个值相同且值在缓存池范围内的Integer实例使用自动装箱来创建，那么就会引用相同的对象
::: warning 注意
范围在-128~127之间
:::
在 jdk 1.8 所有的数值类缓冲池中，Integer 的缓冲池 IntegerCache 很特殊，这个缓冲池的下界是 - 128，上界默认是 127，但是这个上界是可调的，在启动 jvm 的时候，通过 -XX:AutoBoxCacheMax=size 来指定这个缓冲池的大小，该选项在 JVM 初始化的时候会设定一个名为 java.lang.IntegerCache.high 系统属性，然后 IntegerCache 初始化的时候就会读取该系统属性来决定上界。

[StackOverflow : Differences between new Integer(123), Integer.valueOf(123) and just 123](https://stackoverflow.com/questions/9030817/differences-between-new-integer123-integer-valueof123-and-just-123)

```java

Integer integer = new Integer(100);
Integer integer1 = new Integer(100);
System.out.println(integer==integer1);//false

int integer2 = 200;
int integer3 = 200;
System.out.println(integer2==integer3);//true

Integer integer4 = 200;
Integer integer5 = 200;
System.out.println(integer4==integer5);//false

Integer integer6 = Integer.valueOf(123);
Integer integer7 = Integer.valueOf(123);
System.out.println(integer6 == integer7);// true

Integer integer8 = Integer.valueOf(153);
Integer integer9 = Integer.valueOf(153);
System.out.println(integer8 == integer9);// false

```
试着解释以上代码

## 二、String
String 被声明为 final，因此它不可被继承。(Integer 等包装类也不能被继承）  
在 Java 8 中，String 内部使用 char 数组存储数据。
::: details 点击查看代码
```java
public final class String
  implements java.io.Serializable, Comparable<String>, CharSequence {
  /** The value is used for character storage. */
  private final char value[];
}
```
:::
在 Java 9 之后，String 类的实现改用 byte 数组存储字符串，同时使用 coder 来标识使用了哪种编码。
::: details 点击查看代码
```java
public final class String
  implements java.io.Serializable, Comparable<String>, CharSequence {
  /** The value is used for character storage. */
  private final byte[] value;

  /** The identifier of the encoding used to encode the bytes in {@code value}. */
  private final byte coder;
}
```
:::
value 数组被声明为 final，这意味着 value 数组初始化之后就不能再引用其它数组。并且 String 内部没有改变 value 数组的方法，因此可以保证 String 不可变。

### 不可变的好处
- 可以缓存hash值  
因为String的hash值经常被使用，例如String如果用做HashMap的key。不可变的特性可以使得hash值也不可变，因此只需要进行一次计算
- String Pool的需要 
如果一个String对象已经被创建了，那么就会从StringPool(运行时常量池)中取得其引用。只有String是不可变的时候，取得的引用才不会冲突，即能使用String Pool

- 安全性  
String经常被作为参数，String不可变性可以保证参数不可变。例如在作为网络连接参数的情况下如果String可变，那么在网络连接过程中，String经常被改变，改变String的一方以为现在连接的是其他主机，而实际情况可能不是。

- 线程安全  
String 不可变性天生具备线程安全，可以在多个线程中安全地使用
[Program Creek : Why String is immutable in Java?](https://www.programcreek.com/2013/04/why-string-is-immutable-in-java/)

::: warning 注意
1. **可变性**

- String 不可变
- StringBuffer 和 StringBuilder 可变

2. **线程安全**

- String 不可变，因此是线程安全的
- StringBuilder 不是线程安全的
- StringBuffer 是线程安全的，内部使用 synchronized 进行同步

:::

### 常量池
字符串常量池（String Pool）保存着所有字符串字面量（literal strings），这些字面量在编译时期就确定。不仅如此，还可以使用 String 的 intern() 方法在运行过程将字符串添加到 String Pool 中。

当一个字符串调用 intern() 方法时，如果 String Pool 中已经存在一个字符串和该字符串值相等（使用 equals() 方法进行确定），那么就会返回 String Pool 中字符串的引用；否则，就会在 String Pool 中添加一个新的字符串，并返回这个新字符串的引用。

下面示例中，s1 和 s2 采用 new String() 的方式新建了两个不同字符串，而 s3 和 s4 是通过 s1.intern() 方法取得同一个字符串引用。intern() 首先把 s1 引用的字符串放到 String Pool 中，然后返回这个字符串引用。因此 s3 和 s4 引用的是同一个字符串。

```java
String s1 = new String("aaa");
String s2 = new String("aaa");
System.out.println(s1 == s2);           // false
String s3 = s1.intern();
String s4 = s1.intern();
System.out.println(s3 == s4);           // true
```
如果是采用 "bbb" 这种字面量的形式创建字符串，会自动地将字符串放入 String Pool 中。
```java
String s5 = "bbb";
String s6 = "bbb";
System.out.println(s5 == s6);  // true
```
在 Java 7 之前，String Pool 被放在运行时常量池中，它属于[永久代](../jvm/runtime#_4-3-永久代)。而在 Java 7，String Pool 被移到堆中。这是因为永久代的空间有限，在大量使用字符串的场景下会导致 OutOfMemoryError 错误。

### new String("abc")
使用这种方式一共会创建两个字符串对象（前提是 String Pool 中还没有 "abc" 字符串对象）。
- "abc" 属于字符串字面量，因此编译时期会在 String Pool 中创建一个字符串对象，指向这个 "abc" 字符串字面量；
- 而使用 new 的方式会在堆中创建一个字符串对象。

## 三、运算
### 3.1 参数传递
Java 的参数是以值传递的形式传入方法中，而不是引用传递。

以下代码中 Dog dog 的 dog 是一个指针，存储的是对象的地址。在将一个参数传入一个方法时，本质上是将对象的地址以值的方式传递到形参中。
```java
public class Dog {

    String name;

    Dog(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getObjectAddress() {
        return super.toString();
    }
}
```
在方法中改变对象的字段值会改变原对象该字段值，因为引用的是同一个对象。

```java
class PassByValueExample {
    public static void main(String[] args) {
        Dog dog = new Dog("A");
        func(dog);
        System.out.println(dog.getName());          // B
    }

    private static void func(Dog dog) {
        dog.setName("B");
    }
}
```
但是在方法中将指针引用了其它对象，那么此时方法里和方法外的两个指针指向了不同的对象，在一个指针改变其所指向对象的内容对另一个指针所指向的对象没有影响。

```java
public class PassByValueExample {
    public static void main(String[] args) {
        Dog dog = new Dog("A");
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        func(dog);
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        System.out.println(dog.getName());          // A
    }

    private static void func(Dog dog) {
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        dog = new Dog("B");
        System.out.println(dog.getObjectAddress()); // Dog@74a14482
        System.out.println(dog.getName());          // B
    }
}
```
也即是说，值传递的情况下如果调用的方法改变对象的引用不会影响到当前方法的对象

:::tip 什么是值传递和引用传递
- 值传递 在调用函数时将实际参数复制一份传递到函数作为形参使用，在被调用函数中如果对参数进行修改，不会影响到实际参数
- 引用传递 在调用函数时将实际参数地址直接传递到函数中，那么在函数中对参数所进行的修改将影响到实际参数
:::
[Java 到底是值传递还是引用传递？](https://www.zhihu.com/question/31203609/answer/576030121)
### 3.2 float与double
Java 不能隐式执行向下转型，因为这会使得精度降低。
1.1 字面量属于 double 类型，不能直接将 1.1 直接赋值给 float 变量，因为这是向下转型。
```java
// float f = 1.1;
```
1.1f 字面量才是 float 类型。
```java
float f = 1.1f;
```
::: tip 向上转型与向下转型
- 向上转型 : 通过子类对象(小范围)实例化父类对象(大范围),这种属于自动转换
- 向下转型 : 通过父类对象(大范围)实例化子类对象(小范围),这种属于强制转换
:::

### 3.3 隐式类型转换
因为字面量 1 是 int 类型，它比 short 类型精度要高，因此不能隐式地将 int 类型向下转型为 short 类型。
```java
short s1 = 1;
// s1 = s1 + 1;
```
但是使用 += 或者 ++ 运算符会执行隐式类型转换。
```java
s1 += 1;
s1++;
```
上面的语句相当于将 s1 + 1 的计算结果进行了向下转型：
```java
s1 = (short) (s1 + 1);
```
### switch
从 Java 7 开始，可以在 switch 条件判断语句中使用 String 对象。
```java
String s = "a";
switch (s) {
    case "a":
        System.out.println("aaa");
        break;
    case "b":
        System.out.println("bbb");
        break;
}
```
switch 不支持 long，是因为 switch 的设计初衷是对那些只有少数几个值的类型进行等值判断，如果值过于复杂，那么还是用 if 比较合适。
```java
// long x = 111;
// switch (x) { // Incompatible types. Found: 'long', required: 'char, byte, short, int, Character, Byte, Short, Integer, String, or an enum'
//     case 111:
//         System.out.println(111);
//         break;
//     case 222:
//         System.out.println(222);
//         break;
// }
```
## 四、关键字
### 4.1 final

1. 数据

声明数据为常量，可以是编译时常量，也可以是在运行时被初始化后不能被改变的常量。

- 对于基本类型，final 使数值不变；
- 对于引用类型，final 使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的。
```java
final int x = 1;
// x = 2;  // cannot assign value to final variable 'x'
final A y = new A();
y.a = 1;
```
2. 方法

声明方法不能被子类重写。

private 方法隐式地被指定为 final，如果在子类中定义的方法和基类中的一个 private 方法签名相同，此时子类的方法不是重写基类方法，而是在子类中定义了一个新的方法。

3. 类

声明类不允许被继承。
### 4.2 static

1. 静态变量

- 静态变量：又称为类变量，也就是说这个变量属于类的，类所有的实例都共享静态变量，可以直接通过类名来访问它。静态变量在内存中只存在一份。
- 实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。
```java
public class A {

    private int x;         // 实例变量
    private static int y;  // 静态变量

    public static void main(String[] args) {
        // int x = A.x;  // Non-static field 'x' cannot be referenced from a static context
        A a = new A();
        int x = a.x;
        int y = A.y;
    }
}
```
2. 静态方法

静态方法在类加载的时候就存在了，它不依赖于任何实例。所以静态方法必须有实现，也就是说它不能是抽象方法。
```java
public abstract class A {
    public static void func1(){
    }
    // public abstract static void func2();  // Illegal combination of modifiers: 'abstract' and 'static'
}
```
只能访问所属类的静态字段和静态方法，方法中不能有 this 和 super 关键字，因此这两个关键字与具体对象关联。
```java
public class A {

    private static int x;
    private int y;

    public static void func1(){
        int a = x;
        // int b = y;  // Non-static field 'y' cannot be referenced from a static context
        // int b = this.y;     // 'A.this' cannot be referenced from a static context
    }
}
```
3. 静态语句块

静态语句块在类初始化时运行一次。
```java
public class A {
    static {
        System.out.println("123");
    }

    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new A();
    }
}
```

4. 静态内部类

非静态内部类依赖于外部类的实例，也就是说需要先创建外部类实例，才能用这个实例去创建非静态内部类。而静态内部类不需要。
```java
public class OuterClass {

    class InnerClass {
    }

    static class StaticInnerClass {
    }

    public static void main(String[] args) {
        // InnerClass innerClass = new InnerClass(); // 'OuterClass.this' cannot be referenced from a static context
        OuterClass outerClass = new OuterClass();
        InnerClass innerClass = outerClass.new InnerClass();
        StaticInnerClass staticInnerClass = new StaticInnerClass();
    }
}
```
静态内部类不能访问外部类的非静态的变量和方法。

5. 静态导包

在使用静态变量和方法时不用再指明 ClassName，从而简化代码，但可读性大大降低。
```java
import static com.xxx.ClassName.*
```
6. 初始化顺序

静态变量和静态语句块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序。
```java
public static String staticField = "静态变量";

static {
    System.out.println("静态语句块");
}

public String field = "实例变量";

{
    System.out.println("普通语句块");
}
```
最后才是构造函数的初始化。
```java
public InitialOrderTest() {
    System.out.println("构造函数");
}
```
存在继承的情况下，初始化顺序为：

- 父类（静态变量、静态语句块）
- 子类（静态变量、静态语句块）
- 父类（实例变量、普通语句块）
- 父类（构造函数）
- 子类（实例变量、普通语句块）
- 子类（构造函数）

[关键字总结](https://snailclimb.gitee.io/javaguide/#/docs/java/basic/final,static,this,super)


### 4.3 == equal()和hashCode()
[Java hashCode() 和 equals()的若干问题解答](https://www.cnblogs.com/skywang12345/p/3324958.html)