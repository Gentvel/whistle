package com.whistle.code.file.upload;

import com.whistle.code.file.upload.bean.FileUploadContext;
import com.whistle.code.file.upload.bean.UploadResult;

/**
 * 文件上传处理器
 * @author Lin
 * @version 1.0.0
 */
public interface UploadProcessor {


    /**
     * 预查询
     * @param context 上下文
     */
    UploadResult preQuery(FileUploadContext context);

    /**
     * 上传文件处理
     * @param context 文件上下文
     */
    UploadResult process(FileUploadContext context);


    /**
     * 合并文件
     * @param context 上下文
     */
    UploadResult merge(FileUploadContext context);

}
