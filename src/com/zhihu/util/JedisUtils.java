package com.zhihu.util;

import redis.clients.jedis.Jedis;

/**
 * Jedis工具类
 * @author zhangyx
 *
 */
public class JedisUtils {
	private static Jedis jedis = null;
	
	private static String HOST = "localhost";
	
	private static int PORT = 6379;
	
	/**
	 * 获取jedis单例
	 * @author zhangyx
	 */
	public static  Jedis getInstance(){
		if(jedis == null){
			synchronized (JedisUtils.class) {
				if(jedis == null){
					jedis = new Jedis(HOST,PORT);
				}
				return jedis;
			}
		}
		return jedis;
	}
}
