package com.whistle.code.file.upload.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Lin
 * @version 1.0.0
 */
@Data
public class ChunkUploaded {
    /**
     * 已上传分片索引
     */
    private int chunkNumber;
    /**
     * 已上传分片size
     */
    @JsonIgnore
    private long loadedSize;

}
