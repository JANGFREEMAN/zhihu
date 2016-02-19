package com.zhihu.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zhihu.model.User;
import com.zhihu.util.CrawlerUtils;
import com.zhihu.util.HttpUtils;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.JedisUtils;
import com.zhihu.util.Queue;
import com.zhihu.util.SerializeUtil;

import redis.clients.jedis.Jedis;

public class TestHtppUtils {

	
	@Test
	public void testGetCookie(){
//		try {
////			HttpUtils.login("448313485@qq.com", "freeman111");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void testWebCrawler() throws IOException{
		CrawlerUtils.crawlerUser("https://www.zhihu.com/people/liancheng");
	}
	
	@Test
	public void testJdbc() throws UnsupportedEncodingException{
		List<String> list = new ArrayList<String>();
		list.add("sdf");
		User user = new User();
		user.setUsername(new String("张勇翔是天才"));
		user.setSignature("sdfsdf");
		user.setLocation("sdf");
		user.setIndustry("sdf");
		user.setSex("sdf");
		user.setCompany("sdf");
		user.setJob("sf");
		user.setUniversity("sdf");
		user.setMajor("sdfs");
		user.setPersionProfile("sdf");
		user.setFollow(list);
		user.setFollower(list);
		JdbcUtils.SaveUser(user);
	}
	
	
	@Test
	public void testSeri() throws UnsupportedEncodingException{
//		Jedis jedis = JedisUtils.getInstance();
//		String data = jedis.lpop("userQueue");
//		System.out.println(data);
//		User user = (User) SerializeUtil.unSerizlize(data.getBytes());
//		System.out.println(user.toString());
		System.out.println(new Queue("visitedUser").size());
	}
	
}
