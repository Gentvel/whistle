package com.whistle.code.file.upload;

import com.whistle.code.file.upload.bean.ChunkUploaded;
import com.whistle.code.file.upload.bean.FileUploadContext;

import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
public interface UploadHandler {
    /**
     * 分片文件校验查询
     * @param context 上下文
     */
    List<ChunkUploaded> preCheckUpload(FileUploadContext context);

    /**
     * 获取分片文件UUID
     * @param context 上下文
     */
    String getUUID(FileUploadContext context);

    /**
     * 上传前置处理
     * @param context 上下文
     */
    void beforeProcess(FileUploadContext context);

    /**
     * 上传后置处理
     * @param context 上下文
     */
    void afterProcess(FileUploadContext context);

    /**
     * 合并前置处理
     * @param context 上下文
     */
    void beforeMerge(FileUploadContext context);

    /**
     * 合并后置处理
     * @param context 上下文
     */
    void afterMerge(FileUploadContext context);
}
