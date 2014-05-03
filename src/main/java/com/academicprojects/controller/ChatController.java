package com.academicprojects.controller;
import java.sql.ResultSet;
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
import com.academicprojects.model.GettingUserName;
import com.academicprojects.model.State;
import com.academicprojects.model.TypeOfSentence;
import com.academicprojects.model.User;
import com.academicprojects.model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {
	Conversation conversation = new Conversation();
	DbService db = null;
	Logger log = LoggerFactory.getLogger(ChatController.class);
	Chatbot chatbot = new Chatbot();
	User user = new User();
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	 public ModelAndView student(ModelMap model) {
		
		//System.out.println("typ max" + p.getFirstType());
		runDb();
		
		if (conversation.isUserTurn()) 
			{
				recognizeTypeOfSentence(conversation.getLast());
				String [] phrases = conversation.getLast().split("[\',;/.\\s]+");
				for(int i = 0; i<phrases.length; i++)
				{
					System.out.println(i);
					int level = 0;
					String type = null;
					System.out.println(phrases[i]);
					try {
						ResultSet rs = db.query("SELECT level, type FROM phrase as ph join personality as pe on ph.id_per=pe.id_per WHERE ph.word LIKE '"+phrases[i]+"'");
						
						ResultSetMetaData meta   = rs.getMetaData();
						
						while(rs.next()) {
							
								
				            	level = rs.getInt(1);
				            	type = rs.getString(2);
				            	log.info("Level: "+level+ " type: "+type);
				            	user.updatePersonality(type, level);
				            	log.info(user.getPerType().toString());
				            
						}
						
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if ((chatbot.gettingUserName.getLevel()==State.START))
				{
					chatbot.gettingUserName.doTask(conversation.getLast(), user);
					conversation.course.add("Witaj "+user.getName()+". Na pocz¹tku naszej rozmowy chcia³bym zadaæ Ci kilka pytañ, które pozwol¹ mi Cie bli¿ej poznaæ. Ile masz lat?");
				}
				else if ((chatbot.gettingUserAge.getLevel()==State.START))
				{
					if (chatbot.gettingUserAge.doTask(conversation.getLast(), user)==null)					
						conversation.course.add("Nie odpowiadaj jeœli nie chcesz. Jak Twoje dzisiejsze samopoczucie?");
					else conversation.course.add("Jesteœ wci¹¿ m³od¹ osob¹. Jak Twoje dzisiejsze samopoczucie?");
					
				}
				else conversation.course.add("Jestem na razie g³upim chatbotem i nic wiecej nie umiem powiedzieæ.");
				
				
			}
		model.addAttribute("answers",conversation.course);
		//db.shutdown();
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
	   conversation.course.add(a.getSentence());
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

