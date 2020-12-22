package com.whistle.code.design.observer;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class People implements Observer{


    @Override
    public void update() {
        System.out.println("People 收到牛奶");
    }
}
