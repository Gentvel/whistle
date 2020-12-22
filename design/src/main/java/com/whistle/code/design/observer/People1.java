package com.whistle.code.design.observer;

/**
 * TODO
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class People1 implements Observer{
    @Override
    public void update() {
        System.out.println("People1 收到牛奶");
    }
}
