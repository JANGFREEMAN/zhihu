package com.zhihu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zhihu.task.CrawlerTask;
import com.zhihu.util.*;


/**
 * @author zhangyx
 *
 */
public class  WebCrawler {

	/**
	 * @author zhangyx
	 * 初始化爬虫
	 * @throws Exception
	 */
	public void initCrawler() throws Exception{
		String homeUrl = PropertiesUtil.getValue("config.properties","homeUrl");
		/**
		 *  将自己账号下的用户数据初始化进入队列中
		 */
		ParseHtmlUtil.getHomeUrlsEnQueueByHomeUrl(homeUrl);
	}

	/**
	 * @author zhangyx
	 * 开始爬虫
	 */
	public void crawling() {
		/*
		 *	初始化爬虫
		 */
		try {
			initCrawler();
		} catch (Exception e) {
			System.out.println("初始化爬虫异常==========================================");
		}
		/*
		 *	创建爬虫任务
		 */
		final CrawlerTask task  = new CrawlerTask();


		for(int i = 0 ; i < 10 ; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					task.crawlUser();
				}
			}).start();
		}

		for(int i = 0 ; i < 10 ; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					task.saveUsersInDatabase();
				}
			}).start();
		}


	}

}
