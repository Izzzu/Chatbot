package com.academicprojects.model;

import java.util.ArrayList;
import java.util.List;


public class Conversation {

	private String topic = "";
	private int topicID = -1;
	private int chatLevel = 1;
	private int currentStatementNote = 0;
	private int expectedAnswerTypeId = -1;
	

	private List<String> course = new ArrayList<String>();
	
	
	public List<String> getCourse() {
		return course;
	}
	public void setCourse(List<String> course) {
		this.course = course;
	}
	

	
	public void chatLevelUp()
	{
		chatLevel++;
	}
	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public int getExpectedAnswerTypeId() {
		return expectedAnswerTypeId;
	}
	public void setExpectedAnswerTypeId(int expectedAnswerTypeId) {
		this.expectedAnswerTypeId = expectedAnswerTypeId;
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void addChatbotAnswerToCourse(String s) {
		course.add(s);
	}

	


	public String randomWelcomePhrase(String name)
	{
		String [] welcome = {"Witaj! Jestem empatycznym chatbotem. Nazywam si� <name>, a Ty?", "Dzie� dobry, jestem Chatbot <name>, jak Ci na imi�?",
				"Hej, zapraszam do rozmowy. Mam na imi� <name>, a Ty?", "Witaj, jestem Chatbotem. Ch�tnie z Tob� porozmawiam. Jak masz na imi�?" };
		int r = (int) (Math.random()*welcome.length);
		return welcome[r].replace("<name>", name);
	}

	

	public Conversation() {
		course.add(randomWelcomePhrase("Mieczys�aw"));
	}
	
	public Conversation(String name) {
		course.add(randomWelcomePhrase(name));
	}

	public String getLastAnswer() {
		return course.get(course.size()-1);
	}

	public boolean isUserTurn()
	{
		if (course.size()%2==0) return true;
		else return false;
	}

	public void setChatLevel(int chatLevel) {
		this.chatLevel = chatLevel;
	}

	public int getChatLevel() {
		return chatLevel;
	}

	public void setCurrentStatementNote(int currentStatementNote) {
		this.currentStatementNote = currentStatementNote;
	}

	public int getCurrentStatementNote() {
		return currentStatementNote;
	}



}
