package com.academicprojects.model;

import java.util.Scanner;

public class GettingUserAge extends Task {

	@Override
	public String doTask(String answer, User user) {
		Scanner sc = new Scanner(answer);
		String token = "";
		token = sc.findInLine("\\d+");
		if (token==null) 
			{
				setLevel(State.IN_PROGRESS);
				
			}
		else 
			{
				user.setAge(Integer.parseInt(token));
				setLevel(State.COMPLETED);
				
			}
		return token;
	}

}
