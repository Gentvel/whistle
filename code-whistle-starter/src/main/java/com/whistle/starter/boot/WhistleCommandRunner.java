package com.whistle.starter.boot;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;

/**
 * @author Gentvel
 */
@Slf4j
@Component
public class WhistleCommandRunner implements CommandLineRunner {
    @Resource
    private WhistleWebProperties whistleWebProperties;


    private static String localAddress;


    @Override
    public void run(String... args) throws Exception {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = SpringUtil.getProperty("server.port");
        if(StrUtil.isBlank(port)){
            port="8080";
        }
        String path = SpringUtil.getProperty("server.servlet.context-path");
        if(StrUtil.isBlank(path)){
            path="";
        }
        localAddress=String.format("http://%s:%s%s",ip, port, path);
        if (whistleWebProperties.isEnableKnife4J()) {
            log.info("Application is running! Access address:\n\tLocal:\t\thttp://localhost:{}{}\n\tExternal:\thttp://{}:{}{}\n\tDocument:\thttp://localhost:{}{}/doc.html", port, path ,ip, port, path, port, path);
        }else{
            log.info("Application is running! Access address:\n\tLocal:\t\thttp://localhost:{}{}\n\tExternal:\thttp://{}:{}{}", port, path ,ip, port, path);
        }
    }


    public static String  getLocalAddress(){
        return localAddress;
    }
}
