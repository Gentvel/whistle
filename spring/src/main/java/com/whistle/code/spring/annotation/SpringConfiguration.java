package com.whistle.code.spring.annotation;

import com.whistle.code.spring.annotation.another.School;
import com.whistle.code.spring.annotation.another.User;
import com.whistle.code.spring.annotation.customize.CustomizeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
