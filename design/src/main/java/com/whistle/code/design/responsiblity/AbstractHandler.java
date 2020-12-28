package com.whistle.code.design.responsiblity;

import java.util.List;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public abstract class AbstractHandler<T> implements Handler<T>{
    private Handler<T> next;

    public void setNext(Handler<T> next){
        this.next = next;
    }

    public Handler<T> getNext(){
        return next;
    }

}
