package com.whistle.code.spring.annotation.configuration;

import com.whistle.code.springboot.extend.School;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Configuration
public class SpringConfiguration {
    @Bean
    public School school(){
        return new School();
    }
}
