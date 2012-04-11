package com.easyparking.spider.baidu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import com.easyparking.spider.Request;
import com.google.gson.Gson;

/**
 * @author tanxiuguang
 * create on Mar 15, 2012
 */
public class Crawler {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private static final String[] keys = {
		"8ff13ca44f18bdc78e6acba0ee5222e4",
		"be68e80a086317f5a44b65116ccb1bfb",
		"0c4a94a4a9c96f79c38e2d6bade729be",
		"a6d7f4d6cc26c40de940dd5ffecbdfce",
		"96c3607ddd6dd6f7c9abd12405c6a43e",
		"8020a4592e26e30f8d866126a8ac8669",
		"36b1f697547f718176f6f66f4200dac5",
		"cc47fbb42ef0f675e9d933e219d56ff1",
		"0479aff8a0ff1dc2d9330303f92c7a66",
		"cf93fe0430649318696ec5d88b2d461d",
		"52b7598df56ae12907b378aaffdef998",
		"9c4d3e986906db7dcc50674b3b62bdc8",
		"073fbc8d3fe5bad07ee070aa12608fa3",
		"740b3a832fd8b56ba6fed6ec2408b7ca"
		
	};
	
	
	public Set<BaiduParkData> parseResult(String result) {
		try {
			JSONObject resultJSON = new JSONObject(result);
			String status = resultJSON.optString("status");
			
			if (!"OK".equals(status)) {
				logger.info("status: " + status + " " + result);
			}
			JSONArray resultJSONArray = resultJSON.optJSONArray("results");
			Set<BaiduParkData> dataSet = new TreeSet<BaiduParkData>();
				
			for (int i = 0; i < resultJSONArray.length(); i++) {
				Gson gson = new Gson();
				BaiduParkData data = gson.fromJson(resultJSONArray.optJSONObject(i).toString(), BaiduParkData.class);
				dataSet.add(data);
			}
			
			return dataSet;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public List<String> crawleData() {
		double startLat = 39.704575;
		double endLat = 40.252305;
		
		double endLng = 116.669233;
		
		int counter = 0;
		List<String> urlList = new ArrayList<String>();
		Set<BaiduParkData> dataSet = new TreeSet<BaiduParkData>();
		for ( ; startLat <= endLat; startLat += 0.01) {
			logger.debug("test");
			for (double startLng = 116.107307; startLng <= endLng; startLng += 0.01) {
//				String url = buildCrawlUrl(startLat, startLng, startLat + 0.01, startLng + 0.01, keys[counter / 800]);
//				logger.info(url);
//				//urlList.add(url);
//				Set<BaiduParkData> dataSet = parseResult(Request.requestGet(url));
//				
//				if (dataSet != null && dataSet.size() >= 20) {
//					logger.info("result size 20" + url);
//				}
				
				crawlScal(dataSet, startLat, startLng, startLat + 0.01, startLng + 0.01, keys[counter / 500], counter);
				
				counter++;
				
				
				
				try {
					Thread.sleep(100);
				} catch	(Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
		saveData(dataSet, true);
		
		return urlList;
	}
	
	private void crawlScal(Set<BaiduParkData> result, double startLat, double startLng, double endLat, double endLng, String key, int counter) {
		String url = buildCrawlUrl(startLat, startLng, endLat, endLng, keys[counter / 800]);
		logger.info(url);
		//urlList.add(url);
		Set<BaiduParkData> dataSet = parseResult(Request.requestGet(url));
		
		
		if (dataSet != null && dataSet.size() >= 20) {
			
			logger.debug("start lat: " + startLat + " endLat: " + endLat);
			double midLat = (startLat + endLat) / 2;
			logger.debug("result size 20 begin recursion" + midLat);
			crawlScal(result, startLat, startLng, (startLat + endLat) / 2, endLng, key, counter + 1);
			crawlScal(result, (startLat + endLat) / 2, startLng, endLat, endLng, key, counter + 1);
		} else {
			result.addAll(dataSet);
			return;
		}
		
		try {
			Thread.sleep(100);
		} catch	(Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	
	private String buildCrawlUrl(double startLat, double startLng, double endLat, double endLng, String key) {
		StringBuilder builder = new StringBuilder();
		builder.append("http://api.map.baidu.com/place/search?&query=%E5%81%9C%E8%BD%A6%E5%9C%BA&output=json&bounds=");
		builder.append(startLat).append(",");
		builder.append(startLng).append(",");
		builder.append(endLat).append(",");
		builder.append(endLng);
		builder.append("&key=").append(key);
		
		return builder.toString();
	}
	
	public synchronized void saveData(Set<BaiduParkData> dataSet, boolean append) {
		Gson gson = new Gson();
		if (dataSet != null && dataSet.size() > 0) {
			try {
				FileOutputStream fos = new FileOutputStream("/tmp/baidudata", append);
				OutputStreamWriter out = new OutputStreamWriter(fos, "utf-8");
				BufferedWriter writer = new BufferedWriter(out);
				for (BaiduParkData data : dataSet) {
					writer.write(gson.toJson(data) + "\n");
				}
				
				writer.flush();
				writer.close();
				out.close();
				fos.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public void test() {
		logger.debug("debug");
		logger.info("test");
		saveData(parseResult(Request.requestGet(buildCrawlUrl(39.72,116.12,39.73,116.13, "5f2345904a86ddb927372e9d88a9de2e"))), true);
	}
	
	public static void main(String[] args) {
//		BasicConfigurator.configure();
//		Logger.getRootLogger().setLevel(Level.INFO);
	
		PropertyConfigurator.configure("log4j.properties");
		Crawler crawler = new Crawler();
		crawler.crawleData();
//		List<String> urlList = crawler.crawleData();
//		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(urlList);
//		ExecutorService executor = Executors.newFixedThreadPool(10);
//		
//		for (int i = 0; i < 20; i++) {
//			CrawlerRunnable worker = new CrawlerRunnable(queue);
//			executor.execute(worker);
//		}
//		executor.shutdown();
//		while (!executor.isTerminated()) {
//			
//		}

	}

}
