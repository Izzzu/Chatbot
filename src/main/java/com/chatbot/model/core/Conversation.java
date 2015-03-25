package com.chatbot.model.core;

import java.rmi.UnexpectedException;
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
		String [] welcome = {"Witaj! Jestem empatycznym chatbotem. Nazywam się <name>, a Ty?", "Dzień dobry, jestem Chatbot <name>, jak Ci na imię?",
				"Hej, zapraszam do rozmowy. Mam na imię <name>, a Ty?", "Witaj, jestem Chatbotem. Chętnie z Tobę porozmawiam. Jak masz na imię?" };
		int r = (int) (Math.random()*welcome.length);
		return welcome[r].replace("<name>", name);
	}

	

	public Conversation() {
		course.add(randomWelcomePhrase("Mieczysław"));
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


	public String getLastChatbotAnswer()  {
		int size = this.getCourse().size();
		try {
			if (size <=1)
				throw new UnexpectedException("Chatbot did not respond yet");
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
		return this.getCourse().get(size-1);
	}
}
