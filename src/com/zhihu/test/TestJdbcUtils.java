package com.zhihu.test;

import com.zhihu.model.User;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.ParseHtmlUtil;
import com.zhihu.util.PropertiesUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by B160 on 2016/5/20.
 */
public class TestJdbcUtils {

    @Test
    public void teustSaveUser() throws IOException {

        long startTime = System.currentTimeMillis();
        User user = ParseHtmlUtil.crawlerUser(PropertiesUtil.getValue("config.properties","homeUrl"));
        JdbcUtils.saveUser(user);
        System.out.println(System.currentTimeMillis() - startTime);
    }

}
