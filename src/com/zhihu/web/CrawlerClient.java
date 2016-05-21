package com.zhihu.web;

import java.io.IOException;


public class CrawlerClient {

	public static void main(String[] args) throws IOException {
		WebCrawler crawler = new WebCrawler();
		crawler.crawling()           ;
	}
}
