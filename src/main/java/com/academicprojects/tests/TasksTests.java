package com.academicprojects.tests;

import org.junit.Assert;
import org.junit.Test;

import com.academicprojects.controller.ChatController;
import com.academicprojects.model.GettingUserAge;
import com.academicprojects.model.GettingUserName;
import com.academicprojects.model.State;
import com.academicprojects.model.TypeOfSentence;
import com.academicprojects.model.User;


public class TasksTests {
	
	@Test
	public void sholudReturnUserName1(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Czesc jestem iza";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Iza"));
	}
	
	@Test
	public void sholudReturnUserName2(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Czesc jestem Iza";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Iza"));
	}
	
	@Test
	public void sholudReturnUserName3(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Czesc nazywam sie janusz";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName4(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Czesc nazywam sie janusz mi³o mi poznac";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Janusz"));
	}
	@Test
	public void sholudReturnUserName5(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Czesc nazywam siê janusz";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName6(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Czesc mam na imiê janusz";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName7(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Zwê siê janusz";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName8(){
		GettingUserName task1 = new GettingUserName();
		String answer = "janusz";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer, u).equals("Janusz"));
		Assert.assertTrue(u.getName().equals("Janusz"));
	}
	
	@Test
	public void sholudReturnAnonymousName(){
		GettingUserName task1 = new GettingUserName();
		String answer = "Jestem ";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("Nieznajomy"));
		Assert.assertTrue(u.getName().equals("Nieznajomy"));
	}
	
	@Test
	public void sholudReturnNumber(){
		GettingUserAge task1 = new GettingUserAge();
		String answer = "Jestem23 ";
		User u = new User();
		Assert.assertTrue(task1.doTask(answer,u).equals("23"));
		Assert.assertTrue(u.getAge()==23);
	}
	
	@Test
	public void sholudReturnNull(){
		GettingUserAge task1 = new GettingUserAge();
		String answer = "Jestem";
		User u = new User();
		//System.out.println(task1.doTask(answer));
		Assert.assertTrue(task1.doTask(answer, u)==null);
		Assert.assertTrue(u.getAge()==0);
	}
	
	@Test
	public void sholudRecognizeQuestion(){
		ChatController c = new ChatController();
		TypeOfSentence st = c.recognizeTypeOfSentence("Hej!!! Co s³ychaæ?");
		
		Assert.assertTrue(st.equals(TypeOfSentence.QUESTION));
	}
	
	@Test
	public void sholudRecognizeIndicativeSentence(){
		ChatController c = new ChatController();
		TypeOfSentence st = c.recognizeTypeOfSentence("Mi³o by³o Ciê poznaæ.");
		
		Assert.assertTrue(st.equals(TypeOfSentence.INDICATIVE));
	}
	
	@Test
	public void sholudRecognizeExclamation(){
		ChatController c = new ChatController();
		TypeOfSentence st = c.recognizeTypeOfSentence("Hej!!! Kup 4 bu³ki!");
		
		Assert.assertTrue(st.equals(TypeOfSentence.EXCLAMATION));
	}
	
	@Test
	public void sholudRecognizeOtherSentence(){
		ChatController c = new ChatController();
		TypeOfSentence st = c.recognizeTypeOfSentence("Hej!!! Kup 4 bu³ki:)");
		
		Assert.assertTrue(st.equals(TypeOfSentence.OTHER));
	}
	

}
