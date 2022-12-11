package com.whistle.starter.boot;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Gentvel
 */
@Slf4j
@Configuration
@AutoConfigureBefore({Knife4jAutoConfiguration.class, OpenApiAutoConfiguration.class})
@Import({WhistleStarterSelector.class})
@EnableConfigurationProperties({WhistleProperties.class, WhistleWebProperties.class})
public class WhistleSpringBootStarter {
    @Bean
    @ConditionalOnProperty(prefix = WhistleWebProperties.PREFIX, value = "enable-knife4j", havingValue = "true")
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.whistle"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("knife4j在线API接口文档")
                .contact(new Contact("Gentvel", "", "gentvel@gmail.com"))
                .version("1.0.0")
                .title("knife4j在线API接口文档")
                .build();
    }
}