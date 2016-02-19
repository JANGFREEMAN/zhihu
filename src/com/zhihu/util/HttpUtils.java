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

	/**֪����¼��ҳ*/
	private static String LOGIN_URL = "http://www.zhihu.com/login/email";
	
	/**֪���˺�*/
	private static String USERNAME = "448313485@qq.com";
	
	/**֪������*/
	private static String PASSWORD = "freeman111";
	
	/**֪������*/
	private static String DOMAIN = "www.zhihu.com";
	
	/**��¼֮�󱣴��Cookie*/
	private static CookieStore cookieStore;
	
	private static String FILE_COOKIE_PATH = "D:/cookieStore.properties";
	
	private static String TEST_URL = "https://www.zhihu.com/people/edit";
	
	private static RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();  
	
	private static CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();  
	
	
	/**
	 * ģ���û���¼֪��
	 */
	public void loginZhiHu(){
		
	}
	
	/**
	 * ��ȡ��ҳ���������е�xsrfֵ
	 * @throws IOException 
	 */
	public static String getXsrf() throws IOException{
		Document doc = Jsoup.connect(LOGIN_URL).get();
		Element _xsrfInput = doc.select("input[name = _xsrf]").get(0);
		String _xsrf = _xsrfInput.attr("value");
		return _xsrf;
	}
	
	/**
	 * ��¼��ҳ�������¼���cookie
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public static void setCookieStore(String username,String password) throws Exception{

		String _xsrf = getXsrf();
		 //����post����
        List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
        valuePairs.add(new BasicNameValuePair("_xsrf", _xsrf));
        valuePairs.add(new BasicNameValuePair("email", username));
        valuePairs.add(new BasicNameValuePair("password", password));
        valuePairs.add(new BasicNameValuePair("remember_me", "true"));
        
        /*
         * ����post���󣬽��������д��������
         */
        HttpPost post = new HttpPost(LOGIN_URL);
        post.setEntity(new UrlEncodedFormEntity(valuePairs, Consts.UTF_8));
        
        /*
         * ��ȡ��Ӧ��Ϣ��
         */
        HttpResponse httpResponse = httpClient.execute(post);
        HttpEntity entity = httpResponse.getEntity();
        
        /*
         * ����cookie
         */
        File cookieFile = new File(FILE_COOKIE_PATH);
        if(!cookieFile.exists()){
        	cookieFile.createNewFile();
        }
	    Header[] headers = httpResponse.getHeaders("Set-Cookie");
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cookieFile));
	    for(Header header : headers){
	    	String cookie = header.getValue().split(";")[0];
	    	bos.write(cookie.getBytes());
	    	bos.write("\r\n".getBytes());//����
	    }
	    bos.close();
	    /*
	     * ����cookie
	     */
	    setCookie();
	}
	
	
	/**
	 * ����cookie
	 * @author zhangyx
	 * @throws Exception 
	 */
	private static void setCookie() throws Exception{
		cookieStore = new BasicCookieStore();
		FileInputStream fis = new FileInputStream(FILE_COOKIE_PATH);
		Properties pro = new Properties();
		pro.load(fis);
		Set<Object> keySet = pro.keySet();
		for(Object obj : keySet){
			String key = String.valueOf(obj);
			String value = pro.getProperty(key);
			BasicClientCookie cookie = new BasicClientCookie(key, value);
		    cookie.setVersion(0);
		    cookie.setDomain(DOMAIN);
		    cookie.setPath("/");
			cookieStore.addCookie(cookie);
		}
	}

	/**
	 * ���Ե�¼��ĵ�COOKIE�Ƿ����
	 * @throws IOException
	 * @author zhangyx
	 */
	public void testCookie() throws IOException{
		CookieStore cookieStore = new BasicCookieStore();
		FileInputStream fis = new FileInputStream(FILE_COOKIE_PATH);
		Properties pro = new Properties();
		pro.load(fis);
		Set<Object> keySet = pro.keySet();
		for(Object obj : keySet){
			String key = String.valueOf(obj);
			String value = pro.getProperty(key);
			BasicClientCookie cookie = new BasicClientCookie(key, value);
		    cookie.setVersion(0);
		    cookie.setDomain(DOMAIN);
		    cookie.setPath("/");
			cookieStore.addCookie(cookie);
		}
	  CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
      HttpGet g = new HttpGet(TEST_URL);  
      CloseableHttpResponse r = client.execute(g);//��ȡ�Ӽ���ע������ҳ�����һ���Ƿ��½�ɹ�  
      System.out.println(EntityUtils.toString(r.getEntity()));  
      r.close();  
	}
	
	/**
	 * ����url��ȡhtml
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
