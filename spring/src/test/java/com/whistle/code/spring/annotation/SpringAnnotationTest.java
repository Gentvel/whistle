package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.another.School;
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
         applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    }

    @Test
    public void testBasic(){
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name:beanDefinitionNames){
            System.out.println(name);
        }
        final School school = applicationContext.getBean("school", School.class);
        System.out.println(school.getUser().getName());
    }
}
