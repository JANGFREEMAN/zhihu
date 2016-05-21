package com.zhihu.test;

import com.zhihu.util.HttpUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by B160 on 2016/5/20.
 */
public class TestHttpUtils {

    @Test
    public void testGetHtml(){
        try {
            String html = HttpUtils.getHtml("https://www.zhihu.com/people/edit");
            System.out.println(html);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
