package com.whistle.code.main.starter.test;

import com.whistle.code.file.upload.Uploader;
import com.whistle.code.file.upload.bean.FileUploadContext;
import com.whistle.code.main.starter.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
@RequestMapping("file")
@RestController
public class UploaderController {
    @Resource
    private Uploader uploader;
    @GetMapping("upload")
    public Result<?> getUploadResult(FileUploadContext fileUploadContext){
        log.info("Get::{}", fileUploadContext.toString());
        return Result.success(uploader.preQuery(fileUploadContext));
    }
    @PostMapping("upload")
    public Result<?> upload(FileUploadContext fileUploadContext){
        log.info(fileUploadContext.toString());
        return Result.success(uploader.upload(fileUploadContext));
    }

    @PostMapping("merge")
    public Result<?> merge(FileUploadContext fileUploadContext){
        log.info(fileUploadContext.toString());
        return Result.success(uploader.merge(fileUploadContext));
    }

}
