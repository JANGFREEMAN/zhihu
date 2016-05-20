package com.zhihu.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HttpUtils {

	/**知乎域名*/
	private static String DOMAIN = "www.zhihu.com";

	/**获取cookieStore对象*/
	private static CookieStore cookieStore = getCookeStore();

	/**
	 * 获取cookieStore（从配置文件中读取cookie数据，模拟用户登录状态）
	 * @return
     */
	private static CookieStore getCookeStore() {
		CookieStore cookieStore = new BasicCookieStore();
		String key = "z_c0";
		String value = PropertiesUtil.getValue("config.properties",key);
		BasicClientCookie cookie = new BasicClientCookie(key,value);
		cookie.setVersion(0);
		cookie.setDomain(DOMAIN);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}

	/**
	 * 根据url获取html
	 * @author zhangyx
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public  static String getHtml(String url) throws IOException{
		CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		HttpGet g = new HttpGet(url);
		CloseableHttpResponse r = client.execute(g);
		return EntityUtils.toString(r.getEntity());
	}

}
