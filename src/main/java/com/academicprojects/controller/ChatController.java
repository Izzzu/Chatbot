package com.academicprojects.controller;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Answer;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.Conversation;

import com.academicprojects.model.State;
import com.academicprojects.model.TypeOfSentence;
import com.academicprojects.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {
	//Conversation conversation = new Conversation();
	DbService db = null;
	Logger log = LoggerFactory.getLogger(ChatController.class);
	Chatbot chatbot = new Chatbot();
	//User user = new User();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView student(ModelMap model) {

		//System.out.println("typ max" + p.getFirstType());
		runDb();
		chatbot.db = db;
		//chatbot.gettingUserMood.db = db;

		if (chatbot.isUserTurn()) 
		{
			recognizeTypeOfSentence(chatbot.getLastAnswer());
			chatbot.updatePersonality();
			chatbot.answer();
			
		}
		model.addAttribute("answers",chatbot.getConversationCourse());
		try {
			db.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("chat4", "Answer", new Answer());
	}
	
	
	public TypeOfSentence recognizeTypeOfSentence(String s) {
		String str = s.trim();
		if (str.charAt(str.length()-1)=='?') {
			return TypeOfSentence.QUESTION;
		}
		else if (str.charAt(str.length()-1)=='!')
		{
			return TypeOfSentence.EXCLAMATION;
		}
		else if (str.charAt(str.length()-1)=='.')
		{
			return TypeOfSentence.INDICATIVE;
		}
		return TypeOfSentence.OTHER;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String addAnswer(@ModelAttribute("Answer")Answer a, ModelMap model) 
	{
		model.addAttribute("sentence", a.getSentence());
		chatbot.addUserAnswer(a.getSentence());
		return "redirect:/";
	}

	public void runDb()
	{
		try {
			db = new DbService("db/db");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

