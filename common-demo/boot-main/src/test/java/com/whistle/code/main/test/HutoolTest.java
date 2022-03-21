package com.whistle.code.main.test;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class HutoolTest {
    @Test
    public void testStr(){
        String s ="02223444111";
        log.info(StrUtil.subPre(s,1));
    }
}
