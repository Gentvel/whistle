package com.whistle.basic.redis;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Zz
 */
@Slf4j
@Component
public class RedisTemplateDemo {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private void getTestSet(){
        redisTemplate.opsForSet().add("test","aa");
        redisTemplate.opsForSet().add("test1","aa");
    }

    private void setTestSet(){
        Set<Object> test = redisTemplate.opsForSet().members("test");
        assert test != null;
        for (Object o:test){
            log.info(o.toString());
        }
    }

    private void demo(){
        setTestSet();
        getTestSet();
    }


}
