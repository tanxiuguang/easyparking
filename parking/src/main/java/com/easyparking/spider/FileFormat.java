package com.easyparking.spider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.easyparking.spider.baidu.BaiduParkData;
import com.google.gson.Gson;

/**
 * @author tanxiuguang
 * create on Mar 16, 2012
 */
public class FileFormat {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("/tmp/baidudata"));
		FileOutputStream fos = new FileOutputStream("/tmp/csv1");
		OutputStreamWriter out = new OutputStreamWriter(fos, "utf-8");
		BufferedWriter bw = new BufferedWriter(out);
		Gson gson = new Gson();
		String line = null;
		List<BaiduParkData> dataList = new ArrayList<BaiduParkData>();
		while ((line = br.readLine()) != null) {
			BaiduParkData data = gson.fromJson(line, BaiduParkData.class);
			if (!dataList.contains(data)) {
				dataList.add(data);
			}
		}
		
		
		System.out.println(dataList.size());
		double startLat = 39.704575;
		double endLat = 40.252305;
		
		//double startLat = 116.107307;
		//double endLat = 116.669233;
		int cursor = 0;
		for ( ; startLat <= endLat; startLat += 0.01) {
			int counter = 0;
			for (BaiduParkData data : dataList) {
				double curLat = Double.valueOf(data.getLocation().getLat());
				if (curLat < (startLat + 0.01) && curLat > startLat) {
					counter++;
				}
				
			}
			
			System.out.println(startLat + " - " + (startLat + 0.01) + ": " + counter);
			
		}
		
		
		for (BaiduParkData data : dataList) {
		
			
			bw.write(data.getName().replace(",", "-") + "," + data.getLocation().getLat() + "," + data.getLocation().getLng() + "," + data.getAddress().replace(",", "-") + "\n");
		}
		

		
		bw.flush();
		br.close();
		bw.close();
	}

}
