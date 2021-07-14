package com.we.javaapi.util;

import java.util.ResourceBundle;

/**
 * 配置文件读取工具类
 * @author we
 * @date 2021-07-13 11:39
 **/
public class ResourceUtil {
    private static final ResourceBundle resourceBundle;

    static{
        resourceBundle = ResourceBundle.getBundle("application");
    }

    public static String getKey(String key){
        return resourceBundle.getString(key);
    }
}
