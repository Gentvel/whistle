package com.whistle.code.customize.ioc.bean.testbean;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Person {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void doSomething(){
        System.out.println("Person do something");
    }

    public void init(){
        System.out.println("Person init");
    }

    public void destroy(){
        System.out.println("Person destroy");
    }
}
