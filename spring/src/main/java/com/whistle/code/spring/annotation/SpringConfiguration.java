package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.importannotation.SpringImport;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Configuration
@Import(SpringImport.class)
public class SpringConfiguration {

}