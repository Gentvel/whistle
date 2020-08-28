package com.whistle.code.foundation.annotation;

import java.lang.annotation.*;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.foundation.annotation.Play
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Play {
    Game[] value();
}
