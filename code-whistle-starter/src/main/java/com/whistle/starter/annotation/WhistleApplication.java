package com.whistle.starter.annotation;

import java.lang.annotation.*;

/**
 * @author Gentvel
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WhistleApplication {
}
