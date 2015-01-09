package com.academicprojects;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.Gender;
import com.academicprojects.model.User;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class QuickTest {



    public QuickTest() {

    }



    @Test
    public void test() throws Exception {

        Brain brain;
        DbService db = null;
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        db = new DbService("db/chatbotDb");
        brain = new Brain(db);
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();
        PolishDictionary dictionary = new PolishDictionary();


        assertEquals("", chatbot.pharaprasize("Chyba nie wiesz co mówisz"));
    }


}
