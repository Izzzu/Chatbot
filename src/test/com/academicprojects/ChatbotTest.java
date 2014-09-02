package com.academicprojects;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import junit.framework.TestCase;

public class ChatbotTest extends TestCase {

    Brain brain;
    DbService db = null;
    Chatbot chatbot = new Chatbot();

    public void setUp() throws Exception {
        db = new DbService("db/chatbotDb");
        brain = new Brain(db);
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();

    }
    public void testCatchUserAnswer() throws Exception {



        String useranswer = "finanse";

        assertEquals(5, chatbot.catchUserAnswer(useranswer));


    }

    public void testPersonalityRecognizer() throws Exception {

        assertFalse(chatbot.brain.getPersonalityRecognizer().getPersonalityPhrases().isEmpty());


    }
}