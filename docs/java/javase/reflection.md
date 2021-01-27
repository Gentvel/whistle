---
title: 【Java基础】系列四 反射
date: 2020-08-15
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./generics
next: ./exception
---

## 一、反射机制介绍
java反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取信息和动态调用对象方法的功能称为java的反射机制。
## 二、获取Class对象的三种方式
### 2.1 已知具体类
```java
Class<?> clazz = User.class;
```
每个类都有一个静态的类属性
### 2.2 已知具体对象
```java
User user = new User();
user.getClass();
```
getClass方法是Object类中的本地方法，每个类都隐式继承Object类，所以对象实例都有这个方法
### 2.3 已知类名
```java
Class Clazz = Class.forName("com.demo.User");
```
根据类的全限定名来获取类对象。

在运行期间，一个类只有一个Class对象，存放在元空间（永久代）中。
三种方式常用第三种，第二种对象都有了还要反射干什么。第一种需要导入类的包，依赖太强，不导包就抛编译错误。一般都第三种，一个字符串可以传入也可写在配置文件中等多种方法。

## 三、通过反射获取类属性
首先构造测试类如下：
::: details 点击查看代码
```java
public class Student {
    //---------------构造方法-------------------
    //（默认的构造方法）
    Student(String str){
        System.out.println("(默认)的构造方法 s = " + str);
    }

    //无参构造方法
    public Student(){
        System.out.println("调用了公有、无参构造方法执行了。。。");
    }

    //有一个参数的构造方法
    public Student(char name){
        System.out.println("姓名：" + name);
    }

    //有多个参数的构造方法
    public Student(String name ,int age){
        System.out.println("姓名："+name+"年龄："+ age);//这的执行效率有问题，以后解决。
    }

    //受保护的构造方法
    protected Student(boolean n){
        System.out.println("受保护的构造方法 n = " + n);
    }

    //私有构造方法
    private Student(int age){
        System.out.println("私有的构造方法   年龄："+ age);
    }
    //**********字段*************//
    public String name;
    protected int age;
    char sex;
    private String phoneNum;
    
    @Override
    public String toString() {
      return "Student [name=" + name + ", age=" + age + ", sex=" + sex
          + ", phoneNum=" + phoneNum + "]";
    }
    //**************成员方法***************//
    public void show1(String s){
      System.out.println("调用了：公有的，String参数的show1(): s = " + s);
    }
    protected void show2(){
      System.out.println("调用了：受保护的，无参的show2()");
    }
    void show3(){
      System.out.println("调用了：默认的，无参的show3()");
    }
    private String show4(int age){
      System.out.println("调用了，私有的，并且有返回值的，int参数的show4(): age = " + age);
      return "abcd";
    }


}
```
:::
### 3.1获取构造方法
::: details 点击查看代码
```java
@Test
public void testReflectConstructor() {
    Class<?> aClass = null;
    try {
        aClass = Class.forName("com.demo.reflection.Student");
    } catch (ClassNotFoundException e) {
        System.err.println("Class Not Found");
    }
    System.out.println("---公有---");
    Constructor<?>[] constructors = aClass.getConstructors();
    for (Constructor<?> constructor : constructors) {
        System.out.println(constructor);
    }
    System.out.println("----所有----");
    Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
    for (Constructor<?> declaredConstructor : declaredConstructors) {
        System.out.println(declaredConstructor);
    }
    System.out.println("*****************获取公有、无参的构造方法*******************************");
    Constructor<?> con = null;
    try {
          con= aClass.getConstructor();
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    }
    System.out.println(con);
    //1>、这里需要的是一个参数的类型，切记是类型
    //2>、返回的是描述这个无参构造函数的类对象
    try {
        //取消安全检查
        con.setAccessible(true);
        Student instance = (Student)con.newInstance();
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
        e.printStackTrace();
    }
}
```
:::

### 3.2 获取属性
::: details 点击查看代码
```java
//1.获取Class对象
Class stuClass = Class.forName("fanshe.field.Student");
//2.获取字段
System.out.println("************获取所有公有的字段********************");
Field[] fieldArray = stuClass.getFields();
for(Field f : fieldArray){
  System.out.println(f);
}
System.out.println("************获取所有的字段(包括私有、受保护、默认的)********************");
fieldArray = stuClass.getDeclaredFields();
for(Field f : fieldArray){
  System.out.println(f);
}
System.out.println("*************获取公有字段**并调用***********************************");
Field f = stuClass.getField("name");
System.out.println(f);
//获取一个对象
Object obj = stuClass.getConstructor().newInstance();//产生Student对象--》Student stu = new Student();
//为字段设置值
f.set(obj, "刘德华");//为Student对象中的name属性赋值--》stu.name = "刘德华"
//验证
Student stu = (Student)obj;
System.out.println("验证姓名：" + stu.name);


System.out.println("**************获取私有字段****并调用********************************");
f = stuClass.getDeclaredField("phoneNum");
System.out.println(f);
f.setAccessible(true);//暴力反射，解除私有限定
f.set(obj, "18888889999");
System.out.println("验证电话：" + stu);
```
:::

### 3.3 获取方法
::: details 点击查看代码
```java
//1.获取Class对象
Class stuClass = Class.forName("fanshe.method.Student");
//2.获取所有公有方法
System.out.println("***************获取所有的”公有“方法*******************");
stuClass.getMethods();
Method[] methodArray = stuClass.getMethods();
for(Method m : methodArray){
  System.out.println(m);
}
System.out.println("***************获取所有的方法，包括私有的*******************");
methodArray = stuClass.getDeclaredMethods();
for(Method m : methodArray){
  System.out.println(m);
}
System.out.println("***************获取公有的show1()方法*******************");
Method m = stuClass.getMethod("show1", String.class);
System.out.println(m);
//实例化一个Student对象
Object obj = stuClass.getConstructor().newInstance();
m.invoke(obj, "刘德华");

System.out.println("***************获取私有的show4()方法******************");
m = stuClass.getDeclaredMethod("show4", int.class);
System.out.println(m);
m.setAccessible(true);//解除私有限定
Object result = m.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
System.out.println("返回值：" + result);
```
:::

`Object result = m.invoke(obj, 20);`需要两个参数，一个是要调用的对象（获取有反射），一个是实参，result是返回值

## 四、通过反射越过泛型检查
泛型用在编译期，编译过后类型擦除（消失掉）。所以是可以通过反射越过泛型检查的
::: details 点击查看代码
```java
ArrayList<String> strList = new ArrayList<>();
strList.add("aaa");
strList.add("bbb");

//	strList.add(100);
//获取ArrayList的Class对象，反向的调用add()方法，添加数据
Class listClass = strList.getClass(); //得到 strList 对象的字节码 对象
//获取add()方法
Method m = listClass.getMethod("add", Object.class);
//调用add()方法
m.invoke(strList, 100);

//遍历集合
for(Object obj : strList){
  System.out.println(obj);
}
```
:::

- [java反射总结](https://zhuanlan.zhihu.com/p/80519709)

参考  



## 五、反射机制优缺点
- 优点： 运行期类型的判断，动态加载类，提高代码灵活度。
- 缺点： 
1. 性能瓶颈：反射相当于一系列解释操作，通知 JVM 要做的事情，性能比直接的 java 代码要慢很多。
2. 安全问题，让我们可以动态操作改变类的属性同时也增加了类的安全隐患。