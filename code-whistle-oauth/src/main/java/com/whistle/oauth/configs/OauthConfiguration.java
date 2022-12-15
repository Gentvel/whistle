package com.whistle.oauth.configs;

import cn.dev33.satoken.jwt.StpLogicJwtForMixin;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gentvel
 */
@Configuration
public class OauthConfiguration {
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForMixin();
    }
}
