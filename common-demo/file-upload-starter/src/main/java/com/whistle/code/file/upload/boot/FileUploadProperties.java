package com.whistle.code.file.upload.boot;

import com.whistle.code.file.upload.enums.ProcessType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lin
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(FileUploadProperties.FILE_UPLOAD_PREFIX)
public class FileUploadProperties {
    public static final String FILE_UPLOAD_PREFIX = "loader";
    public static final String FILE_UPLOAD_POOL = "uploadExecutor";
    /**
     * 临时文件夹位置 (temp file location (dir))
     * 如果为空则使用 spring.servlet.multipart.location
     * 还是空则使用系统临时目录
     */
    private String tempFilePath;
    /**
     * 文件处理类型 ()
     */
    private ProcessType processType =ProcessType.DEFAULT;
    /**
     * 默认分片大小(单位/MB)
     */
    private int defaultChunkSize = 10;
    /**
     * 合并完成后删除临时文件夹
     */
    private boolean deleteTempFile = true;
    /**
     * 是否开启自动合并
     */
    private boolean enableAutoMerge = true;
    /**
     * 是否开启零拷贝合并
     */
    private boolean enableZeroMerge = true;
    /**
     * 核心线程池大小
     */
    private Integer coreSize=2;

    /**
     * 最大线程（默认核心线程数*2）
     */
    private Integer maxSize=coreSize*2;
    /**
     * 队列长度
     */
    private Integer queueCapacity = 200;
    /**
     * 活跃时间
     */
    private Integer keepAlive=60;

}
