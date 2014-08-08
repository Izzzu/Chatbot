package com.academicprojects.model;

import java.util.HashMap;
import java.util.Map;

public class User {
	private String name;
	private String ip;
	private int age = 0;
	private Gender gender;
	private int mood = 0;
	private Map<String, String> info = new HashMap<String,String>();
	private Personality perType = new Personality();
	private int lcu = 0;
	
	
	public int getLcu() {
		return lcu;
	}
	public void setLcu(int lcu) {
		this.lcu = lcu;
	}
	public int getMood() {
		return mood;
	}
	public void setMood(int mood) {
		this.mood = mood;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	
	
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
		
		perType.setNewPersonalityType(type.toLowerCase(), perType.getByPersonalityType(type) + level);
		
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Gender getGender() {
		return gender;
	}
	
	

}
