package com.whistle.code.design.observer;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class MilkStation {

    private Set<Observer> list;

    public MilkStation(){
        list = new HashSet<>();
    }

    protected void add(Observer observer){
        list.add(observer);
    }

    protected void remove(Observer observer){
        list.remove(observer);
    }

    public void notifyObserver(){
        list.forEach(Observer::update);
    }


}
