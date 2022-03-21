package com.whistle.code.file.upload;

import com.whistle.code.file.upload.bean.ChunkUploaded;
import com.whistle.code.file.upload.bean.FileUploadContext;

import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
public interface UploadHandler {
    List<ChunkUploaded> preCheckUpload(FileUploadContext context);
    String getUUID(FileUploadContext context);
    void beforeProcess(FileUploadContext context);
    void afterProcess(FileUploadContext context);
    void beforeMerge(FileUploadContext context);
    void afterMerge(FileUploadContext context);
}
