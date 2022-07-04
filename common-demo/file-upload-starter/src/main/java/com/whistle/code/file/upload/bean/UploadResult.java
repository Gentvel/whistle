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
     * 临时文件SM3
     */
    private String identifier;

    /**
     * 临时文件大小
     */
    @JsonIgnore
    private long loadSize;
    /**
     *
     *
     * 上传状态：
     * 1-分片已上传，2-分片上传失败，3-分片上传完成
     * 5-文件合并失败，6-文件校验失败
     * 7-文件已处理完成 8-分片已存在 9-整体文件已存在
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
    /**
     * 当前文件分片
     */
    private Integer chunkNumber;
    /**
     * 文件是否需要合并
     */
    private boolean needMerge;

    /**
     *
     */
    private String message;
}
