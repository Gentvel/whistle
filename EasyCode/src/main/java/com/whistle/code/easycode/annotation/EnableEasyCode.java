package com.whistle.code.easycode.annotation;

import com.whistle.code.easycode.importor.ClassImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ClassImportSelector.class)
public @interface EnableEasyCode {
}
