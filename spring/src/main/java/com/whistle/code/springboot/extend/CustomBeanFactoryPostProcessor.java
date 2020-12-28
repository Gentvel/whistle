package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * beanFactory的扩展接口，调用时机在spring在读取beanDefinition信息之后，实例化bean之前。
 * 用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息。
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    Logger logger = LoggerFactory.getLogger(CustomBeanFactoryPostProcessor.class);
    private ConfigurableApplicationContext applicationContext;

    public CustomBeanFactoryPostProcessor(ConfigurableApplicationContext context){
        this.applicationContext = context;
    }

    /**
     * 用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息。
     * @param beanFactory beanFactory
     * @throws BeansException e
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.info("CustomBeanFactoryPostProcessor,调用在读取beanDefinition之后，实例化bean之前,此时context状态{}",applicationContext.isRunning());
    }
}
