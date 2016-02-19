package com.zhihu.util;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * ����һ�����ж���
 * @author zhangyx
 *
 */
public class Queue {

	private Jedis jedis  = JedisUtils.getInstance();

	private String KEY ;
	
	
	public Queue(String type){
		this.KEY = type;
	}
	
	
	/**
	 * �����
	 * @param user
	 */
	public void enQueue(String user){
		jedis.rpush(KEY, user);
	}
	
	/**
	 * ������
	 * @author zhangyx
	 * @return 
	 */
	public String deQueue(){
		return jedis.lpop(KEY);
	}
	
	/**
	 * �����±�ȡֵ
	 * @return
	 */
	public String get(int index){
		return jedis.lindex(KEY, index);
	}
	
	
	/**
	 * �ж϶������Ƿ���user
	 * @author zhangyx
	 * @param user
	 * @return
	 */
	public boolean contains(String user){
		List<String> list = jedis.lrange(KEY, 0, -1);
		for(String  str : list){
			if(str.equalsIgnoreCase(user)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �ж϶����Ƿ�Ϊ��
	 * @author zhangyx
	 * @return
	 */
	public boolean isEmpty(){
		List<String> list = jedis.lrange(KEY, 0, -1);
		return list.size() == 0 ? true : false;
	}
	
	/**
	 * ���ض��г���
	 * @author zhangyx
	 * @return
	 */
	public int size(){
		List<String> list = jedis.lrange(KEY, 0, -1);
		return list.size();
	}
	
}
