package com.whistle.code.customize.ioc.bean;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface BeanFactory extends BeanDefinitionRegistry{
    Object getBean(String beanName);
}
