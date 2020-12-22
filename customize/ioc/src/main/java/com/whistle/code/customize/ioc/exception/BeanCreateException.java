package com.whistle.code.customize.ioc.exception;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BeanCreateException extends RuntimeException{
    public BeanCreateException(){
        super("创建Bean失败！");
    }

    public BeanCreateException(String message){
        super(message);
    }
}
