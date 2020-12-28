package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.bean.User;
import com.whistle.code.spring.annotation.bean.UserService;
import com.whistle.code.spring.annotation.importannotation.SpringConfiguration;
import com.whistle.code.spring.annotation.importannotation.SpringImport;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@DisplayName("import注解测试")
public class ImportTest {

    AnnotationConfigApplicationContext annotationConfigApplicationContext;
    @BeforeEach
    public void init(){
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    }

    @Test
    @DisplayName("测试import")
    public void testImport(){
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        Stream.of(beanDefinitionNames).forEach(System.out::println);
        UserService bean = annotationConfigApplicationContext.getBean(UserService.class);
        bean.addUser();
    }

}
