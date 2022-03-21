package com.whistle.code.file.upload.processors;

import com.whistle.code.file.upload.AbstractUploadProcessor;
import com.whistle.code.file.upload.UploadHandler;
import com.whistle.code.file.upload.exceptions.UploaderException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class BufferedStreamProcessor extends AbstractUploadProcessor {

    public BufferedStreamProcessor(UploadHandler uploadHandler) {
        super(uploadHandler);
    }

    @Override
    protected boolean doProcess() {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(context.getFile().getInputStream())) {
            byte[] bytesByte = new byte[1024 * 1024 * 5];
            int len ;
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(context.getTempChunkFile()));
            while ((len = bufferedInputStream.read(bytesByte)) != -1) {
                bufferedOutputStream.write(bytesByte, 0, len);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            log.error("", e);
            throw new UploaderException(e.getMessage());
        }
        return false;
    }
}
