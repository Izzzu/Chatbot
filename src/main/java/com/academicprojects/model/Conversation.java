package com.academicprojects.model;

import java.util.ArrayList;
import java.util.List;


public class Conversation {

	public List<String> course = new ArrayList<String>();
	public User user = new User();
	private String [] welcome = {"Witaj! Jestem empatycznym chatbotem. Nazywam siê Zbyszek, a Ty?", "Dzieñ dobry, jestem Chatbot Zbyszek, jak Ci na imiê",
			"Hej, zapraszam do rozmowy. Mam na imiê Zbyszek, a Ty?", "Witaj, jestem Chatbotem. Chêtnie z Tob¹ porozmawiam. Jak masz na imiê?" };


	public String randomWelcomePhrase()
	{
		int r = (int) (Math.random()*welcome.length);
		return welcome[r];
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Conversation() {
		course.add(randomWelcomePhrase());
	}

	public String getLast() {
		return course.get(course.size()-1);
	}

	public boolean isUserTurn()
	{
		if (course.size()%2==0) return true;
		else return false;
	}
}
