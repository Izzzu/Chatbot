package com.academicprojects;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {
	Conversation conversation = new Conversation();
	DbService db = null;
	Logger log = LoggerFactory.getLogger(ChatController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	 public ModelAndView student(ModelMap model) {
		
		
		
		//System.out.println("typ max" + p.getFirstType());
		runDb();
		
		if (conversation.course.size()%2==0) 
			{
			
				String [] phrases = conversation.course.get(conversation.course.size()-1).split("[\',;/.\\s]+");
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
				            	conversation.user.updatePersonality(type, level);
				            	log.info(conversation.user.getPerType().toString());
				            
						}
						
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				conversation.course.add("czeœæ Patryk");
			}
		model.addAttribute("answers",conversation.course);
		//db.shutdown();
	      return new ModelAndView("chat4", "Answer", new Answer());
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

