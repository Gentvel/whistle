package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 *这个扩展点也只有一个方法：destroy()，其触发时机为当此对象销毁时，会自动执行这个方法。
 * 比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
 * @author Gentvel
 * @version 1.0.0
 */
@Component
public class CustomDisposableBean implements DisposableBean {
    Logger logger = LoggerFactory.getLogger(CustomDisposableBean.class);

    @Override
    public void destroy() throws Exception {
        logger.info("其触发时机为当此对象销毁时，会自动执行这个方法。");
    }
}
