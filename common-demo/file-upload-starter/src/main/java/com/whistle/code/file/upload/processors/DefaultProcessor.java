package com.whistle.code.file.upload.processors;

import com.whistle.code.file.upload.AbstractUploadProcessor;
import com.whistle.code.file.upload.UploadHandler;
import com.whistle.code.file.upload.exceptions.UploaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class DefaultProcessor extends AbstractUploadProcessor {

    public DefaultProcessor(UploadHandler uploadHandler) {
        super(uploadHandler);
    }

    @Override
    protected boolean doProcess() {
        MultipartFile file = context.getFile();
        try {
            file.transferTo(context.getTempChunkFile());
        } catch (IOException e) {
            log.error("", e);
            throw new UploaderException(e.getMessage());
        }
        return true;
    }
}
