package com.zhihu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.zhihu.model.User;
import com.zhihu.util.HttpUtils;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.Queue;
import com.zhihu.util.SerializeUtil;

/**
 * @author zhangyx
 *
 */
public class  WebCrawler {
	/***/
	private  Queue unVisitedUser = new Queue("unVisitedUser");
	
	/**�Ѿ���ȡ��Ϣ���û�����*/
	private  Queue visitedUser = new Queue("visitedUser");
	
	/**�û�����*/
	private  Queue UserQueue = new Queue("userQueue");
	
	/**�˺Ÿ�����ҳ��ַ*/
	private  String homeUrl = "https://www.zhihu.com/people/zhang-yong-xiang-12";
	
	/**����һ����*/
	private Lock lock = new ReentrantLock();
	
	private Condition condition1 = lock.newCondition();
	
	private Condition condition2 = lock.newCondition();
	
	
	
	/**
	 * ��ʼ�����У����ҹ�ע�˺͹�ע�߼���unVisitedUser������
	 * @author zhangyx
	 */
	public  void initUnVisitedUser(){
		getUsersEnQueueByUser(homeUrl);
	}
	
	/**
	 * ����user��ȡ��user��ע�˺͹�ע�ߵ��û��������
	 * @param user �û���ҳ��ַ
	 */
	public  void getUsersEnQueueByUser(String user){
		try {
			/*�û���ע�˵�ַ*/
			String followeesUrl = user + "/followees";
			/*�û���ע�ߵ�ַ*/
			String followersUrl = user + "/followers";
			String followeesHtml = HttpUtils.getHtml(followeesUrl);
			String followersHtml = HttpUtils.getHtml(followersUrl);
			Document followeesDoc = Jsoup.parse(followeesHtml);
			Document followersDoc = Jsoup.parse(followersHtml);
			Elements userElements =followeesDoc.select(".zm-list-content-title > a ");
			for(int i = 0 ; i < userElements.size() ; i ++){
				Element userElement = userElements.get(i);
				String url = userElement.attr("href");
				if(!url.trim().equals("")&&!unVisitedUser.contains(url)&&!visitedUser.contains(url)){
					System.out.println(url+":�������");
					unVisitedUser.enQueue(url);
				}
			}
			userElements = followersDoc.select(".zm-list-content-title > a");
			for(int i = 0 ; i < userElements.size() ; i ++){
				Element userElement = userElements.get(i);
				String url = userElement.attr("href");
				if(!url.trim().equals("")&&!unVisitedUser.contains(url)&&!visitedUser.contains(url)){
					System.out.println(url+":�������");
					unVisitedUser.enQueue(url);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * ��ȡ�û���ע�˵��û�
	 * @param user �û���ҳ��ַ
	 */
	public  List<String> getFolloweesUsersByUser(String user){
		try {
			/*�û���ע�˵�ַ*/
			String followeesUrl = user + "/followees";
			String followeesHtml = HttpUtils.getHtml(followeesUrl);
			Document followeesDoc = Jsoup.parse(followeesHtml);
			Elements userElements =followeesDoc.select(".zm-list-content-title > a ");
			List<String> users = new ArrayList<String>();
			for(int i = 0 ; i < userElements.size() ; i ++){
				Element userElement = userElements.get(i);
				String username = userElement.attr("title");
				users.add(username);
			}
			return users;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * ��ȡ��ע�˸��û����û�
	 * @param user �û���ҳ��ַ
	 */
	public   List<String> getFollowersUsersByUser(String user){
		try {
			/*�û���ע�ߵ�ַ*/
			String followersUrl = user + "/followers";
			String followersHtml = HttpUtils.getHtml(followersUrl);
			Document followersDoc = Jsoup.parse(followersHtml);
			Elements userElements =followersDoc.select(".zm-list-content-title > a ");
			List<String> users = new ArrayList<String>();
			for(int i = 0 ; i < userElements.size() ; i ++){
				Element userElement = userElements.get(i);
				String username = userElement.attr("title");
				users.add(username);
			}
			return users;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	
	
	
	/**
	 * �����û���ҳ��ַץȡ�û�������Ϣ
	 * @param user
	 * @throws IOException 
	 */
	public  User crawlerUser(String user) throws IOException{
		/*�û�����ҳ  = �û���ҳ + "/about"*/
		String url = user + "/about";
		String html = HttpUtils.getHtml(url);
		Document doc = Jsoup.parse(html);
		User u = new User();
		/**�û���*/
		 String username = doc.select(".ellipsis a").size() == 0 ? "":doc.select(".ellipsis a").get(0).text();
		 u.setUsername(username);
		/**����ǩ��*/
		 String signature = doc.select(".ellipsis span").size() == 0 ? "":doc.select(".ellipsis span").get(0).text();
		 u.setSignature(signature);
		/**��ס��*/
		 String location = doc.select("[data-name = location] span.location").size() == 0 ?"":doc.select("[data-name = location] span.location").get(0).attr("title");
		 u.setLocation(location);
		 /**��ҵ*/
		 String industry = doc.select("[data-name = location] span.business").size() == 0 ? "":doc.select("[data-name = location] span.business").get(0).attr("title");
		 u.setIndustry(industry);
		 /**�Ա�*/
		 String sex = doc.select("[data-name = location] span.gender i").size() == 0 ? "" : doc.select("[data-name = location] span.gender i").get(0).className().indexOf("male") > 0?"��":"Ů" ;
		 u.setSex(sex);
		 /**��˾*/
		 String company = doc.select("[data-name = employment] span.employment").size() == 0 ?  "" : doc.select("[data-name = employment] span.employment").get(0).attr("title");
		 u.setCompany(company);
		 /**ְλ*/
		 String job = doc.select("[data-name = employment] span.position ").size() == 0 ? "" : doc.select("[data-name = employment] span.position ").get(0).attr("title");
		 u.setJob(job);
		 /**��ѧ*/
		 String university = doc.select("[data-name = education] span.education ").size() == 0 ? "" : doc.select("[data-name = education] span.education ").get(0).attr("title");
		 u.setUniversity(university);
		 /**רҵ*/
		 String major = doc.select("[data-name = education] span.education-extra ").size() == 0 ? "" : doc.select("[data-name = education] span.education-extra ").get(0).attr("title");
		 u.setMajor(major);
		 /**���˼��*/
		 String persionProfile =   doc.select("[data-name = description] span.content ").size() == 0 ? "" : doc.select("[data-name = description] span.content ").get(0).text();
		 u.setPersionProfile(persionProfile);
		 u.setFollow(getFolloweesUsersByUser(user));
		 u.setFollower(getFollowersUsersByUser(user));
		 return u;
	}
	
	/**
	 * �߳�����ץȡ�û�
	 * @author zhangyx
	 * @throws IOException 
	 */
	public  synchronized void getUsersTask() throws IOException{
		String user = unVisitedUser.deQueue();
		getUsersEnQueueByUser(user);
		visitedUser.enQueue(user);
		System.out.println(String.format("%s:�߳�ץȡ%s��ע���û�,��ʱunVisitedUsr���д�СΪ��%s,visitedUser���д�СΪ��%s", Thread.currentThread().getName(),user,unVisitedUser.size(),visitedUser.size()));
	}
	
	/**
	 * �߳������ȡ�û�����
	 * @author zhangyx
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private int i = 0 ;
	public    synchronized void crawlUserInfoTask() throws IOException, InterruptedException {
		String user = unVisitedUser.get(i);
		User u = crawlerUser(user);
		System.out.println(u.toString());
		JdbcUtils.SaveUser(u);
		i++;
	}
	
	/**
	 * ���û���Ϣ���б��浽���ݿ���
	 * @throws InterruptedException 
	 */
	public  void getUserQueueInDataBase() throws InterruptedException{
		lock.lock();
		while(UserQueue.isEmpty()){
			System.out.println(String.format("%s:�߳�ִ�д洢���ݿ�ʱuserQueue����Ϊ�գ�����ȴ�", Thread.currentThread().getName()));
			condition2.await();
		}
		String userData = UserQueue.deQueue();
		User user  = (User)SerializeUtil.unSerizlize(userData.getBytes());
		System.out.println(String.format("%s:�߳̿�ʼ���û�%s��Ϣ�������ݿ���", Thread.currentThread().getName(),user));
		JdbcUtils.SaveUser(user);
		lock.unlock();
	}
	
	
	/**
	 * @author zhangyx
	 * ��ʼ������
	 * @throws Exception 
	 */
	public void initCrawler() throws Exception{
		/*
		 * ����cookie
		 */
		HttpUtils.setCookieStore("448313485@qq.com", "freeman111");
		/*
		 * ��ʼ���û�����unVisitedUser������
		 */
		initUnVisitedUser();
	}
	
	
	
	/**
	 * @author zhangyx
	 * ��ʼ����
	 */
	public void crawling(){
		try {
			/*��ʼ������*/
			initCrawler();
			
			/*
			 * �����̣߳�ץ�û�����unVisitedUser������
			 */
			for(int i = 0 ; i < 3 ; i ++){
				new Thread(new  Runnable() {
					public void run() {
						while(true){
							try {
								getUsersTask();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
			
			/*
			 * �����̣߳���ȡ�û���Ϣ
			 */
			for(int i = 0 ; i < 3 ; i ++){
				new Thread(new  Runnable() {
					public void run() {
						while(true){
							try {
								crawlUserInfoTask();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
