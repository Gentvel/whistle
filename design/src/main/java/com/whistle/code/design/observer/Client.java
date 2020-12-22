package com.whistle.code.design.observer;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Client {
    public static void main(String[] args) {
        MilkStation milkStation = new MilkStation();
        milkStation.add(new People());
        milkStation.add(new People1());
        milkStation.notifyObserver();
    }
}
