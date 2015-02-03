package com.academicprojects.controller;

import com.academicprojects.model.Answer;
import com.academicprojects.model.Chatbot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Scope("session")
@Controller
public class ChatController {

	Logger log = LoggerFactory.getLogger(ChatController.class);
	Chatbot chatbot;

    public ChatController() throws Exception {

            chatbot = new Chatbot();

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView student(ModelMap model) throws Exception {

		if (chatbot.isUserTurn()) 
		{
			chatbot.recognizeTypeOfSentence(chatbot.getLastAnswer());
			chatbot.updatePersonality();
			chatbot.answer();
            log.trace(chatbot.getChatbotName());
			
		}
		model.addAttribute("answers",chatbot.getConversationCourse());

		return new ModelAndView("chat4", "Answer", new Answer());
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    protected void finalize()
    {


    }




}

