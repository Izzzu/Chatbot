package com.academicprojects.model;



public abstract class Task {
	private String name = "";
	
	private State level = State.START;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public State getLevel() {
		return level;
	}
	public void setLevel(State level) {
		this.level = level;
	}
	
	public abstract String doTask(String answer, User user);
	
	public boolean isCompleted()
	{
		if (getLevel()==State.COMPLETED) return true;
		else return false;
	}
	
}