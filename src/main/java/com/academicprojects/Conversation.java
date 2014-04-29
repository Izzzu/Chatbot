package com.academicprojects;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
	
	public List<String> course = new ArrayList<String>();
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	Conversation() {
		course.add("Witaj! Jestem empatycznym chatbotem. Nazywam siê Zbyszek, a Ty? ");
	}
}
