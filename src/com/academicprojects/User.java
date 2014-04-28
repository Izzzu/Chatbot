package com.academicprojects;

import java.util.HashMap;
import java.util.Map;

public class User {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Map<String, String> getInfo() {
		return info;
	}
	public void setInfo(Map<String, String> info) {
		this.info = info;
	}
	private String ip;
	private Map<String, String> info = new HashMap<String,String>();

}
