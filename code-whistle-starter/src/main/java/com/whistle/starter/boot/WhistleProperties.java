package com.whistle.starter.boot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Gentvel
 */
@Setter
@Getter
@ConfigurationProperties(prefix = WhistleProperties.PREFIX)
public class WhistleProperties {
    public static final String PREFIX = "whistle";
    /**
     * 是否开启Redisson
     */
    private boolean enableRedisson;
}
