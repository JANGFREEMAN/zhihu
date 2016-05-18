package com.zhihu.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.zhihu.util.JedisUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.zhihu.model.User;
import com.zhihu.util.CrawlerUtils;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.Queue;

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
		System.out.println("sdf");
		System.out.println(new Queue("visitedUser").size());
	}

	@Test
	public void testRedis(){
		System.out.println(JedisUtils.getInstance());
	}

	@Test
	public void testLogin() throws IOException {
		CookieStore cookieStore = new BasicCookieStore();
//		String cookies = "d_c0=\"AEAAGtM78QmPTv3AruUdn0Vs6JI7Ka_txhA=|1463587741\"; _za=23efb2ff-4188-4faa-936c-e35eaadbedca; l_n_c=1; q_c1=4ac8493fc01744c3ad0d80db4feb8fd4|1463587742000|1463587742000; _xsrf=cc7be9e44518103ebb0acc1dff039105; _zap=4fc04152-a66d-4f52-9c86-77d84c9f1b7d; login=\"YWQ1YzIwYTg3MmY4NDExYTlmNmEwYjU1NDFkNmI0NGI=|1463592123|7193641f0e78c4dd0558e44f8dad0d256678834d\"; __utmt=1; l_cap_id=\"YTM5NTcyMTAyZjU0NDYxNzhkYzk3OTViMTQzYWNjYzQ=|1463594139|7fb506034fbe9d83257274fd533703ad578ebb04\"; cap_id=\"ZTMzZTI3NzE3NDFiNGEzMmIwZWFjMDc5OTcwN2UxMmE=|1463594139|f630c286d510a0df4ed74393ea152b394ee554b3\"; __utma=51854390.328634491.1463587740.1463590496.1463594117.4; __utmb=51854390.4.10.1463594117; __utmc=51854390; __utmz=51854390.1463594117.4.3.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/; __utmv=51854390.000--|2=registration_date=20141015=1^3=entry_date=20160519=1; n_c=1";
//		String[] cookieArr = cookies.split(";");
//		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("D:/cookieStore.properties")));
//		for(String cookie : cookieArr){
//			bos.write(cookie.getBytes());
//			bos.write("\r\n".getBytes());//换行
//			String key  = cookie.split("=")[0];
//			String value  = cookie.split("=")[1];
//			System.out.println(key+":"+value);
//			BasicClientCookie ck = new BasicClientCookie(key, value);
//			ck.setVersion(0);
//			ck.setDomain("www.zhihu.com");
//			ck.setPath("/");
//			cookieStore.addCookie(ck);
//		}
//		bos.close();
		FileInputStream fis = new FileInputStream("D:/cookieStore.properties");
		Properties pro = new Properties();
		pro.load(fis);
		Set<Object> keySet = pro.keySet();
		for(Object obj : keySet){
			String key = String.valueOf(obj);
			String value = pro.getProperty(key);
			System.out.println(key+":"+value);
			BasicClientCookie cookie = new BasicClientCookie(key, value);
			cookie.setVersion(0);
			cookie.setDomain("www.zhihu.com");
			cookie.setPath("/");
			cookieStore.addCookie(cookie);
		}

		CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		HttpGet g = new HttpGet("https://www.zhihu.com/people/edit");
		CloseableHttpResponse r = client.execute(g);//获取子集关注的问题页面测试一下是否登陆成功
		System.out.println(EntityUtils.toString(r.getEntity()));
		r.close();
	}

	public static void main(String[] args) {
		String test = "_c0=\"AEAAGtM78QmPTv3AruUdn0Vs6JI7Ka_txhA=|1463587741\"";
		test.split("=");
	}

}
