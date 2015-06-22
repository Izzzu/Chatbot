package com.chatbot.controller;

import com.chatbot.model.answer.Answer;
import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.user.PersonalityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Scope("session")
@Controller
public class ChatController {

	private Logger log = LoggerFactory.getLogger(ChatController.class);
	private Chatbot chatbot;

    public ChatController() throws Exception {
		chatbot = new Chatbot(new Brain());

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView student(ModelMap model) throws Exception {

		if (chatbot.isUserTurn()) 
		{
			String lastAnswer = chatbot.getLastAnswer();
			chatbot.updateInformationAboutUser(lastAnswer);
			chatbot.answer();
            log.trace(chatbot.getChatbotName());
			
		}
		model.addAttribute("answers",chatbot.getConversationCourse());

		return new ModelAndView("chat4", "Answer", new Answer());
	}

	@RequestMapping(value = "/raport", method = RequestMethod.GET)
	public ModelAndView raport() throws Exception {

		ModelAndView mav = new ModelAndView("raport");
			List<PersonalityType> personalities = chatbot.getUser().getPersonality().getTypes();

		mav.addObject("personalityTypes",personalities);

		return mav;
	}


	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public String reload() throws IOException, SQLException {
		chatbot = new Chatbot(new Brain());
		return "redirect:/";
	}



    @RequestMapping(value = "/", method = RequestMethod.POST)
	public String addAnswer(@ModelAttribute("Answer")Answer a, ModelMap model)
	{
		model.addAttribute("sentence", a.getSentence());
		chatbot.addUserAnswer(a.getSentence());
		return "redirect:/";
	}


}

