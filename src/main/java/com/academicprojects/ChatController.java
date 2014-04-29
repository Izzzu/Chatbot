package com.academicprojects;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatController {
	Conversation conversation = new Conversation();
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	 public ModelAndView student(ModelMap model) {
		
		Personality p = new Personality();
		//System.out.println("typ max" + p.getFirstType());
		if (conversation.course.size()%2==0) 
			{
				conversation.course.add("czeœæ Patryk");
			}
		model.addAttribute("answers",conversation.course);
	      return new ModelAndView("chat4", "Answer", new Answer());
	   }
	   
	@RequestMapping(value = "/", method = RequestMethod.POST)
	 public String addAnswer(@ModelAttribute("Answer")Answer a, ModelMap model) 
	{
	   model.addAttribute("sentence", a.getSentence());
	   conversation.course.add(a.getSentence());
	   return "redirect:/";
	}
	
}

