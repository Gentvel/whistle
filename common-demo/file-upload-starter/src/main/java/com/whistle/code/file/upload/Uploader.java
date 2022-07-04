package com.whistle.code.file.upload;

import com.whistle.code.file.upload.bean.FileUploadContext;
import com.whistle.code.file.upload.bean.UploadResult;
import com.whistle.code.file.upload.boot.FileUploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class Uploader {

    @Resource(name = FileUploadProperties.FILE_UPLOAD_POOL)
    private Executor fileUploadExecutor;
    private final UploadProcessor uploadProcessor;
    private final UploadResult errorResult;

    public Uploader(UploadProcessor uploadProcessor) {
        errorResult = new UploadResult();
        errorResult.setStatus(9);
        errorResult.setMessage("操作失败");
        this.uploadProcessor = uploadProcessor;

    }

    /**
     * 文件上传
     * @param context
     */
    public UploadResult upload(FileUploadContext context) {
        //return uploadProcessor.process(context);


        CompletableFuture<UploadResult> future = CompletableFuture.supplyAsync(() -> uploadProcessor.process(context), fileUploadExecutor);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
            return errorResult;
        }
    }

    /**
     * 预查询
     * @param context
     */
    public UploadResult preQuery(FileUploadContext context) {
        CompletableFuture<UploadResult> future = CompletableFuture.supplyAsync(() -> uploadProcessor.preQuery(context), fileUploadExecutor);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
            return errorResult;
        }
    }

    /**
     * 文件合并
     *
     * @param context
     */
    public UploadResult merge(FileUploadContext context) {
        CompletableFuture<UploadResult> future = CompletableFuture.supplyAsync(() -> uploadProcessor.merge(context), fileUploadExecutor);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
            return errorResult;
        }
    }

}
