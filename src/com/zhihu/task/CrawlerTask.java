package com.zhihu.task;

import com.zhihu.inter.Queue;
import com.zhihu.inter.impl.UserQueue;
import com.zhihu.util.JdbcUtils;
import com.zhihu.util.ParseHtmlUtil;

import java.io.IOException;

/**
 * 爬虫任务
 */
public class CrawlerTask {

    private Queue queue = UserQueue.getInstance();

    /**
     * 爬取用户进入队列
     */
    public void crawlUser(){
        while (true){
            if(queue.size() == 2000000){//如果爬取了两百万数据结束爬虫
                return ;
            }
            String homeUrl = queue.deQueue();
            ParseHtmlUtil.getHomeUrlsEnQueueByHomeUrl(homeUrl);
            System.out.println("此时队列大小为==================================================================："+queue.size());
        }
    }
    /**
     * 保存用户到数据库中
     */
    public void saveUsersInDatabase() throws IOException {
        JdbcUtils.saveUsers();
    }
}