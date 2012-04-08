package com.easyparking.spider.baidu;
/**
 * @author tanxiuguang
 * create on Mar 15, 2012
 */
public class BaiduParkData implements Comparable<BaiduParkData>{
	
	private String name;
	
	private Location location;
	
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.location.equals(((BaiduParkData) obj).getLocation());
	}

	public int compareTo(BaiduParkData o) {
		return this.location.equals(((BaiduParkData) o).getLocation()) ? 0 : 1;
	}
}
