package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.customize.CustomizeBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@ComponentScan(basePackages = "com.whistle.code.spring.annotation",
        nameGenerator = CustomizeBeanNameGenerator.class)
public class SpringComponentScan {
    //自定义 customize
}
