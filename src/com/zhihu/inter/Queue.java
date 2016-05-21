package com.zhihu.inter;

import com.zhihu.util.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public abstract class Queue {

    private Jedis jedis  = JedisUtils.getInstance();

    private  final String KEY = getType();

    /**
     * 入队列
     * @param user
     */
    public synchronized void enQueue(String user){
        jedis.rpush(KEY, user);
    }

    /**
     * 出队列
     * @author zhangyx
     * @return
     */
    public synchronized String deQueue(){
        return jedis.lpop(KEY);
    }

    /**
     * 根据下表取值
     * @return
     */
    public synchronized String get(int index){
        return jedis.lindex(KEY, index);
    }


    /**
     * 判断队列中是否含有user
     * @author zhangyx
     * @param user
     * @return
     */
    public synchronized boolean contains(String user){
        List<String> list = jedis.lrange(KEY, 0, -1);
        for(String  str : list){
            if(str.equalsIgnoreCase(user)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断队列是否为空
     * @author zhangyx
     * @return
     */
    public synchronized boolean isEmpty(){
        List<String> list = jedis.lrange(KEY, 0, -1);
        return list.size() == 0 ? true : false;
    }

    /**
     * 返回队列长度
     * @author zhangyx
     * @return
     */
    public synchronized int size(){
        List<String> list = jedis.lrange(KEY, 0, -1);
        return list.size();
    }

    /**
     * 清空队列
     */
    public synchronized void empty(){
        jedis.del(KEY);
    }

    /**
     * 设置队列
     * @return
     */
    public abstract  String getType();
}