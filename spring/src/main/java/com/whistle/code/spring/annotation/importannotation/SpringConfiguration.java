package com.whistle.code.spring.annotation.importannotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Configuration
@Import(CustomImportSelector.class)
@ComponentScan("com.whistle.code.spring.annotation.bean")
public class SpringConfiguration {
}
