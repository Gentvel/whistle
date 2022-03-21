package com.whistle.code.main.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class MD5Test {
    @Test
    public void test(){
        File file = FileUtil.file("D:\\temps\\图表配置2-饼图地图.mp4");
        String s = SecureUtil.md5(file);
        log.info(s);
        //HMac hmacMd5 = SecureUtil.hmacMd5("c16ba8277e8a953bdd23a01e4f8f7300");

    }

}
