package com.whistle.code.file.upload.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
@Data
public class UploadResult {
    /**
     * 临时文件MD5
     */
    private String identifier;
    /**
     * 临时文件UID
     */
    private String loadedUID;
    /**
     * 临时文件大小
     */
    private long loadSize;
    /**
     * 上传状态 0-未上传，1-分片已上传，2-文件已合并，3-文件已校验，4-文件已完成
     */
    private int status=0;
    /**
     * 分片已全部上传完成，实现秒传效果
     */
    private boolean skipUpload;

    /**
     * 已上传文件
     */
    private List<ChunkUploaded> uploaded;
}
