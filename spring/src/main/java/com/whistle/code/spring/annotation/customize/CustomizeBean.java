package com.whistle.code.spring.annotation.customize;

import org.springframework.context.annotation.Bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Bean
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CustomizeBean {
}
