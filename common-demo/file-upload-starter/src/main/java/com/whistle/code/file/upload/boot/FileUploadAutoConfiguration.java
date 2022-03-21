package com.whistle.code.file.upload.boot;

import com.whistle.code.file.upload.UploadHandler;
import com.whistle.code.file.upload.UploadProcessor;
import com.whistle.code.file.upload.Uploader;
import com.whistle.code.file.upload.handlers.DefaultHandler;
import com.whistle.code.file.upload.processors.BufferedStreamProcessor;
import com.whistle.code.file.upload.processors.DefaultProcessor;
import com.whistle.code.file.upload.processors.RandomAccessProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

/**
 * @author Lin
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(FileUploadProperties.class)
public class FileUploadAutoConfiguration {

    @Bean
    @ConditionalOnClass(Uploader.class)
    public Uploader uploader(UploadProcessor uploadProcessor){
        return new Uploader(uploadProcessor);
    }
    @Bean
    @ConditionalOnClass(UploadHandler.class)
    public UploadHandler uploadHandler(){
        return new DefaultHandler();
    }


    @Bean
//    @Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnClass(UploadProcessor.class)
    @ConditionalOnProperty(prefix=FileUploadProperties.FILE_UPLOAD_PREFIX,name="process-type",havingValue = "DEFAULT",matchIfMissing = true)
    public UploadProcessor defaultProcessor(UploadHandler uploadHandler){
        return new DefaultProcessor(uploadHandler);
    }

    @Bean
//    @Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnClass(UploadProcessor.class)
    @ConditionalOnProperty(prefix=FileUploadProperties.FILE_UPLOAD_PREFIX,name="process-type",havingValue = "RANDOM")
    public UploadProcessor randomProcessor(UploadHandler uploadHandler){
        return new RandomAccessProcessor(uploadHandler);
    }

    @Bean
//    @Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
    @ConditionalOnClass(UploadProcessor.class)
    @ConditionalOnProperty(prefix=FileUploadProperties.FILE_UPLOAD_PREFIX,name="process-type",havingValue = "BUFFERED")
    public UploadProcessor bufferedProcessor(UploadHandler uploadHandler){
        return new BufferedStreamProcessor(uploadHandler);
    }


}
