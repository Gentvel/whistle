package com.whistle.code.spring.customize;

import com.whistle.code.spring.annotation.componentscan.SpringComponentScan;
import com.whistle.code.spring.annotation.bean.School;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("spring 拓展功能")
public class CustomizeTest {
    @Test
    @Disabled
    public void testCustomizeAnnotationConfigApplicationContext(){
        ApplicationContext applicationContext = new CustomAnnotationConfigApplicationContext(SpringComponentScan.class);
        final School school = applicationContext.getBean("school", School.class);
        System.out.println(school.getUser().getName());
    }
}
