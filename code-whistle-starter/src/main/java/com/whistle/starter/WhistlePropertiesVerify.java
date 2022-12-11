package com.whistle.starter;

import cn.hutool.core.util.NumberUtil;
import com.whistle.starter.boot.WhistleProperties;
import com.whistle.starter.boot.WhistleWebProperties;
import com.whistle.starter.enums.EnvEnum;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.*;

/**
 * @author Gentvel
 */
public final class WhistlePropertiesVerify {

    public static Map<String,Object> verify(ConfigurableEnvironment environment){
        Map<String,Object> extVars = new HashMap<>(16);
        StringJoiner excludeList = new StringJoiner(",");
        extVars.putAll(Objects.requireNonNull(verifyKnife4J(environment,excludeList)));
        extVars.put("spring.autoconfigure.exclude",excludeList.toString());
        //清空临时缓存变量
        WhistleConstants.getWeakCache().clear();
        return extVars;
    }

    private static Map<String, Object> verifyKnife4J(ConfigurableEnvironment environment,StringJoiner excludeList) {
        Boolean enableKnife4j = environment.getProperty(getWebVariableName("enable-knife4j"), Boolean.class, false);
        String env = environment.getProperty("env", String.class, EnvEnum.DEV.name());
        Map<String, Object> knife4j;
        if(enableKnife4j&&!env.equalsIgnoreCase(EnvEnum.PROD.name())){
            knife4j = WhistleConstants.knife4jEnable();
            float lastKnife4jVersion = 2.6f;
            String version = SpringBootVersion.getVersion();
            //Fix Issue https://github.com/spring-projects/spring-boot/issues/28794
            //在Spring Boot 2.6及以后，引入的新PathPatternParser导致的Swagger 失效
            if(NumberUtil.parseFloat(version)>=lastKnife4jVersion){
                knife4j.put("spring.mvc.pathmatch.matching-strategy","ant_path_matcher");
            }
        }else{
            knife4j = WhistleConstants.knife4jDisable();
            excludeList.add("springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration");
            excludeList.add("com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration");
        }
        return knife4j;
    }


    private static String getVariableName(String name){
        StringJoiner stringJoiner = new StringJoiner(".");
        stringJoiner.add(WhistleProperties.PREFIX).add(name);
        return stringJoiner.toString();
    }

    private static String getWebVariableName(String name){
        StringJoiner stringJoiner = new StringJoiner(".");
        stringJoiner.add(WhistleWebProperties.PREFIX).add(name);
        return stringJoiner.toString();
    }

}
