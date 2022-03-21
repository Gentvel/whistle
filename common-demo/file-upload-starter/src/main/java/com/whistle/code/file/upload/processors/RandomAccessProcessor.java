package com.whistle.code.file.upload.processors;

import com.whistle.code.file.upload.AbstractUploadProcessor;
import com.whistle.code.file.upload.UploadHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;

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
    protected boolean doProcess() {
        try (RandomAccessFile accessTmpFile = new RandomAccessFile(context.getTempChunkFile(),"rw")){
//            long chunkSize = file.getChunkSize()==0L ? (long) properties.getDefaultChunkSize() * 1024 * 1024 : file.getChunkSize();
//            long offset = chunkSize*file.getChunkNumber();
            accessTmpFile.seek(0);
            if(context.getFile()==null){
                log.info("文件未生成成功.....{}",context.toString());
            }
            accessTmpFile.write(context.getFile().getBytes());
        } catch (IOException e) {
            log.error("",e);
            return false;
        }
        return true;
    }
}
