package com.academicprojects;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.Gender;
import com.academicprojects.model.User;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(Parameterized.class)
public class ParaphraseTest {

    String chatbotAnswer;
    String userAnswer;

    public ParaphraseTest(String chatbotAnswer, String userAnswer) {
        this.chatbotAnswer = chatbotAnswer;
        this.userAnswer = userAnswer;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {" miałeś problemy finansowe.", "Miałem problemy finansowe."},
                {" zauważasz poprawę.", "Zauważam poprawę"},
                {" nie zauważasz poprawy.", "Nie zauważam poprawy"},
                {" chciałaś to naprawić.", "Chciałam to naprawić."},
                {" chciałaś zrobić co miałaś.", "Chciałam zrobić co miałam."}
        });
    }

    @Test
    public void pharaprasizeTest() throws Exception {

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

        assertTrue(chatbot.pharaprasize(userAnswer).contains(chatbotAnswer));
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

        assertEquals("",chatbot.pharaprasize("Nie wiem, co mam robić"));
        assertEquals("", chatbot.pharaprasize("Mam kłopoty finansowe"));
        assertEquals("", chatbot.pharaprasize("Chyba nie wiesz co mówisz"));
    }


}
