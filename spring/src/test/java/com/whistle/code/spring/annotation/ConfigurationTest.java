package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.configuration.SpringConfiguration;
import com.whistle.code.springboot.extend.School;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("Configuration 注解测试")
public class ConfigurationTest {
    AnnotationConfigApplicationContext applicationContext;
    @BeforeEach
    public void init(){
        applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    }

    @Test
    public void testConfiguration(){
        /*
        可以看到在debug模式下能看到注册到容器中的springConfigurationBean,说明在spring容器中会注入被Configuration注入的bean
         */
        School school = (School) applicationContext.getBean("school");
        school.toString();
    }


}
