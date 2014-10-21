package com.academicprojects.test;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatbotTest {

    Brain brain;
    DbService db = null;
    Chatbot chatbot = new Chatbot();

    @Before
    public void setUp() throws Exception {
        db = new DbService("db/chatbotDb");
        brain = new Brain(db);
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();

    }
    @Test
    public void testCatchUserAnswer() throws Exception {



        String useranswer = "finanse";

        Assert.assertEquals(5, chatbot.catchUserAnswer(useranswer));


    }
    @Test
    public void testPersonalityRecognizer() throws Exception {

        Assert.assertFalse(chatbot.brain.getPersonalityRecognizer().getPersonalityPhrases().isEmpty());
    }

    @Test
    public void pharaprasizeTest() {

        PolishDictionary dictionary = new PolishDictionary();

        Assert.assertEquals("Mówisz, że miałeś problemy finansowe.", chatbot.pharaprasize("Miałem problemy finansowe."));



    }



}