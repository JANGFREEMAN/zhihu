package com.zhihu.web;

import java.io.IOException;

import com.zhihu.util.HttpUtils;

public class CrawlerClient {

	public static void main(String[] args) throws IOException {
		WebCrawler crawler = new WebCrawler();
		crawler.crawling();
	}
}
