package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.bean.School;
import com.whistle.code.spring.annotation.componentscan.SpringComponentScan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.Stream;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("ComponentScan 注解测试")
public class ComponentScanTest {
    AnnotationConfigApplicationContext applicationContext;

    @BeforeEach
    public void init(){
        applicationContext = new AnnotationConfigApplicationContext(SpringComponentScan.class);
    }

    @Test
    public void testComponentScan(){
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        Stream.of(beanDefinitionNames).forEach(System.out::println);
    }
}
