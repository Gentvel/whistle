package com.whistle.code.simulate.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
@Component
public class FileReading {
    private static final String personBytePath = "D:\\Projects\\simulated_data\\person\\person.data";
    private static final String posterBytePath = "D:\\Projects\\simulated_data\\person\\poster.data";
    private static final String replyBytePath = "D:\\Projects\\simulated_data\\person\\reply.data";

    private static List<String> fileList = new ArrayList<String>();

    static{
        fileList.add(personBytePath);
        fileList.add(posterBytePath);
        fileList.add(replyBytePath);
    }

    private static final long _1G = 1024*1024*1000;
    private static final long _500M = 1024*1024*500;
    private static final long _30M = 1024*1024*30;

    public void read() throws IOException {
        File file = Paths.get(personBytePath).toFile();
        if(file.exists()&&file.isFile()){
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel channel = fileInputStream.getChannel();
            log.info("文件大小为：{} mb",channel.size()/1024/1024);
            if(channel.size()<_30M){
                log.debug("当前文件[{}]小于30M,使用BufferedInputStream....",file.getName());
            }else if(channel.size()<_500M){
                log.debug("当前文件[{}]小于500M,使用RandomAccessFile....",file.getName());
            }else if(channel.size()<_1G){
                log.debug("当前文件[{}]小于1G,使用文件分割处理....",file.getName());
            }else{
                log.debug("当前文件[{}]大于于1G,使用MappedByteBuffer....",file.getName());
            }
        }
    }





}
