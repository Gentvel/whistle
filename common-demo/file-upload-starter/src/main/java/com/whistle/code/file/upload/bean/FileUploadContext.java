package com.whistle.code.file.upload.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;

/**
 * @author Lin
 * @version 1.0.0
 */
@Data
@ToString
public class FileUploadContext implements Serializable {
    @ApiModelProperty("文件MD5")
    private String identifier;
    @ApiModelProperty("文件名")
    private String filename;
    @ApiModelProperty("文件总大小")
    private long totalSize;
    @ApiModelProperty("当前分片大小")
    private long chunkSize;
    @ApiModelProperty("当前分片已上传大小")
    private long chunkLoaded;
    @ApiModelProperty("当前分片序号")
    private Integer chunkNumber;
    @ApiModelProperty("分片总数")
    private Integer totalChunks;
    @ApiModelProperty("文件")
    private MultipartFile file;
    /**
     * 是否前置查询
     */
    private boolean pre;
    /**
     * 上传处理结果
     */
    private UploadResult uploadResult = new UploadResult();

    /**
     * 分片上传临时文件
     */
    private File tempChunkFile;
    /**
     * 临时文件所在目录
     */
    private String tempChunkFileDir;
    /**
     * 临时分片文件名
     */
    private String tempChunkFileName;
    /**
     * 文件合并后的文件名
     */
    private String mergedFile;

    /**
     * 文件是否需要合并
     */
    private boolean needMerge;
}
