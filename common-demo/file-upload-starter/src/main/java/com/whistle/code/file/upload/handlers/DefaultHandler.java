package com.whistle.code.file.upload.handlers;

import com.whistle.code.file.upload.UploadHandler;
import com.whistle.code.file.upload.bean.ChunkUploaded;
import com.whistle.code.file.upload.bean.FileUploadContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
public class DefaultHandler implements UploadHandler {

    @Override
    public List<ChunkUploaded> preCheckUpload(FileUploadContext context) {
        return new ArrayList<>();
    }

    @Override
    public String getUUID(FileUploadContext context) {
        return null;
    }

    @Override
    public void beforeProcess(FileUploadContext context) {

    }

    @Override
    public void afterProcess(FileUploadContext context) {

    }

    @Override
    public void beforeMerge(FileUploadContext context) {

    }

    @Override
    public void afterMerge(FileUploadContext context) {

    }
}
