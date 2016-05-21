package com.zhihu.task;

import com.zhihu.inter.Queue;
import com.zhihu.inter.impl.UserQueue;
import com.zhihu.model.User;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.ParseHtmlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫任务
 */
public class CrawlerTask {

    private Queue queue = UserQueue.getInstance();

    /**
     * 爬取用户进入队列
     */
    public void crawlUser(){
        int i = 0;
        while (true){
            synchronized (this){
                if(queue.size() == 2000000){//如果爬取了两百万数据结束爬虫
                    return ;
                }
                String homeUrl = queue.get(i);
                i++;
                ParseHtmlUtil.getHomeUrlsEnQueueByHomeUrl(homeUrl);
                System.out.println("SIZE:"+queue.size()+":"+Thread.currentThread().getName());
            }
        }
    }

    /**
     * 抓取用户详情进入队列
     */
    public void crawUserDetail(){
        while(queue.isEmpty()){

        }
    }



    /**
     * 保存用户到数据库中
     */
    public void saveUsersInDatabase() throws IOException {
        JdbcUtils.saveUsers();
    }
}
