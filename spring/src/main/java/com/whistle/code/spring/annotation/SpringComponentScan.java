package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.customize.CustomizeBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@ComponentScan(basePackages = "com.whistle.code.spring.annotation",
        nameGenerator = CustomizeBeanNameGenerator.class,
excludeFilters = @ComponentScan.Filter(value = Service.class))
public class SpringComponentScan {
    //自定义 customize
}