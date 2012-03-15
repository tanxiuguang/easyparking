package com.easyparking.spider.baidu;
/**
 * @author tanxiuguang
 * create on Mar 15, 2012
 */
public class Location {
	
	private String lat;
	
	private String lng;

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	@Override
	public boolean equals(Object obj) {
		Location location = (Location) obj;
		return this.lat == location.lat && this.lng == location.lng;
	}
	

}
