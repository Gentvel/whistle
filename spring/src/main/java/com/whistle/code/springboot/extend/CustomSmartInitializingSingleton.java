package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * 这个接口中只有一个方法afterSingletonsInstantiated，其作用是是 在spring容器管理的所有单例对象（非懒加载对象）初始化完成之后调用的回调接口。
 * 其触发时机为postProcessAfterInitialization之后。
 * @author Gentvel
 * @version 1.0.0
 */
@Component
public class CustomSmartInitializingSingleton implements SmartInitializingSingleton {
    Logger logger = LoggerFactory.getLogger(CustomSmartInitializingSingleton.class);
    @Override
    public void afterSingletonsInstantiated() {
        logger.info("用户可以扩展此接口在对所有单例对象初始化完毕后，做一些后置的业务处理。");
    }
}
