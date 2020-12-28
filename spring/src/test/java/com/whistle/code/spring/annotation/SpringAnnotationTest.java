package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.bean.School;
import com.whistle.code.spring.annotation.componentscan.SpringComponentScan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("annotation test")
public class SpringAnnotationTest {
    private ApplicationContext applicationContext;
    @BeforeEach
    public void initialize(){
         applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    }

    @Test
    @DisplayName("test annotation")
    public void testBasic(){
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name:beanDefinitionNames){
            System.out.println(name);
        }
        final School school = applicationContext.getBean("school", School.class);
        System.out.println(school.getUser().getName());
    }
}
