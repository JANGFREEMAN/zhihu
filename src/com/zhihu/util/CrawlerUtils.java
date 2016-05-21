package com.zhihu.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhihu.model.User;

/**
 * @author zhangyx
 *
 */
public class  CrawlerUtils {

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
//		u.setFollow(getFolloweesUsersByUser(user));
//		u.setFollower(getFollowersUsersByUser(user));
	}

}
