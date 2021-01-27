---
title: 【Java基础】系列七 序列化
date: 2020-08-23
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./io
next: ./annotation
---

## 一、序列化的含义、意义及使用场景
序列化：将对象写入IO流
反序列化：从IO流中恢复对象
意义：序列化机制允许将实现序列化的java对象转换位字节序列，这些字节序列可以保存在磁盘上，或通过网络传输，以达到以后恢复成原来的对象。序列化机制使得对象可以脱离程序的运行而独立存在。
使用场景：所有可在网络上传输的对象都必须是序列化的，传入的参数或返回的对象都是可序列化的，否则会出错；所有需要保存到磁盘的java对象都必须是可序列化的。
## 二、序列化的实现方式
如果需要将某个对象保存到磁盘上或者通过网络传输，那么这个类应该实现Serializable接口或者Externalizable接口之一。
### 2.1 Serializable
Serializable接口是一个标记接口，不用实现任何方法。一旦实现了此接口，该类的对象就是可序列化的。
序列化：
:::details 展开
```java
package com.demo.serilizable;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Person
 * <p>Description:</p>
 *
 * @author: Gentvel
 * @since: 1.0
 * @see: Person
 */
public class Person implements Serializable {
    private int age;
    private String name;
    //不提供无参构造函数
    public Person(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.println("构造函数被调用");
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/test.txt"))){
            Person person = new Person();
            person.setAge(12);
            person.setName("张三");
            oos.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```
使用上面序列化的对象反序列化
```java
try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:/test.txt"))){

    Person o = (Person)ois.readObject();
    System.out.println(o);
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();
}
```
:::
:::tip 注意
使用transient修饰的属性，java序列化时，会忽略掉此字段，所以反序列化出的对象，被transient修饰的属性是默认值。对于引用类型，值是null；基本类型，值是0；boolean类型，值是false。
:::

### 2.2 重写writeObject和readobject方法
使用transient虽然简单，但将此属性完全隔离在序列化之外，java提供了可选的自定义序列化。可以进行控制序列化的方式，或者对序列化数据进行编码加密等。
```java
private void writeObject(java.io.ObjectOutputStream out) throws IOException；
private void readObject(java.io.ObjectIutputStream in) throws IOException,ClassNotFoundException;
private void readObjectNoData() throws ObjectStreamException;
```
通过重写以上方法，可以选择那些属性需要序列化，那些属性不需要。如果writeObject使用种规则序列化，则相应的readObject需要相反的规则反序列化，以便能正确反序列化出对象。

```java
private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.writeObject(this.name);
    //oos.writeInt(this.age);
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    this.name = (String)ois.readObject();
}
```

### 2.3 更彻底的自定义序列化
```java
ANY-ACCESS-MODIFIER Object writeReplace() throws ObjectStreamException;
ANY-ACCESS-MODIFIER Object readResolve() throws ObjectStreamException;
```

- writeReplace：在序列化时，会先调用此方法，再调用writeObject方法。此方法可将任意对象代替目标序列化对象
```java
private Object writeReplace() throws ObjectStreamException {
      ArrayList<Object> list = new ArrayList<>(2);
      list.add(this.name);
      list.add(this.age);
      return list;
  }
public static void main(String[] args) throws Exception {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.txt"));
          ObjectInputStream ios = new ObjectInputStream(new FileInputStream("person.txt"))) {
        Person person = new Person("9龙", 23);
        oos.writeObject(person);
        ArrayList list = (ArrayList)ios.readObject();
        System.out.println(list);
    }
}
```

- readResolve：反序列化时替换反序列化出的对象，反序列化出来的对象被立即丢弃。此方法在readeObject后调用。
```java
     private Object readResolve() throws ObjectStreamException{
        return new HashMap<String,Integer>("brady", 23);
    }
    public static void main(String[] args) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.txt"));
             ObjectInputStream ios = new ObjectInputStream(new FileInputStream("test.txt"))) {
            Person person = new Person("9龙", 23);
            oos.writeObject(person);
            HashMap map = (HashMap)ios.readObject();
            System.out.println(map);
        }
    }
```

readResolve常用来反序列单例类，保证单例类的唯一性。

:::warning 注意
readResolve与writeReplace的访问修饰符可以是private、protected、public，如果父类重写了这两个方法，子类都需要根据自身需求重写，这显然不是一个好的设计。通常建议对于final修饰的类重写readResolve方法没有问题；否则，重写readResolve使用private修饰。
:::

## 三、强制自定义序列化
通过实现Externalizable接口，必须实现writeExternal、readExternal方法。
```java
public interface Externalizable extends java.io.Serializable {
     void writeExternal(ObjectOutput out) throws IOException;
     void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
}
```
:::details 展开
```java
package com.demo.serilizable;


import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * ExPerson
 * <p>Description:</p>
 *
 * @author: Gentvel
 * @since: 1.0
 * @see: ExPerson
 */
public class ExPerson implements Externalizable {
    private String name;
    private int age;
    //注意，必须加上pulic 无参构造器
    public ExPerson() {
    }

    public ExPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.name);
        out.writeInt(this.age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name= (String)in.readObject();
        this.age = in.readInt();
    }

    @Override
    public String toString() {
        return "ExPerson{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        OutputStream out;
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/test.txt"));
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:/test.txt"));) {
            oos.writeObject(new ExPerson("gredy",23));
            ExPerson exPerson= (ExPerson)ois.readObject();
            System.out.println(exPerson);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

```
:::

:::warning 注意
Externalizable接口不同于Serializable接口，实现此接口必须实现接口中的两个方法实现自定义序列化，这是强制性的；特别之处是必须提供pulic的无参构造器，因为在反序列化的时候需要反射创建对象。
:::

## 四、两种序列化对比
实现Serializable接口|实现Externalizable接口
-|-
系统自动存储必要的信息|程序员决定存储哪些信息
Java内建支持，易于实现，只需要实现该接口即可，无需任何代码|支持	必须实现接口内的两个方法
性能略差|性能略好

虽然Externalizable接口带来了一定的性能提升，但变成复杂度也提高了，所以一般通过实现Serializable接口进行序列化。

## 五、序列化版本号serialVersionUID
反序列化必须拥有class文件，但随着项目的升级，class文件也会升级，序列化保证升级前后的兼容性提供了一个`private static final long serialVersionUID`的序列化版本号，只有版本号相同，即使更改了序列化属性，对象也可以正确被反序列化回来。
如果反序列化使用的class的版本号与序列化时使用的不一致，反序列化会报InvalidClassException异常。序列化版本号可自由指定，如果不指定，JVM会根据类信息自己计算一个版本号，这样随着class的升级，就无法正确反序列化；不指定版本号另一个明显隐患是，不利于jvm间的移植，可能class文件没有更改，但不同jvm可能计算的规则不一样，这样也会导致无法反序列化。
以下三种情况需要修改序列号：
- 如果只是修改了方法，反序列化不容影响，则无需修改版本号；
- 如果只是修改了静态变量，瞬态变量（transient修饰的变量），反序列化不受影响，无需修改版本号；
- 如果修改了非瞬态变量，则可能导致反序列化失败。如果新类中实例变量的类型与序列化时类的类型不一致，则会反序列化失败，这时候需要更改serialVersionUID。如果只是新增了实例变量，则反序列化回来新增的是默认值；如果减少了实例变量，反序列化时会忽略掉减少的实例变量。