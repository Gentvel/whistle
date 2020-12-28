package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 整个spring容器在刷新之前初始化ConfigurableApplicationContext的回调接口，简单来说，就是在容器刷新之前调用此类的initialize方法。
 * 这个点允许被用户自己扩展。用户可以在整个spring容器还没被初始化之前做一些事情。
 * @author Gentvel
 * @version 1.0.0
 */

public class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    Logger logger = LoggerFactory.getLogger(CustomApplicationContextInitializer.class);
    /**
     * 可以想到的场景可能为，在最开始激活一些配置，或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作。
     * 像nacos的服务节点先去找配置文件然后配置文件的生成操作
     * @param applicationContext context
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        logger.info("CustomApplicationContextInitializer, 可以在此处扩展在容器未初始化时配置一些操作，比如：添加listener,配置参数等等，此时context状态为：{}", applicationContext.isRunning());
        applicationContext.addBeanFactoryPostProcessor(new CustomBeanDefinitionRegistryPostProcessor(applicationContext));
        applicationContext.addBeanFactoryPostProcessor(new CustomBeanFactoryPostProcessor(applicationContext));
    }
}
