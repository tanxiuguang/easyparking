package com.easyparking.spider.baidu;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyparking.spider.Request;

/**
 * @author tanxiuguang
 * create on Mar 15, 2012
 */
public class CrawlerRunnable extends Crawler implements Runnable {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private final BlockingQueue<String> queue;
	
	public CrawlerRunnable(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void run() {
		logger.info("thread is running " + Thread.currentThread().getName());
		try {
			boolean finished = false;
			while (!finished) {
				String url = queue.take();
				if (url == null) {
					finished = true;
				}
				Set<BaiduParkData> dataSet = parseResult(Request.requestGet(url));
				Thread.sleep(50);
				saveData(dataSet, true);
				
				logger.info(dataSet.size());
				if (dataSet.size() >= 20) {
					logger.info("result size 20" + url);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
