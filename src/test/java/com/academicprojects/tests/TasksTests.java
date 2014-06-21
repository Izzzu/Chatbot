package com.academicprojects.tests;

import org.junit.Assert;
import org.junit.Test;

import com.academicprojects.controller.ChatController;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.Conversation;
import com.academicprojects.model.State;
import com.academicprojects.model.TypeOfSentence;
import com.academicprojects.model.User;


public class TasksTests {
	
	Chatbot chatbot = new Chatbot();
	Conversation u = new Conversation();
	
	@Test
	public void sholudReturnUserName1(){
		
		String answer = "Czesc jestem iza";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Iza"));
	}
	
	@Test
	public void sholudReturnUserName2(){
		
		String answer = "Czesc jestem Iza";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Iza"));
	}
	
	@Test
	public void sholudReturnUserName3(){
		
		String answer = "Czesc nazywam sie janusz";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName4(){
		
		String answer = "Czesc nazywam sie janusz mi³o mi poznac";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Janusz"));
	}
	@Test
	public void sholudReturnUserName5(){
		
		String answer = "Czesc nazywam siê janusz";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName6(){
		
		String answer = "Czesc mam na imiê janusz";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName7(){
		
		String answer = "Zwê siê janusz";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Janusz"));
	}
	
	@Test
	public void sholudReturnUserName8(){
		
		String answer = "janusz";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Janusz"));
		Assert.assertTrue(u.user.getName().equals("Janusz"));
	}
	
	@Test
	public void sholudReturnAnonymousName(){
		
		String answer = "Jestem ";
		
		Assert.assertTrue(chatbot.getUserName(u).equals("Nieznajomy"));
		Assert.assertTrue(u.user.getName().equals("Nieznajomy"));
	}
	
	@Test
	public void sholudReturnNumber(){
		
		String answer = "Jestem23 ";
		
		Assert.assertTrue(chatbot.getUserAge(u).equals("23"));
		Assert.assertTrue(u.user.getAge()==23);
	}
	
	@Test
	public void sholudReturnNull(){
		
		String answer = "Jestem";
		
		//System.out.println(task1.doTask(answer));
		Assert.assertTrue(chatbot.getUserAge(u)==null);
		Assert.assertTrue(u.user.getAge()==0);
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
