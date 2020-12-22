package com.whistle.code.design.proxy;


/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Client {
    public static void main(String[] args) {
        Subject subject = new Subject();
        subject.operate();
    }
}
