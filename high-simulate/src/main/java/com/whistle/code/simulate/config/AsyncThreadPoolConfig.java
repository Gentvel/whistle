package com.whistle.code.simulate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncThreadPoolConfig implements AsyncConfigurer {
    @Bean("defaultPool")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(4);
        //最大线程数
        executor.setMaxPoolSize(8);
        //队列容量
        executor.setQueueCapacity(32);
        //活跃时间
        executor.setKeepAliveSeconds(60);
        //线程名字前缀
        executor.setThreadNamePrefix("file-thread-");
        /*
            当poolSize已达到maxPoolSize，如何处理新任务（是拒绝还是交由其它线程处理）
            CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("==========================" + ex.getMessage() + "=======================", ex);
            log.error("exception method:" + method.getName());
        };
    }
}
