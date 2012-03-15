package com.easyparking.spider.baidu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
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
		"5f2345904a86ddb927372e9d88a9de2e",
		"4d263b1a71c6470dcc9d5d997eec2db9", 
		"92647395c154b571306ee85460032a9b",
		"a71bb127025edc29a14dd9f5b0dcec16",
		"8ff13ca44f18bdc78e6acba0ee5222e4",
		"be68e80a086317f5a44b65116ccb1bfb"
	};
	
	
	private Set<BaiduParkData> parseResult(String result) {
		try {
			JSONObject resultJSON = new JSONObject(result);
			String status = resultJSON.optString("status");
			logger.debug("status: " + status);
			
			JSONArray resultJSONArray = resultJSON.optJSONArray("results");
			Set<BaiduParkData> dataSet = new HashSet<BaiduParkData>();
			
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
	
	public void crawleData() {
		double startLat = 39.704575;
		double endLat = 40.252305;
		double endLng = 116.669233;
		
		int counter = 0;
		for ( ; startLat <= endLat; startLat += 0.01) {
			logger.debug("test");
			for (double startLng = 116.107307; startLng <= endLng; startLng += 0.01) {
				logger.debug(buildCrawlUrl(startLat, startLng, startLat + 0.01, startLng + 0.01, keys[counter / 800]));
				counter++;
			}
		}
		logger.debug(counter);
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
	
	private void saveData(Set<BaiduParkData> dataSet, boolean append) {
		Gson gson = new Gson();
		if (dataSet != null) {
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
		saveData(parseResult(Request.requestGet(buildCrawlUrl(39.72,116.12,39.73,116.13, "5f2345904a86ddb927372e9d88a9de2e"))), true);
	}
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Crawler crawler = new Crawler();
		crawler.crawleData();
	}

}
