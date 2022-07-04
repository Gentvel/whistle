package com.whistle.code.file.upload.boot;

import com.whistle.code.file.upload.UploadHandler;
import com.whistle.code.file.upload.UploadProcessor;
import com.whistle.code.file.upload.Uploader;
import com.whistle.code.file.upload.handlers.DefaultHandler;
import com.whistle.code.file.upload.processors.BufferedStreamProcessor;
import com.whistle.code.file.upload.processors.DefaultProcessor;
import com.whistle.code.file.upload.processors.RandomAccessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(FileUploadProperties.class)
public class FileUploadAutoConfiguration {

    @Bean
    @ConditionalOnClass(Uploader.class)
    public Uploader uploader(UploadProcessor uploadProcessor){
        return new Uploader(uploadProcessor);
    }
    @Bean
    @Lazy
    @ConditionalOnClass(UploadHandler.class)
    public UploadHandler uploadHandler(){
        return new DefaultHandler();
    }


    @Bean
    @Lazy
    @ConditionalOnClass(UploadProcessor.class)
    @ConditionalOnProperty(prefix=FileUploadProperties.FILE_UPLOAD_PREFIX,name="process-type",havingValue = "DEFAULT",matchIfMissing = true)
    public UploadProcessor defaultProcessor(UploadHandler uploadHandler){
        return new DefaultProcessor(uploadHandler);
    }

    @Bean
    @Lazy
    @ConditionalOnClass(UploadProcessor.class)
    @ConditionalOnProperty(prefix=FileUploadProperties.FILE_UPLOAD_PREFIX,name="process-type",havingValue = "RANDOM")
    public UploadProcessor randomProcessor(UploadHandler uploadHandler){
        return new RandomAccessProcessor(uploadHandler);
    }

    @Bean
    @Lazy
    @ConditionalOnClass(UploadProcessor.class)
    @ConditionalOnProperty(prefix=FileUploadProperties.FILE_UPLOAD_PREFIX,name="process-type",havingValue = "BUFFERED")
    public UploadProcessor bufferedProcessor(UploadHandler uploadHandler){
        return new BufferedStreamProcessor(uploadHandler);
    }
    @Lazy
    @Bean(FileUploadProperties.FILE_UPLOAD_POOL)
    public Executor fileUploadExecutor(FileUploadProperties fileUploadProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        if(fileUploadProperties.getCoreSize()<=0){
            log.warn("上传线程池参数错误...已使用默认线程参数");
        }
        //核心线程池大小
        executor.setCorePoolSize(fileUploadProperties.getCoreSize()<=0?2: fileUploadProperties.getCoreSize());
        //最大线程数
        executor.setMaxPoolSize(fileUploadProperties.getMaxSize()<=0?executor.getCorePoolSize()*2: fileUploadProperties.getMaxSize());
        //队列容量
        executor.setQueueCapacity(fileUploadProperties.getQueueCapacity()<=0?80: fileUploadProperties.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(fileUploadProperties.getKeepAlive()<=0?60: fileUploadProperties.getKeepAlive());
        //线程名字前缀
        executor.setThreadNamePrefix("uploader-thread-");
        /*
            当poolSize已达到maxPoolSize，如何处理新任务（是拒绝还是交由其它线程处理）
            CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
