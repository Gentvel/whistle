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
        List<String> excludeList = new ArrayList<>();
        extVars.putAll(verifyKnife4J(environment,excludeList));
        extVars.putAll(verifyRedisson(environment,excludeList));
        extVars.putAll(verifyDatabase(environment,excludeList));
        extVars.putAll(verifyDynamic(environment,excludeList));
        StringJoiner excludeString = new StringJoiner(",");
        excludeList.forEach(excludeString::add);
        extVars.put("spring.autoconfigure.exclude",excludeString.toString());
        //清空临时缓存变量
        WhistleConstants.getWeakCache().clear();
        return extVars;
    }

    private static Map<String, Object> verifyKnife4J(ConfigurableEnvironment environment,List<String> excludeList) {
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

    private static Map<String, Object> verifyRedisson(ConfigurableEnvironment environment,List<String> excludeList) {
        Boolean enableRedisson = environment.getProperty(getVariableName("redisson"), Boolean.class, false);
        Map<String, Object> redisson = WhistleConstants.empty();
        if(!enableRedisson){
            excludeList.add("org.redisson.spring.starter.RedissonAutoConfiguration");
            excludeList.add("org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration");
        }
        return redisson;
    }

    private static Map<String, Object> verifyDatabase(ConfigurableEnvironment environment,List<String> excludeList) {
        Boolean enableDatabase = environment.getProperty(getVariableName("database"), Boolean.class, false);
        Map<String, Object> database = WhistleConstants.empty();
        if(!enableDatabase){
            excludeList.add("com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration");
            excludeList.add("com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration");
            excludeList.add("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
        }
        return database;
    }

    private static Map<String, Object> verifyDynamic(ConfigurableEnvironment environment,List<String> excludeList) {
        Boolean enableDynamic = environment.getProperty(getVariableName("dynamic"), Boolean.class, false);
        Boolean enableDatabase = environment.getProperty(getVariableName("database"), Boolean.class, false);
        Map<String, Object> database = WhistleConstants.empty();
        if(enableDynamic){
            if(!enableDatabase){
                excludeList.remove("com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration");
                excludeList.remove("com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration");
                excludeList.remove("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
            }
        }else{
            excludeList.add("com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration");
        }
        return database;
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
