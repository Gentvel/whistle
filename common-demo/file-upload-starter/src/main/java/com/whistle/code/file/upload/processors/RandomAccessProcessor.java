package com.whistle.code.file.upload.processors;

import com.whistle.code.file.upload.AbstractUploadProcessor;
import com.whistle.code.file.upload.UploadHandler;
import com.whistle.code.file.upload.bean.FileUploadContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class RandomAccessProcessor extends AbstractUploadProcessor {


    public RandomAccessProcessor(UploadHandler uploadHandler) {
        super(uploadHandler);
    }

    @Override
    protected boolean doProcess(FileUploadContext context) {
        try (RandomAccessFile accessTmpFile = new RandomAccessFile(context.getTempChunkFile(),"rw")){
            accessTmpFile.seek(0);
            accessTmpFile.write(context.getFile().getBytes());
        } catch (IOException e) {
            log.error("",e);
            return false;
        }
        return true;
    }
}
