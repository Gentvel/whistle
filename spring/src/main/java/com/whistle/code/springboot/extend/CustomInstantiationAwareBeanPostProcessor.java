package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 *
 * BeanPostProcessor接口只在bean的初始化化阶段进行扩展（注入spring上下文前后，
 * 而InstantiationAwareBeanPostProcessor接口在此基础上增加了3个方法，把可扩展的范围增加了实例化阶段和属性注入阶段。
 *  实例化阶段和初始化阶段，
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
 * @author Gentvel
 * @version 1.0.0
 */
@Component
public class CustomInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    Logger logger = LoggerFactory.getLogger(CustomInstantiationAwareBeanPostProcessor.class);

    /**
     * 实例化bean之前，相当于new这个bean之前
     * @param beanClass class
     * @param beanName bean
     * @return Object instance
     * @throws BeansException exception
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if(beanName.equalsIgnoreCase("school")){
            logger.info("postProcessBeforeInstantiation 实例化bean之前，相当于new这个bean之前 {}",beanClass);
        }
        return null;
    }



    /**
     * 实例化bean之后，相当于new这个bean之后
     * @param bean bean
     * @param beanName bean
     * @return b
     * @throws BeansException exception
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(beanName.equalsIgnoreCase("school")){
            logger.info("postProcessAfterInstantiation 实实例化bean之后，相当于new这个bean之后 {}",bean.toString());
        }
        return true;
    }

    /**
     * bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if(beanName.equalsIgnoreCase("school")) {
            logger.info("postProcessProperties bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现");
        }
        return pvs;
    }

    /**
     * 初始化bean之前，相当于把bean注入spring上下文之前
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equalsIgnoreCase("school")) {
            logger.info("postProcessBeforeInitialization 初始化bean之前，相当于把bean注入spring上下文之前");
        }
        return bean;
    }

    /**
     * 初始化bean之后，相当于把bean注入spring上下文之后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equalsIgnoreCase("school")) {
            logger.info("postProcessAfterInitialization 初始化bean之后，相当于把bean注入spring上下文之后");
        }
        return bean;
    }
}
