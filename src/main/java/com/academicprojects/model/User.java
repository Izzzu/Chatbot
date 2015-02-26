package com.academicprojects.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
public class User {
	private String name = "Nieznajomy";
	private String ip;
	private int age = 0;
	private Gender gender = Gender.NOTKNOWN;
	private int mood = 0;
	private Map<String, String> info = new HashMap<String,String>();
	private Personality personality = new Personality();
	private int lcu = 0;

	public void updatePersonality(int id, int level){

        int personalityLevel = personality.getById(id).getLevel() + level;
		personality.setNewPersonalityType(id, personalityLevel);
		
	}
}
