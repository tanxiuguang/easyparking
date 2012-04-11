package com.easyparking.spider.baidu;
/**
 * @author tanxiuguang
 * create on Mar 15, 2012
 */
public class Location implements Comparable<Location> {
	
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
		return this.lat.equals(location.lat) && this.lng.equals(location.lng);
	}
	
	public int compareTo(Location o) {
		return Math.abs(this.lat.compareTo(o.getLat())) + Math.abs(this.lng.compareTo(o.getLng()));
	}

	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + "]";
	}
	
}
