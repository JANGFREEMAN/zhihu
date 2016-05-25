package com.zhihu.task;

import com.zhihu.inter.Queue;
import com.zhihu.inter.impl.UserQueue;
import com.zhihu.model.User;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.ParseHtmlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 爬虫任务
 */
public class CrawlerTask {

    private Queue queue = UserQueue.getInstance();

    private Object obj1 = new Object();

    private Object obj2 = new Object();

    private int i = 0;

    /**
     * 爬取用户进入队列
     */
    public void crawlUser(){
        int i = 0;
        while (true){
            synchronized (obj1){
                if(queue.size() >= 1000000){//如果爬取了两百万数据结束爬虫
                    return ;
                }
                String homeUrl = queue.get(i);
                i++;
                System.out.println(homeUrl+":爬取用户下面的节点开始====================================");
                ParseHtmlUtil.getHomeUrlsEnQueueByHomeUrl(homeUrl);
                System.out.println(homeUrl+":爬取用户下面的节点结束====================================SIZE:"+queue.size());
            }
        }
    }

    /**
     * 保存用户到数据库中
     */
    public void saveUsersInDatabase() {
        while(!queue.isEmpty()){
            synchronized (obj2){
                User user = null;
                try {
                    user = ParseHtmlUtil.crawlerUser(queue.get(i));
                    JdbcUtils.saveUser(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
    }
}
