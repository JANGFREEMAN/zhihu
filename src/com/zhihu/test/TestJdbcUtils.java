package com.zhihu.test;

import com.zhihu.util.JdbcUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by B160 on 2016/5/20.
 */
public class TestJdbcUtils {

    @Test
    public void teustSaveUser() throws IOException {
        JdbcUtils.saveUsers();
    }
}
