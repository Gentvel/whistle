package com.whistle.code.file.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.whistle.code.file.upload.bean.ChunkUploaded;
import com.whistle.code.file.upload.bean.FileUploadContext;
import com.whistle.code.file.upload.bean.UploadResult;
import com.whistle.code.file.upload.boot.FileUploadProperties;
import com.whistle.code.file.upload.exceptions.UploaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public abstract class AbstractUploadProcessor implements UploadProcessor {

    private final UploadHandler uploadHandler;

    protected FileUploadProperties properties = SpringUtil.getBean(FileUploadProperties.class);

    private static final String TEMP_FILE_SUFFIX = ".tmp";

    protected FileUploadContext context;

    public AbstractUploadProcessor(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

    /**
     * 前置处理
     */
    private void preProcess() {
        //文件已上传，并校验通过
        if (FileUtil.exist(context.getMergedFile()) && context.isPre()) {
            if (mergedFileCheck()) {
                UploadResult uploadResult = context.getUploadResult();
                uploadResult.setSkipUpload(true);
                log.debug("[{}]该文件已上传...", context.getFilename());
                return;
            }
        }
        //校验分片临时文件夹
        File dir = new File(context.getTempChunkFileDir());
        if (dir.isFile()) {
            FileUtil.del(dir);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
            //预查询时
            log.debug("预查询阶段：判断文件[{}]分片上传情况",context.getFilename());
            if (context.isPre() && properties.isEnableTestChunk()) {
                List<ChunkUploaded> uploadeds = uploadHandler.preCheckUpload(context);
                UploadResult uploadResult = context.getUploadResult();
                long tempsSize = 0L;
                if (uploadeds != null && uploadeds.size() > 0) {
                    tempsSize = uploadeds.stream().mapToLong(ChunkUploaded::getLoadedSize).sum();
                } else {
                    log.debug("预查询阶段：文件[{}]尚未上传完毕...轮询文件夹获取分片文件数据中",context.getFilename());
                    //是否已存在分片文件
                    List<String> strings = FileUtil.listFileNames(context.getTempChunkFileDir());
                    strings.sort(Comparator.comparingInt(o -> Integer.parseInt(FileUtil.getPrefix(o))));
                    uploadeds = new ArrayList<>(strings.size());
                    if (strings.size() > 0) {
                        if(strings.size()>context.getTotalChunks()){
                            log.warn("预查询阶段：当前分片文件数大于实际分片数，正在清除所有文件....");
                            FileUtil.del(context.getTempChunkFileDir());
                        }
                        for (String string : strings) {
                            long length = new File(context.getTempChunkFileDir() + File.separator + string).length();
                            String index = FileUtil.getPrefix(string);
                            if(length==context.getChunkSize()){
                                String fileUUID = uploadHandler.getUUID(context);
                                ChunkUploaded chunkUploaded = new ChunkUploaded();
                                chunkUploaded.setChunkNumber(Integer.parseInt(index));
                                chunkUploaded.setLoadedSize(length);
                                chunkUploaded.setLoadedUID(fileUUID);
                                uploadeds.add(chunkUploaded);
                                tempsSize += length;
                            }
                        }
                    }
                }

                if (tempsSize == context.getTotalSize()) {
                    log.debug("预查询阶段：文件[{}]分片文件上传检测完毕，所有分片上传完成",context.getFilename());
                    uploadResult.setSkipUpload(true);
                    context.setNeedMerge(true);
                }
                uploadResult.setUploaded(uploadeds);
                return;
            }
        }
        context.setTempChunkFile(FileUtil.file(context.getTempChunkFileName()));
    }


    @Override
    public UploadResult process(FileUploadContext file) {
        initContext(file);
        preProcess();
        UploadResult uploadResult = context.getUploadResult();
        uploadResult.setLoadedUID(uploadHandler.getUUID(context));
        if (uploadResult.isSkipUpload() || context.isPre()) {
            //开启自动合并
            uploadHandler.beforeMerge(context);
            if (context.isNeedMerge() && properties.isEnableAutoMerge() && merge()) {
                uploadHandler.afterMerge(context);
                if (properties.isEnableAutoCheck() && mergedFileCheck()) {
                    //TODO 合并后状态处理以及校验状态处理
                    return uploadResult;
                }
                return uploadResult;
            }
            return uploadResult;
        }
        uploadHandler.beforeProcess(context);
        //文件处理完成
        if (doProcess()) {
            //context.setFile(null);
            uploadHandler.afterProcess(context);
            uploadResult.setStatus(1);
            //当前分片为最后分片
            if (Objects.equals(context.getTotalChunks(), context.getChunkNumber())) {
                //开启自动合并
                uploadHandler.beforeMerge(context);
                if (properties.isEnableAutoMerge() && merge()) {
                    uploadHandler.afterMerge(context);
                    if (properties.isEnableAutoCheck() && mergedFileCheck()) {
                        //TODO 合并后状态处理以及校验状态处理
                        return uploadResult;
                    }
                    return uploadResult;
                }
            }
        }
        return context.getUploadResult();

    }

    /**
     * 初始化FileContext
     */
    private void initContext(FileUploadContext fileUploadContext) {
        this.context = fileUploadContext;
        context.getUploadResult().setIdentifier(context.getIdentifier());
        context.setMergedFile(mergeFileName());
        context.setTempChunkFileDir(tempDirectory());
        context.setTempChunkFileName(tempFileName());
    }


    protected abstract boolean doProcess();


    /**
     * 文件合并
     */
    private boolean merge() {
        String[] list = new File(context.getTempChunkFileDir()).list();
        Arrays.sort(list);
        long tempsSize = 0L;
        if (properties.isEnableZeroMerge()) {
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
                tempsSize = output.size();
            } catch (IOException e) {
                log.error("", e);
                throw new UploaderException(e.getMessage());
            }

        } else {
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(context.getMergedFile()))) {
                byte[] tempBuffer = new byte[1024 * 1024 * 3];
                int readLength;
                for (String temp : list) {
                    File file = FileUtil.file(context.getTempChunkFileDir() + File.separator + temp);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    while ((readLength = bufferedInputStream.read(tempBuffer)) != -1) {
                        bufferedOutputStream.write(tempBuffer, 0, readLength);
                    }
                    bufferedOutputStream.flush();
                    bufferedInputStream.close();
                    tempsSize += file.length();
                }

            } catch (IOException e) {
                log.error("", e);
                throw new UploaderException(e.getMessage());
            }
        }
        if (tempsSize != context.getTotalSize()) {
            log.info("合并文件失败，与源文件大小不符，删除合并文件中....");
            return false;
        }
        if (properties.isDeleteTempFile()) {
            FileUtil.del(context.getTempChunkFileDir());
        }
        uploadHandler.afterMerge(context);
        return true;
    }

    /**
     * 生成MD5临时文件夹
     */
    private String tempDirectory() {
        StringBuilder sb = new StringBuilder();
        String tempFilePath = properties.getTempFilePath();
        if (StrUtil.isBlank(tempFilePath)) {
            String springLocation = SpringUtil.getProperty("spring.servlet.multipart.location");
            if (StrUtil.isBlank(springLocation)) {
                tempFilePath = FileUtil.getTmpDirPath();
            } else {
                tempFilePath = springLocation;
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
    private  String tempFileName() {
        return tempDirectory() + File.separator + context.getChunkNumber()  + TEMP_FILE_SUFFIX;
    }

    /**
     * 合并文件名
     */
    private String mergeFileName() {
        return properties.getTempFilePath().endsWith(File.separator) ? properties.getTempFilePath() : properties.getTempFilePath() + File.separator + context.getFilename();
    }

    private boolean mergedFileCheck() {
        File file = FileUtil.file(context.getMergedFile());
        if (file.length() != context.getTotalSize()) {
            log.warn("合并文件校验失败，与源文件大小不符，删除合并文件中....");
            FileUtil.del(file);
            return false;
        }
        String md5 = SecureUtil.md5(file);
        if (!md5.equals(context.getIdentifier())) {
            //TODO 删除还是其他操作？
            log.warn ("文件MD5校验失败，正在删除[{}]相关所有文件...", context.getFilename());
            FileUtil.del(context.getTempChunkFileDir());
            FileUtil.del(file);
            return false;
        }

        return true;
    }

}
