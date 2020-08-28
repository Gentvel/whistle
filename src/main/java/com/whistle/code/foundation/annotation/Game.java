package com.whistle.code.foundation.annotation;

import java.lang.annotation.*;

/**
 * Description: <br>
 * 元注解有5种，第一个是@Target、@Retention、@Document、@Inherited 和@Repeatable
 * trrid
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.foundation.annotation.AnoTest
 */
@Repeatable(Play.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Game {
    String value() default "";
}
