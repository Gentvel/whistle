package com.whistle.code.customize.ioc.bean;

import com.whistle.code.customize.ioc.exception.BeanDefinitionRegistryException;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName,BeanDefinition beanDefinition) throws BeanDefinitionRegistryException;
    BeanDefinition getBeanDefinition(String beanName);
    boolean containBeanDefinition(String beanName);
}
