package com.academicprojects.controller;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Answer;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.TypeOfSentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@Controller
public class ChatController {

	DbService db = null;
	Logger log = LoggerFactory.getLogger(ChatController.class);
	Chatbot chatbot = new Chatbot();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView student(ModelMap model) throws Exception {

		//System.out.println("typ max" + p.getFirstType());
		runDb();
		chatbot.db = db;
        chatbot.brain.db = db;
        chatbot.brain.setUpBrain();
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
			db = new DbService("db/chatbotDb");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

