package com.zhihu.test;

import com.zhihu.inter.Queue;
import com.zhihu.inter.impl.UserQueue;
import com.zhihu.model.User;
import com.zhihu.util.JedisUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by B160 on 2016/5/20.
 */
public class TestRedisUtils {

    @Test
    public void testRedis(){
        Jedis jedis = JedisUtils.getInstance();
        jedis.rpush("test","1");
        jedis.rpush("test","2");
        jedis.rpush("test","3");
        jedis.rpush("test","4");
        jedis.rpush("test","5");
        jedis.rpush("test","6");
        System.out.println(jedis.lindex("test",0));
    }

    @Test
    public void testDel(){
        Jedis jedis = JedisUtils.getInstance();
        jedis.del("test");
    }

    @Test
    public void testDelQueue(){
        Queue queue = UserQueue.getInstance();
        queue.empty();
    }

    @Test
    public void testSaveUsr(){
        User user = new User();
        user.setCompany("sdf");
        Jedis jedis = JedisUtils.getInstance();
//        jedis.lpush
    }

    @Test
    public void testHash(){
        Jedis jedis = JedisUtils.getInstance();
//        jedis.set
    }

}
