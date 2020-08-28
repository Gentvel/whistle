---
title: 【Java基础】系列九 注解
date: 2020-08-06
sidebar: auto
categories:
 - java
tags:
- javase
prev: false
next: false
---


## 一、元注解
元注解顾名思义我们可以理解为注解的注解，它是作用在注解中，方便我们使用注解实现想要的功能。元注解分别有@Retention、 @Target、 @Document、 @Inherited和@Repeatable（JDK1.8加入）五种。

### 1.1 @Retention
Retention英文意思有保留、保持的意思，它表示注解存在阶段是保留在源码（编译期），字节码（类加载）或者运行期（JVM中运行）。在@Retention注解中使用枚举RetentionPolicy来表示注解保留时期  
`@Retention(RetentionPolicy.SOURCE)`，注解仅存在于源码中，在class字节码文件中不包含  
`@Retention(RetentionPolicy.CLASS)`， 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得  
`@Retention(RetentionPolicy.RUNTIME)`， 注解会在class字节码文件中存在，在运行时可以通过反射获取到
如果我们是自定义注解，则通过前面分析，我们自定义注解如果只存着源码中或者字节码文件中就无法发挥作用，而在运行期间能获取到注解才能实现我们目的，所以自定义注解中肯定是使用 `@Retention(RetentionPolicy.RUNTIME)`
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTestAnnotation {

}
```

### 1.2 @Target
Target的英文意思是目标，这也很容易理解，使用@Target元注解表示我们的注解作用的范围就比较具体了，可以是类，方法，方法参数变量等，同样也是通过枚举类ElementType表达作用类型

`@Target(ElementType.TYPE)` 作用接口、类、枚举、注解  
`@Target(ElementType.FIELD)` 作用属性字段、枚举的常量  
`@Target(ElementType.METHOD)` 作用方法  
`@Target(ElementType.PARAMETER)` 作用方法参数  
`@Target(ElementType.CONSTRUCTOR)` 作用构造函数  
`@Target(ElementType.LOCAL_VARIABLE)`作用局部变量  
`@Target(ElementType.ANNOTATION_TYPE)` 作用于注解（@Retention注解中就使用该属性）  
`@Target(ElementType.PACKAGE)` 作用于包  
`@Target(ElementType.TYPE_PARAMETER)` 作用于类型泛型，即泛型方法、泛型类、泛型接口 （jdk1.8加入）  
`@Target(ElementType.TYPE_USE)` 类型使用.可以用于标注任意类型除了 class （jdk1.8加入）  
一般比较常用的是ElementType.TYPE类型
@Retention(RetentionPolicy.RUNTIME)

### 1.3 @Documented
Document的英文意思是文档。它的作用是能够将注解中的元素包含到 Javadoc 中去。
### 1.4 @Inherited
Inherited的英文意思是继承，但是这个继承和我们平时理解的继承大同小异，一个被@Inherited注解了的注解修饰了一个父类，如果他的子类没有被其他注解修饰，则它的子类也继承了父类的注解。
下面我们来看个@Inherited注解例子

```java
/**自定义注解*/
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyTestAnnotation {
}
/**父类标注自定义注解*/
@MyTestAnnotation
public class Father {
}
/**子类*/
public class Son extends Father {
}
/**测试子类获取父类自定义注解*/
public class test {
   public static void main(String[] args){

      //获取Son的class对象
       Class<Son> sonClass = Son.class;
      // 获取Son类上的注解MyTestAnnotation可以执行成功
      MyTestAnnotation annotation = sonClass.getAnnotation(MyTestAnnotation.class);
   }
}
```

### 1.5 @Repeatable
Repeatable的英文意思是可重复的。顾名思义说明被这个元注解修饰的注解可以同时作用一个对象多次，但是每次作用注解又可以代表不同的含义。
下面我们看一个人玩游戏的例子
```java
/**一个人喜欢玩游戏，他喜欢玩英雄联盟，绝地求生，极品飞车，尘埃4等，则我们需要定义一个人的注解，他属性代表喜欢玩游戏集合，一个游戏注解，游戏属性代表游戏名称*/
/**玩家注解*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface People {
    Game[] value() ;
}
/**游戏注解*/
@Repeatable(People.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Game {
    String value() default "";
}
/**玩游戏类*/
@Game(value = "LOL")
@Game(value = "PUBG")
@Game(value = "NFS")
@Game(value = "Dirt4")
public class PlayGame {
}  
```
通过上面的例子，你可能会有一个疑问，游戏注解中括号的变量是啥，其实这和游戏注解中定义的属性对应。接下来我们继续学习注解的属性。

## 二、注解的属性
解的属性其实和类中定义的变量有异曲同工之处，只是注解中的变量都是成员变量（属性），并且注解中是没有方法的，只有成员变量，变量名就是使用注解括号中对应的参数名，变量返回值注解括号中对应参数类型。相信这会你应该会对上面的例子有一个更深的认识。而@Repeatable注解中的变量则类型则是对应Annotation（接口）的泛型Class。
### 2.1 属性类型
注解属性类型可以有以下列出的类型
1. 基本数据类型
2. String
3. 枚举类型
4. 注解类型
5. Class类型
6. 以上类型的一维数组类型

### 2.2 注解成员变量赋值
如果注解又多个属性，则可以在注解括号中用“;”号隔开分别给对应的属性赋值，如下例子，注解在父类中赋值属性
```java
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyTestAnnotation {
    String name() default "mao";
    int age() default 18;
}

@MyTestAnnotation(name = "father",age = 50)
public class Father {
}
```

### 2.3 获取注解属性
如果获取注解属性主要有三个反射基本的方法
```java
 /**是否存在对应 Annotation 对象*/
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return GenericDeclaration.super.isAnnotationPresent(annotationClass);
    }

 /**获取 Annotation 对象*/
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        Objects.requireNonNull(annotationClass);

        return (A) annotationData().annotations.get(annotationClass);
    }
 /**获取所有 Annotation 对象数组*/   
 public Annotation[] getAnnotations() {
        return AnnotationParser.toArray(annotationData().annotations);
    }    
```