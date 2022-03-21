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
     * 上传文件处理
     * @param file 文件对象
     * @return
     */
    UploadResult process(FileUploadContext file);

}
