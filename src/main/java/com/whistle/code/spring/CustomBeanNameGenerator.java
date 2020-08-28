package com.whistle.code.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * Description: <br>
 * 在ComponentScan注解中配置BeanNameGenerator
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.spring.CustomBeanNameGenerator
 */
public class CustomBeanNameGenerator implements BeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return null;
    }
}
