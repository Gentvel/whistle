package com.whistle.code.file.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.whistle.code.file.upload.bean.ChunkUploaded;
import com.whistle.code.file.upload.bean.FileUploadContext;
import com.whistle.code.file.upload.bean.UploadResult;
import com.whistle.code.file.upload.boot.FileUploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;
import org.springframework.util.backoff.FixedBackOff;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public abstract class AbstractUploadProcessor implements UploadProcessor {

    private final UploadHandler uploadHandler;

    protected FileUploadProperties properties = SpringUtil.getBean(FileUploadProperties.class);

    private static final String TEMP_FILE_SUFFIX = ".tmp";

    /**
     * 0-未上传，1-分片已上传，2-分片上传失败，3-分片上传完成，4-分片已上传部分
     * 5-文件合并失败，6-文件校验失败，7-文件已处理完成，9-文件处理失败
     */
    public static final Integer UNLOAD = 0;
    public static final Integer CHUNK_UPLOADED = 1;
    public static final Integer CHUNK_ERROR = 2;
    public static final Integer CHUNK_SUCCESS = 3;
    public static final Integer CHUNK_PART = 4;
    public static final Integer FILE_MERGE_ERROR = 5;
    public static final Integer FILE_CHECK_ERROR = 6;
    public static final Integer FILE_SUCCESS = 7;
    public static final Integer FILE_ERROR = 9;


    public AbstractUploadProcessor(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

    /**
     * 前置处理
     */
    private void beforeUpload(FileUploadContext context) {
        //校验分片临时文件夹
        File dir = new File(context.getTempChunkFileDir());
        if (dir.isFile()) {
            FileUtil.del(dir);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (log.isDebugEnabled()) {
            log.debug("当前文件[{}]分片[{}]号文件不存在，创建中......", context.getMergedFile(), context.getChunkNumber());
        }
        context.setTempChunkFile(FileUtil.file(context.getTempChunkFileName()));
    }


    @Override
    public UploadResult preQuery(FileUploadContext context) {
        initContext(context);
        UploadResult uploadResult = context.getUploadResult();
        //文件已上传，并合并校验通过
        if (FileUtil.exist(context.getMergedFile()) && mergedFileCheck(context)) {
            uploadResult.setSkipUpload(true);
            log.info("[{}]该文件已上传完成", context.getFilename());
            uploadResult.setStatus(FILE_SUCCESS);
            return uploadResult;
        }
        //预查询阶段
        List<ChunkUploaded> uploadeds = uploadHandler.preCheckUpload(context);
        long tempsSize = 0L;
        if (uploadeds != null && uploadeds.size() > 0) {
            tempsSize = uploadeds.stream().mapToLong(ChunkUploaded::getLoadedSize).sum();
            uploadResult.setStatus(CHUNK_PART);
            if (tempsSize == context.getTotalSize()) {
                log.info("预查询阶段：文件[{}]分片文件上传检测完毕，所有分片上传完成", context.getFilename());
                uploadResult.setSkipUpload(true);
                uploadResult.setNeedMerge(true);
                uploadResult.setStatus(CHUNK_SUCCESS);
            }
            uploadResult.setUploaded(uploadeds);
            return uploadResult;

        }
        //分片文件夹不存在
        if (!FileUtil.exist(context.getTempChunkFileDir())) {
            uploadResult.setUploaded(uploadeds);
            uploadResult.setStatus(UNLOAD);
            return uploadResult;
        }
        //是否已存在分片文件
        List<String> strings = FileUtil.listFileNames(context.getTempChunkFileDir());
        strings.sort(Comparator.comparingInt(o -> Integer.parseInt(FileUtil.getPrefix(o))));
        uploadeds = new ArrayList<>(strings.size());
        //未上传任何分片的情况
        if (strings.size() == 0) {
            log.info("预查询阶段：文件[{}]尚未上传...", context.getFilename());
            uploadResult.setUploaded(uploadeds);
            uploadResult.setStatus(UNLOAD);
            return uploadResult;
        }
        //分片上传超过的情况
        if (strings.size() > context.getTotalChunks()) {
            log.warn("预查询阶段：当前分片文件数大于实际分片数，正在清除所有文件....");
            FileUtil.del(context.getTempChunkFileDir());
            uploadResult.setUploaded(uploadeds);
            uploadResult.setStatus(UNLOAD);
            return uploadResult;
        }
        //轮询文件名
        for (String string : strings) {
            long length = FileUtil.file(context.getTempChunkFileDir() + File.separator + string).length();
            if (length == 0) {
                continue;
            }
            String index = FileUtil.getPrefix(string);
            //String fileUUID = uploadHandler.getUUID(context);
            ChunkUploaded chunkUploaded = new ChunkUploaded();
            chunkUploaded.setChunkNumber(Integer.parseInt(index));
            chunkUploaded.setLoadedSize(length);
            uploadeds.add(chunkUploaded);
            tempsSize += length;
        }
        uploadResult.setStatus(CHUNK_PART);
        if (tempsSize == context.getTotalSize()) {
            log.debug("预查询阶段：文件[{}]分片文件上传检测完毕，所有分片上传完成", context.getFilename());
            uploadResult.setSkipUpload(true);
            uploadResult.setNeedMerge(true);
            uploadResult.setStatus(CHUNK_SUCCESS);
        }
        uploadResult.setUploaded(uploadeds);
        return uploadResult;
    }

    @Override
    public UploadResult process(FileUploadContext context) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean mergedFlag = false;
        initContext(context);
        beforeUpload(context);
        UploadResult uploadResult = context.getUploadResult();
        uploadHandler.beforeProcess(context);
        //文件处理完成
        if (doProcess(context)) {
            uploadHandler.afterProcess(context);
            uploadResult.setStatus(CHUNK_UPLOADED);
            //当前分片为最后分片
            if (Objects.equals(context.getTotalChunks(), context.getChunkNumber())) {
                uploadResult.setNeedMerge(true);
                uploadResult.setStatus(CHUNK_SUCCESS);
                UploadResult result = preQuery(context);
                if (result.getUploaded().size() == context.getTotalSize()) {
                    mergedFlag = true;
                } else {
                    BackOff backOff = new FixedBackOff(2000L, 3);
                    BackOffExecution execution = backOff.start();
                    for (int i = 0; i < 3; i++) {
                        if(execution.nextBackOff()==BackOffExecution.STOP){
                            break;
                        }else{
                            List<ChunkUploaded> uploaded = preQuery(context).getUploaded();
                            if(uploaded.size()== context.getTotalSize()){
                                mergedFlag=true;
                                break;
                            }
                        }
                    }
                }
                if (mergedFlag && properties.isEnableAutoMerge() && merged(context)) {
                    uploadResult.setStatus(FILE_SUCCESS);
                    return uploadResult;
                }
            }
            return uploadResult;
        }
        uploadResult.setStatus(CHUNK_ERROR);
        return uploadResult;
    }

    /**
     * 初始化FileContext
     */
    private void initContext(FileUploadContext context) {
        context.getUploadResult().setIdentifier(context.getIdentifier());
        context.setMergedFile(mergeFileName(context));
        context.setTempChunkFileDir(tempDirectory(context));
        context.setTempChunkFileName(tempFileName(context));
        context.getUploadResult().setChunkNumber(context.getChunkNumber());
    }


    protected abstract boolean doProcess(FileUploadContext context);


    private boolean merged(FileUploadContext context) {
        UploadResult result = merge(context);
        return result.getStatus() == FILE_SUCCESS;
    }


    /**
     * 文件合并
     */
    @Override
    public UploadResult merge(FileUploadContext context) {
        initContext(context);
        UploadResult uploadResult = context.getUploadResult();
        uploadHandler.beforeMerge(context);
        String[] list = new File(context.getTempChunkFileDir()).list();
        Arrays.sort(list);
        //临时文件总大小
        long tempsTotalSize = 0L;
        if (properties.isEnableZeroMerge()) {
            if (log.isDebugEnabled()) {
                log.debug("使用channel零拷贝中.....合并文件为:{}", context.getFilename());
            }
            try (FileChannel output = new FileOutputStream(context.getMergedFile()).getChannel()) {
                for (String temp : list) {
                    FileInputStream fileInputStream = new FileInputStream(context.getTempChunkFileDir() + File.separator + temp);
                    FileChannel input = fileInputStream.getChannel();
                    output.transferFrom(input, output.size(), input.size());
                    //output.force(false);
                    input.close();
                    fileInputStream.close();
                }
                output.force(true);
                tempsTotalSize = output.size();
            } catch (IOException e) {
                log.error("", e);
                uploadResult.setStatus(FILE_MERGE_ERROR);
                return uploadResult;
            }

        } else {
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(context.getMergedFile()))) {
                byte[] tempBuffer = new byte[1024 * 1024 * 5];
                int readLength;
                for (String temp : list) {
                    File file = FileUtil.file(context.getTempChunkFileDir() + File.separator + temp);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    while ((readLength = bufferedInputStream.read(tempBuffer)) != -1) {
                        bufferedOutputStream.write(tempBuffer, 0, readLength);
                    }
                    bufferedOutputStream.flush();
                    bufferedInputStream.close();
                    tempsTotalSize += file.length();
                }

            } catch (IOException e) {
                log.error("", e);
                uploadResult.setStatus(FILE_MERGE_ERROR);
                return uploadResult;
            }
        }
        if (tempsTotalSize != context.getTotalSize()) {
            log.info("合并文件失败，与源文件大小不符，删除合并文件中....");
            FileUtil.del(context.getMergedFile());
            uploadResult.setStatus(FILE_CHECK_ERROR);
        }

        if (properties.isDeleteTempFile()) {
            FileUtil.del(context.getTempChunkFileDir());
        }
        uploadResult.setStatus(FILE_SUCCESS);
        if (!mergedFileCheck(context)) {
            uploadResult.setStatus(FILE_CHECK_ERROR);
        }
        uploadHandler.afterMerge(context);
        return uploadResult;
    }

    /**
     * 生成MD5临时文件夹
     */
    private String tempDirectory(FileUploadContext context) {
        StringBuilder sb = new StringBuilder();
        String tempFilePath = properties.getTempFilePath();
        if (StrUtil.isBlank(tempFilePath)) {
            String springLocation = SpringUtil.getProperty("spring.servlet.multipart.location");
            if (StrUtil.isBlank(springLocation)) {
                tempFilePath = FileUtil.getTmpDirPath() + File.separator + FileUploadProperties.FILE_UPLOAD_PREFIX;
                log.info("使用系统临时文件夹:{}" + tempFilePath);
            } else {
                tempFilePath = springLocation + File.separator + FileUploadProperties.FILE_UPLOAD_PREFIX;
                log.info("使用SpringBoot目录:{}" + tempFilePath);
            }
        }
        if (tempFilePath.endsWith(File.separator)) {
            sb.append(tempFilePath).append(context.getIdentifier());
        }
        sb.append(tempFilePath).append(File.separator).append(context.getIdentifier());
        return sb.toString();
    }


    /**
     * 分片文件临时存储文件名
     */
    private String tempFileName(FileUploadContext context) {
        return tempDirectory(context) + File.separator + context.getChunkNumber() + TEMP_FILE_SUFFIX;
    }

    /**
     * 合并文件名
     */
    private String mergeFileName(FileUploadContext context) {
        return properties.getTempFilePath().endsWith(File.separator) ? properties.getTempFilePath() : properties.getTempFilePath() + File.separator + context.getFilename();
    }

    /**
     * 合并文件校验
     */
    private boolean mergedFileCheck(FileUploadContext context) {
        File file = FileUtil.file(context.getMergedFile());
        if (file.length() != context.getTotalSize()) {
            log.warn("合并文件校验失败，与源文件大小不符，删除合并文件中....");
            FileUtil.del(file);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("校验文件[{}]大小成功", context.getFilename());
        }
        String md5 = SecureUtil.md5(file);
        if (!md5.equals(context.getIdentifier())) {
            log.warn("文件MD5校验失败，正在删除[{}]相关所有文件...", context.getFilename());
            FileUtil.del(context.getTempChunkFileDir());
            FileUtil.del(file);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("校验文件[{}]MD5成功！", context.getFilename());
        }
        return true;
    }

}
