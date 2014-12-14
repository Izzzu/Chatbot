package com.academicprojects.test;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


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
                {"Mówisz, że miałeś problemy finansowe.", "Miałem problemy finansowe."},
                {"", "Mam problemy finansowe."},
                {"Mówisz, że chciałaś to naprawić.", "Chciałam to naprawić."},
                {"Mówisz, że zauważasz poprawę. ", "Zauważam poprawę"},
                {"Mówisz, że nie zauważasz poprawy. ", "Nie zauważam poprawy"},
                {"", "Nie wiem, co mam robić."}
        });
    }
    @Test
    public void pharaprasizeTest() throws Exception {

        Brain brain;
        DbService db = null;
        Chatbot chatbot = new Chatbot();
        db = new DbService("db/chatbotDb");
        brain = new Brain(db);
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();

        PolishDictionary dictionary = new PolishDictionary();

        Assert.assertEquals(chatbotAnswer, chatbot.pharaprasize(userAnswer));

    }
}
