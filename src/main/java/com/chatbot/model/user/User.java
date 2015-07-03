package com.chatbot.model.user;

import com.chatbot.model.capabilities.PersonalityId;
import com.chatbot.model.core.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private String name = "Nieznajomy";
	private String ip;
	private int age = 0;
	private Gender gender = Gender.NOTKNOWN;
	private int mood = 0;
	private Set<Topic> topics = new HashSet<>();
	private Personality personality = new Personality();
	private int lcu = 0;

	public void updatePersonality(PersonalityId id, double toAdd) {
		double personalityLevel = personality.getById(id).getLevel() + toAdd;
		personality.setNewPersonalityType(id, personalityLevel);
	}

	private void addTopic(Topic topic) {
		 topics.add(topic);
	}

	private void addLcu(int lcu) {
		this.lcu += lcu;
	}

	public void updateTopicAndLcu(Topic topic) {
		if(topic!=null && !topics.contains(topic)){
			addTopic(topic);
			addLcu(topic.getLcu());
		}
	}
}
