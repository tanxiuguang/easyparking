package com.easyparking.spider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

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
		while ((line = br.readLine()) != null) {
			BaiduParkData data = gson.fromJson(line, BaiduParkData.class);
			bw.write(data.getName().replace(",", "-") + "," + data.getLocation().getLat() + "," + data.getLocation().getLng() + "," + data.getAddress().replace(",", "-") + "\n");
		}
		
		bw.flush();
		br.close();
		bw.close();
	}

}
