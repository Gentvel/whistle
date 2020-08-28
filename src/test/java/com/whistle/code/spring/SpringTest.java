package com.whistle.code.spring;

import com.whistle.code.CommonTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.spring.SpringTset
 */
public class SpringTest  extends CommonTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ConfigurationConfig.class);
        Object configurationConfig = ac.getBean("configurationConfig");
        System.out.println(configurationConfig);
    }
}
