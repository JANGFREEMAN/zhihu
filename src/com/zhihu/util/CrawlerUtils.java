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
	/**δ��ȡ��Ϣ���û�����*/
	private static Queue unVisitedUser = new Queue("unVisitedUser");
	
	/**�Ѿ���ȡ��Ϣ���û�����*/
	private static Queue visitedUser = new Queue("visitedUser");
	
	/**�˺Ÿ�����ҳ��ַ*/
	private static String homeUrl = "https://www.zhihu.com/people/zhang-yong-xiang-12";
	
	/**
	 * ��ʼ�����У����ҹ�ע�˺͹�ע�߼���unVisitedUser������
	 * @author zhangyx
	 */
	public static void initUnVisitedUser(){
		getUsersEnQueueByUser(homeUrl);
	}
	
	/**
	 * ����user��ȡ��user��ע�˺͹�ע�ߵ��û��������
	 * @param user �û���ҳ��ַ
	 */
	public static void getUsersEnQueueByUser(String user){
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
	 * ��ȡ�û���ע�˵��û�
	 * @param user �û���ҳ��ַ
	 */
	public static List<String> getFolloweesUsersByUser(String user){
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
	public  static List<String> getFollowersUsersByUser(String user){
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
	public static void crawlerUser(String user) throws IOException{
		/*�û�����ҳ  = �û���ҳ + "/about"*/
		String url = user + "/about";
		String html = HttpUtils.getHtml(url);
		Document doc = Jsoup.parse(html);
		User u = new User();
		/**�û���*/
		 String username = doc.select(".ellipsis a").get(0).text();
		 u.setUsername(username);
		/**����ǩ��*/
		 String signature = doc.select(".ellipsis span").get(0).text();
		 u.setSignature(signature);
		/**��ס��*/
		 String location = doc.select("[data-name = location] span.location").get(0).attr("title");
		 u.setLocation(location);
		 /**��ҵ*/
		 String industry = doc.select("[data-name = location] span.business").get(0).attr("title");
		 u.setIndustry(industry);
		 /**�Ա�*/
		 String sex = doc.select("[data-name = location] span.gender i").get(0).className().indexOf("male") > 0?"��":"Ů" ;
		 u.setSex(sex);
		 /**��˾*/
		 String company = doc.select("[data-name = employment] span.employment").get(0).attr("title");
		 u.setCompany(company);
		 /**ְλ*/
		 String job = doc.select("[data-name = employment] span.position ").get(0).attr("title");
		 u.setJob(job);
		 /**��ѧ*/
		 String university = doc.select("[data-name = education] span.education ").get(0).attr("title");
		 u.setUniversity(university);
		 /**רҵ*/
		 String major = doc.select("[data-name = education] span.education-extra ").get(0).attr("title");
		 u.setMajor(major);
		 /**���˼��*/
		 String persionProfile = doc.select("[data-name = description] span.content ").get(0).text();
		 u.setPersionProfile(persionProfile);
		 u.setFollow(getFolloweesUsersByUser(user));
		 u.setFollower(getFollowersUsersByUser(user));
		 System.out.println(u.toString());
	}
	
}
