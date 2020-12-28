package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * 在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
 * 可以在这里动态注册自己的beanDefinition，可以加载classpath之外的bean
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    Logger logger = LoggerFactory.getLogger(CustomBeanDefinitionRegistryPostProcessor.class);


    private ConfigurableApplicationContext applicationContext;

    public CustomBeanDefinitionRegistryPostProcessor(ConfigurableApplicationContext context){
        this.applicationContext = context;
    }
    /**
     * 可以在此处定义一些bean注册到容器中
     * {@link AbstractApplicationContext#refresh()}
     * {@link AbstractApplicationContext#finishBeanFactoryInitialization}
     * @param registry registry
     * @throws BeansException exception
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        logger.info("CustomBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry ,可以在此处动态注册beanDefinition，或者加载classpath之外的bean");
        BeanDefinition beanDefinition = new GenericBeanDefinition();

        beanDefinition.setBeanClassName("com.whistle.code.springboot.extend.School");
        //只有注册成single的时候才会在初始化beanFactory时将bean实例化，调用方法为
        beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
        registry.registerBeanDefinition("school",beanDefinition);
        logger.info("测试：注册一个school的bean 此时context：{}",applicationContext.isRunning());
    }

    /**
     * 此方法是beanFactory初始化完成后的拓展方法，可以对bean进行一些处理
     * @param beanFactory beanFactory
     * @throws BeansException exception
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.info("CustomBeanDefinitionRegistryPostProcessor.postProcessBeanFactory 获取注册的school bean 内容为:{}",beanFactory.getBeanDefinition("school"));
    }
}
