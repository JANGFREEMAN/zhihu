package com.zhihu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by B160 on 2016/5/20.
 */
public class PropertiesUtil {

    /**
     * 根据文件名和键获取值
     * @param file 文件名
     * @param key 键
     * @return
     */
    public static String getValue(String file,String key){
        InputStream in = PropertiesUtil.class.getResourceAsStream("/"+file);
        Properties pro = new Properties();
        try {
            pro.load(in);
        } catch (IOException e) {
           return "";
        }
        return pro.getProperty(key);
    }
}
