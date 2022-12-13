package com.whistle.starter;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.DateUnit;
import com.whistle.starter.boot.WhistleWebProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gentvel
 */
public class WhistleConstants {

    private static final WeakCache<String, Map<String,Object>> WEAK_CACHE =CacheUtil.newWeakCache(DateUnit.SECOND.getMillis() * 10);

    static{
        //knife4j start
        Map<String,Object> knife4jDisable = new HashMap<>(1){
            {
                put("knife4j.enable",false);
                put(WhistleWebProperties.PREFIX+".enable-knife4j",false);
            }
        };
        Map<String,Object> knife4jEnable = new HashMap<>(1){
            {
                put("knife4j.enable",true);
                //是否开启生产环境保护策略
                //put("knife4j.production",true);
                put("knife4j.setting.enableFooter",false);
                //是否开启自定义Footer
                put("knife4j.setting.enableFooterCustom",true);
                //是否显示界面中SwaggerModel功能
                put("knife4j.setting.enableSwaggerModels",false);
                //是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点
                put("knife4j.setting.enableVersion",true);
                //针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
                put("knife4j.setting.enableFilterMultipartApis",true);
                put("knife4j.setting.footerCustomContent","Apache License 2.0 | Copyright  2019-NOW whistle(https://gentvel.github.io/whistle/) knife4j(https://gitee.com/xiaoym/knife4j)");
            }
        };
        WEAK_CACHE.put("knife4jDisable",knife4jDisable);
        WEAK_CACHE.put("knife4jEnable",knife4jEnable);
        //knife4j end
        WEAK_CACHE.put("empty", new HashMap<>(0));
    }

    public static Map<String,Object> knife4jDisable(){
        return WEAK_CACHE.get("knife4jDisable");
    }

    public static Map<String,Object> knife4jEnable(){
        return WEAK_CACHE.get("knife4jEnable");
    }

    public static WeakCache<String, Map<String,Object>> getWeakCache(){
        return WEAK_CACHE;
    }

    public static Map<String,Object> empty(){
        return WEAK_CACHE.get("empty");
    }

}
