package com.zhihu.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhihu.model.User;
import com.zhihu.util.HttpUtils;
import com.zhihu.util.Queue;

/**
 * @author zhangyx
 *
 */
public class  CrawlerUtils {
	/**未爬取信息的用户队列*/
	private static Queue unVisitedUser = new Queue("unVisitedUser");
	
	/**已经爬取信息的用户队列*/
	private static Queue visitedUser = new Queue("visitedUser");
	
	/**账号个人主页地址*/
	private static String homeUrl = "https://www.zhihu.com/people/zhang-yong-xiang-12";
	
	/**
	 * 初始化队列，将我关注了和关注者加入unVisitedUser队列中
	 * @author zhangyx
	 */
	public static void initUnVisitedUser(){
		getUsersEnQueueByUser(homeUrl);
	}
	
	/**
	 * 根据user获取该user关注了和关注者的用户加入队列
	 * @param user 用户主页地址
	 */
	public static void getUsersEnQueueByUser(String user){
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
					unVisitedUser.enQueue(url);
				}
			}
			userElements = followersDoc.select(".zm-list-content-title > a");
			for(int i = 0 ; i < userElements.size() ; i ++){
				Element userElement = userElements.get(i);
				String url = userElement.attr("href");
				unVisitedUser.enQueue(url);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 获取用户关注了的用户
	 * @param user 用户主页地址
	 */
	public static List<String> getFolloweesUsersByUser(String user){
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
	public  static List<String> getFollowersUsersByUser(String user){
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
	public static void crawlerUser(String user) throws IOException{
		/*用户详情页  = 用户主页 + "/about"*/
		String url = user + "/about";
		String html = HttpUtils.getHtml(url);
		Document doc = Jsoup.parse(html);
		User u = new User();
		/**用户名*/
		 String username = doc.select(".ellipsis a").get(0).text();
		 u.setUsername(username);
		/**个性签名*/
		 String signature = doc.select(".ellipsis span").get(0).text();
		 u.setSignature(signature);
		/**居住地*/
		 String location = doc.select("[data-name = location] span.location").get(0).attr("title");
		 u.setLocation(location);
		 /**行业*/
		 String industry = doc.select("[data-name = location] span.business").get(0).attr("title");
		 u.setIndustry(industry);
		 /**性别*/
		 String sex = doc.select("[data-name = location] span.gender i").get(0).className().indexOf("male") > 0?"男":"女" ;
		 u.setSex(sex);
		 /**公司*/
		 String company = doc.select("[data-name = employment] span.employment").get(0).attr("title");
		 u.setCompany(company);
		 /**职位*/
		 String job = doc.select("[data-name = employment] span.position ").get(0).attr("title");
		 u.setJob(job);
		 /**大学*/
		 String university = doc.select("[data-name = education] span.education ").get(0).attr("title");
		 u.setUniversity(university);
		 /**专业*/
		 String major = doc.select("[data-name = education] span.education-extra ").get(0).attr("title");
		 u.setMajor(major);
		 /**个人简介*/
		 String persionProfile = doc.select("[data-name = description] span.content ").get(0).text();
		 u.setPersionProfile(persionProfile);
		 u.setFollow(getFolloweesUsersByUser(user));
		 u.setFollower(getFollowersUsersByUser(user));
		 System.out.println(u.toString());
	}
	
}
