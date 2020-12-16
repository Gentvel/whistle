package com.whistle.code.java.java8.functionquote;

/**
 * TODO
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Person {
    private String name;
    private int age;

    public Person() {
        System.out.println("Not Parameter Constructor");
    }

    public Person(String name){
        this.name = name;
        System.out.println("Single Parameter Constructor");
    }

    public Person(String name,int age){
        this.name = name;
        this.age = age;
        System.out.println("Multiple Parameter Constructor");
    }
    public String getName(){
        return name;
    }

    public String getMeddle(String meddle){
        return meddle+name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
