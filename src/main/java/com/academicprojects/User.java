package com.academicprojects;

import java.util.HashMap;
import java.util.Map;

public class User {
	private String name;
	private String ip;
	private Map<String, String> info = new HashMap<String,String>();
	private Personality perType = new Personality();
	
	public Personality getPerType() {
		return perType;
	}
	public void setPerType(Personality perType) {
		this.perType = perType;
	}
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
	
	public void updatePersonality(String type, int level){
		
		perType.setByKey(type.toLowerCase(), perType.getByKey(type)+level);
		
	}
	

}
