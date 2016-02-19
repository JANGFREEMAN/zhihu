package com.zhihu.util;

import redis.clients.jedis.Jedis;

/**
 * Jedis������
 * @author zhangyx
 *
 */
public class JedisUtils {
	private static Jedis jedis = null;
	
	private static String HOST = "localhost";
	
	private static int PORT = 6379;
	
	/**
	 * ��ȡjedis����
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
