package com.zhihu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zhihu.util.SerializeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.zhihu.model.User;
import com.zhihu.util.HttpUtils;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.Queue;

/**
 * @author zhangyx
 *
 */
public class  WebCrawler {
	/***/
	private  Queue unVisitedUser = new Queue("unVisitedUser");

	/**已经爬取信息的用户队列*/
	private  Queue visitedUser = new Queue("visitedUser");

	/**用户详情*/
	private  Queue UserQueue = new Queue("userQueue");

	/**账号个人主页地址*/
	private  String homeUrl = "https://www.zhihu.com/people/zhang-yong-xiang-12";

	/**定义一把锁*/
	private Lock lock = new ReentrantLock();

	private Condition condition1 = lock.newCondition();

	private Condition condition2 = lock.newCondition();



	/**
	 * 初始化队列，将我关注了和关注者加入unVisitedUser队列中
	 * @author zhangyx
	 */
	public  void initUnVisitedUser(){
		getUsersEnQueueByUser(homeUrl);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * 根据user获取该user关注了和关注者的用户加入队列
	 * @param user 用户主页地址
	 */
	public  void getUsersEnQueueByUser(String user){
		try {
			/*用户关注了地址*/
			String followeesUrl = user + "/followees";
			/*用户关注者地址*/
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
					System.out.println(url+":进入队列");
					unVisitedUser.enQueue(url);
				}
			}
			userElements = followersDoc.select(".zm-list-content-title > a");
			for(int i = 0 ; i < userElements.size() ; i ++){
				Element userElement = userElements.get(i);
				String url = userElement.attr("href");
				if(!url.trim().equals("")&&!unVisitedUser.contains(url)&&!visitedUser.contains(url)){
					System.out.println(url+":进入队列");
					unVisitedUser.enQueue(url);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 获取用户关注了的用户
	 * @param user 用户主页地址
	 */
	public  List<String> getFolloweesUsersByUser(String user){
		try {
			/*用户关注了地址*/
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
	 * 获取关注了该用户的用户
	 * @param user 用户主页地址
	 */
	public   List<String> getFollowersUsersByUser(String user){
		try {
			/*用户关注者地址*/
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
	 * 根据用户主页地址抓取用户基本信息
	 * @param user
	 * @throws IOException
	 */
	public  User crawlerUser(String user) throws IOException{
		/*用户详情页  = 用户主页 + "/about"*/
		String url = user + "/about";
		String html = HttpUtils.getHtml(url);
		Document doc = Jsoup.parse(html);
		User u = new User();
		/**用户名*/
		String username = doc.select(".ellipsis a").size() == 0 ? "":doc.select(".ellipsis a").get(0).text();
		u.setUsername(username);
		/**个性签名*/
		String signature = doc.select(".ellipsis span").size() == 0 ? "":doc.select(".ellipsis span").get(0).text();
		u.setSignature(signature);
		/**居住地*/
		String location = doc.select("[data-name = location] span.location").size() == 0 ?"":doc.select("[data-name = location] span.location").get(0).attr("title");
		u.setLocation(location);
		/**行业*/
		String industry = doc.select("[data-name = location] span.business").size() == 0 ? "":doc.select("[data-name = location] span.business").get(0).attr("title");
		u.setIndustry(industry);
		/**性别*/
		String sex = doc.select("[data-name = location] span.gender i").size() == 0 ? "" : doc.select("[data-name = location] span.gender i").get(0).className().indexOf("male") > 0?"男":"女" ;
		u.setSex(sex);
		/**公司*/
		String company = doc.select("[data-name = employment] span.employment").size() == 0 ?  "" : doc.select("[data-name = employment] span.employment").get(0).attr("title");
		u.setCompany(company);
		/**职位*/
		String job = doc.select("[data-name = employment] span.position ").size() == 0 ? "" : doc.select("[data-name = employment] span.position ").get(0).attr("title");
		u.setJob(job);
		/**大学*/
		String university = doc.select("[data-name = education] span.education ").size() == 0 ? "" : doc.select("[data-name = education] span.education ").get(0).attr("title");
		u.setUniversity(university);
		/**专业*/
		String major = doc.select("[data-name = education] span.education-extra ").size() == 0 ? "" : doc.select("[data-name = education] span.education-extra ").get(0).attr("title");
		u.setMajor(major);
		/**个人简介*/
		String persionProfile =   doc.select("[data-name = description] span.content ").size() == 0 ? "" : doc.select("[data-name = description] span.content ").get(0).text();
		u.setPersionProfile(persionProfile);
		u.setFollow(getFolloweesUsersByUser(user));
		u.setFollower(getFollowersUsersByUser(user));
		return u;
	}

	/**
	 * 线程任务抓取用户
	 * @author zhangyx
	 * @throws IOException
	 */
	public  synchronized void getUsersTask() throws IOException{
		String user = unVisitedUser.deQueue();
		getUsersEnQueueByUser(user);
		visitedUser.enQueue(user);
		System.out.println(String.format("%s:线程抓取%s关注的用户,此时unVisitedUsr队列大小为：%s,visitedUser队列大小为：%s", Thread.currentThread().getName(),user,unVisitedUser.size(),visitedUser.size()));
	}

	/**
	 * 线程任务获取用户详情
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
	 * 将用户信息队列保存到数据库中
	 * @throws InterruptedException
	 */
	public  void getUserQueueInDataBase() throws InterruptedException{
		lock.lock();
		while(UserQueue.isEmpty()){
			System.out.println(String.format("%s:线程执行存储数据库时userQueue队列为空，挂起等待", Thread.currentThread().getName()));
			condition2.await();
		}
		String userData = UserQueue.deQueue();
		User user  = (User) SerializeUtil.unSerizlize(userData.getBytes());
		System.out.println(String.format("%s:线程开始将用户%s信息存入数据库中", Thread.currentThread().getName(),user));
		JdbcUtils.SaveUser(user);
		lock.unlock();
	}


	/**
	 * @author zhangyx
	 * 初始化爬虫
	 * @throws Exception
	 */
	public void initCrawler() throws Exception{
		/**
		 * 初始化数据进入队列
		 */
		List<String> userUrl = HttpUtils.getHtml(homeUrl);
	}

	/**
	 * @author zhangyx
	 * 开始爬虫
	 */
	public void crawling(){
		try {
			/*初始化爬虫*/
			initCrawler();
			getUsersTask();
			/*
			 * 定义线程，抓用户进入unVisitedUser队列中
			 */
			for(int i = 0 ; i < 1 ; i ++){
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
			 * 定义线程，爬取用户信息
			 */
			for(int i = 0 ; i < 1 ; i ++){
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
