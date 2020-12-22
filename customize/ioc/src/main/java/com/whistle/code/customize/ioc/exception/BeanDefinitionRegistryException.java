package com.whistle.code.customize.ioc.exception;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BeanDefinitionRegistryException extends RuntimeException {
    public BeanDefinitionRegistryException(){
        super("BeanDefinition 注册异常，请检查");
    }
    public BeanDefinitionRegistryException(String message){
        super(message);
    }
}
