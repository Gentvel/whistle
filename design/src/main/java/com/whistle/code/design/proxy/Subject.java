package com.whistle.code.design.proxy;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Subject {
    private RealSubject realSubject = new RealSubject();
    public void operate(){
        System.out.println(" pre operate");
        realSubject.operate();
        System.out.println(" after operate");
    }
}
