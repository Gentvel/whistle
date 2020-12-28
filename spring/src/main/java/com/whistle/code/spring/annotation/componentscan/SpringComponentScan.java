package com.whistle.code.spring.annotation.componentscan;

import com.whistle.code.spring.annotation.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@ComponentScan(
        basePackages = "com.whistle.code.spring.annotation.bean",
        nameGenerator = CustomizeBeanNameGenerator.class
        //excludeFilters = @ComponentScan.Filter(value = Service.class)
)
public class SpringComponentScan {
    //自定义 customize
    @Bean
    public User user(){
        return new User();
    }
}
