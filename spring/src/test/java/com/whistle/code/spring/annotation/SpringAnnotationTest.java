package com.whistle.code.spring.annotation;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class SpringAnnotationTest {
    private ApplicationContext applicationContext;
    @Before
    public void initialize(){
         applicationContext = new AnnotationConfigApplicationContext("com.whistle.code.spring.annotation");
    }

    @Test
    public void testBasic(){
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name:beanDefinitionNames){
            System.out.println(name);
        }
    }
}
