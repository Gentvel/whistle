package com.whistle.starter.boot;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Gentvel
 */
@Setter
@Getter
@ConfigurationProperties(prefix = WhistleWebProperties.PREFIX)
public class WhistleWebProperties {
    public static final String PREFIX = WhistleProperties.PREFIX+".web";
    /**
     * 是否开启Knife4J
     */
    private boolean enableKnife4J;
}
