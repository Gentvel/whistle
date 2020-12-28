package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 这个类也是Aware扩展的一种，触发点在bean的初始化之前，也就是postProcessBeforeInitialization之前，这个类的触发点方法只有一个：setBeanName
 * @see AbstractAutowireCapableBeanFactory#invokeAwareMethods(String, Object)
 * @author Gentvel
 * @version 1.0.0
 */
@Component
public class CustomBeanNameAware implements BeanNameAware {
    Logger logger = LoggerFactory.getLogger(CustomBeanNameAware.class);
    @Override
    public void setBeanName(String name) {
        if("school".equals(name)){
            logger.info("用户可以扩展这个点，在初始化bean之前拿到spring容器中注册的的beanName，来自行修改这个beanName的值。\n");
        }
    }
}
