package com.whistle.code.file.upload;

import cn.hutool.extra.spring.SpringUtil;
import com.whistle.code.file.upload.bean.FileUploadContext;
import com.whistle.code.file.upload.bean.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class Uploader implements DisposableBean {

    private final UploadProcessor uploadProcessor;
    private final ThreadPoolTaskExecutor executor;
    public Uploader(UploadProcessor uploadProcessor){
        this.uploadProcessor = uploadProcessor;
        executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(4);
        //最大线程数
        executor.setMaxPoolSize(8);
        //队列容量
        executor.setQueueCapacity(32);
        //活跃时间
        executor.setKeepAliveSeconds(60);
        //线程名字前缀
        executor.setThreadNamePrefix("uploader-thread-");
        /*
            当poolSize已达到maxPoolSize，如何处理新任务（是拒绝还是交由其它线程处理）
            CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
    }

    public UploadResult upload(FileUploadContext context){
        Callable<UploadResult> callable = () -> uploadProcessor.process(context);
        Future<UploadResult> submit = executor.submit(callable);
        try {
            UploadResult uploadResult = submit.get();
            log.info(uploadResult.toString());
            return uploadResult;
        } catch (InterruptedException | ExecutionException e) {
            log.error("",e);
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        executor.destroy();
    }
}
