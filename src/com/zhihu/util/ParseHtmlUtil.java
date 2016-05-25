package com.zhihu.util;

import com.zhihu.inter.Queue;
import com.zhihu.inter.impl.UserQueue;
import com.zhihu.model.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析html工具类
 * @author zhangyx
 */
public class ParseHtmlUtil {

    private static Queue queue = UserQueue.getInstance();

    /**
     * 根据用户主页地址获取主页下所有其他用户主页地址b并加入队列
     * @param  homeUrl 用户主页地址
     * @return
     */
    public static  void getHomeUrlsEnQueueByHomeUrl(String homeUrl){
        try {
			/*我关注了谁地址*/
            String followeesUrl = homeUrl + "/followees";
			/*谁关注了我地址*/
            String followersUrl = homeUrl + "/followers";
            String followeesHtml = HttpUtils.getHtml(followeesUrl);
            String followersHtml = HttpUtils.getHtml(followersUrl);
            Document followeesDoc = Jsoup.parse(followeesHtml);
            Document followersDoc = Jsoup.parse(followersHtml);
            Elements userElements =followeesDoc.select(".zm-list-content-title > a ");
            for(int i = 0 ; i < userElements.size() ; i ++){
                Element userElement = userElements.get(i);
                String url = userElement.attr("href");
                synchronized (ParseHtmlUtil.class){
                    if(!url.trim().equals("")&&!queue.contains(url)){
                        System.out.println(url+":进入队列");
                        queue.enQueue(url);
                    }
                }
            }
            userElements = followersDoc.select(".zm-list-content-title > a");
            for(int i = 0 ; i < userElements.size() ; i ++){
                Element userElement = userElements.get(i);
                String url = userElement.attr("href");
                synchronized (ParseHtmlUtil.class){
                    if(!url.trim().equals("")&&!queue.contains(url)){
                        System.out.println(url+":进入队列");
                        queue.enQueue(url);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * 根据用户主页地址抓取用户基本信息
     * @param homeUrl 主页地址
     * @throws IOException
     */
    public static User crawlerUser(String homeUrl) throws IOException{
		/*用户详情页  = 用户主页 + "/about"*/
        String url = homeUrl + "/about";
        String html = HttpUtils.getHtml(url);
        Document doc = Jsoup.parse(html);
        User u = new User();
        u.setHomeUrl(url);
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
        String sex = doc.select("[data-name = location] span.gender i").size() == 0 ? "" : (doc.select("[data-name = location] span.gender i").get(0).className().indexOf("female") > 0?"女":"男" );
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
//        u.setFolloweesUserName(getFolloweesUsersByUser(homeUrl));
//        u.setFollowersUserName(getFollowersUsersByUser(homeUrl));
        return u;
    }


    public static  List<String> getFolloweesUsersByUser(String user){
        try {
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

    private  static  List<String> getFollowersUsersByUser(String user){
        try {
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
}
