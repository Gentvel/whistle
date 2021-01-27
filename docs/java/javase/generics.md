---
title: 【Java基础】系列三 泛型
date: 2020-08-10
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./oop
next: ./reflection
---

## 一、简述
泛型(Generics) 是JDK5中引入的一个新特性，泛型提供了编译时类型安全检测机制，该机制允许程序员在编译时检测到非法的类型。泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。在Java SE 1.5之前，没有泛型的情况的下，通过对类型Object的引用来实现参数的“任意化”，“任意化”带来的缺点是要做显式的强制类型转换，而这种转换是要求开发者对实际参数类型可以预知的情况下进行的。泛型的好处是在编译的时候检查类型安全，并且所有的强制转换都是自动和隐式的，提高代码的重用率。泛型就是显示的告诉编译器容器里的类型。

::: warning 注意
java的泛型是伪泛型，因为在java编译期间，所有的泛型信息都会被擦除掉，也就是常说的类型擦除
:::

可参考 [Java泛型类型擦除以及类型擦除带来的问题](https://www.cnblogs.com/wuqinglong/p/9456193.html)

```java
List<Integer> list = new ArrayList<>();

list.add(12);
//这里直接添加会报错
list.add("a");
Class<? extends List> clazz = list.getClass();
Method add = clazz.getDeclaredMethod("add", Object.class);
//但是通过反射添加，是可以的
add.invoke(list, "kl");

System.out.println(list)
```
原始类型就是擦除去了泛型信息，最后在字节码中的类型变量的真正类型，无论何时定义一个泛型，相应的原始类型都会被自动提供，类型变量擦除，并使用其限定类型（无限定的变量用Object）替换。


## 二、泛型使用的三种方式

### 2.1 泛型类
```java
//此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
//在实例化泛型类时，必须指定T的具体类型
public class Generic<T>{ 
   
    private T key;

    public Generic(T key) { 
        this.key = key;
    }

    public T getKey(){ 
        return key;
    }
}
```
如何实例化泛型类：
```java
Generic<Integer> genericInteger = new Generic<Integer>(123456);
```

### 2.2 泛型接口
```java
public interface Generator<T> {
    public T method();
}
```

**实现泛型接口，不指定类型：**
```java
class GeneratorImpl<T> implements Generator<T>{
    @Override
    public T method() {
        return null;
    }
}
```

**实现泛型接口，指定类型：**
```java
class GeneratorImpl<T> implements Generator<String>{
    @Override
    public String method() {
        return "hello";
    }
}
```

### 2.3 泛型方法
```java
public static < E > void printArray( E[] inputArray )
{         
      for ( E element : inputArray ){        
        System.out.printf( "%s ", element );
      }
      System.out.println();
}
```

使用：

```java
// 创建不同类型数组： Integer, Double 和 Character
Integer[] intArray = { 1, 2, 3 };
String[] stringArray = { "Hello", "World" };
printArray( intArray  ); 
printArray( stringArray  ); 
```
:::danger 注意
泛型类中的静态方法和静态变量不可以使用泛型类所声明的泛型类型参数,因为泛型类中的泛型参数的实例化是在定义对象的时候指定的，而静态变量和静态方法不需要使用对象来调用。对象都没有创建，如何确定这个泛型参数是何种类型，所以当然是错误的。
:::

## 三、常用的通配符

- ？ 表示不确定的 java 类型
- T (type) 表示具体的一个java类型
- K V (key value) 分别代表java键值中的Key Value
- E (element) 代表Element

更多关于Java 泛型中的通配符可以查看这篇文章：[聊一聊-JAVA 泛型中的通配符 T，E，K，V，？](https://juejin.im/post/6844903917835419661)