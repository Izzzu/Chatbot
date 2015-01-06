package com.academicprojects.test;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
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
        int actual = chatbot.catchUserAnswer(useranswer);
        System.out.println("actual: "+ actual);
        Assert.assertEquals(5, actual);
    }

    @Test
    public void testDbConnection() {
        Assert.assertNotNull(db);
    }

    /*@Test
    public void shouldThrowExceptionWhen*/
    @Test
    public void testPersonalityRecognizer() throws Exception {

        Assert.assertFalse(chatbot.brain.getPersonalityRecognizer().getPersonalityPhrases().isEmpty());
    }


    @Test
    public void shouldRecognizeQuestion() {

        String [] chatbotAnswersForQuestions = {
/*                "Wróćmy do rozmowy o Tobie.",
                "Interesujące pytanie.",
                "Ciężko powiedzieć, jestem chatbotem:)",
                "Dlaczego o to pytasz?",
                "Nie potrafię odpowiedzieć, jestem chatbotem.",
                "Porozmawiajmy lepiej o Tobie",
                "Nie wypytuj.",
                "Ty odpowiedz pierwszy",
                "Rozmawiamy o Tobie",
                "Na pytania przyjdzie czas później, teraz rozmawiamy o Tobie.",*/
                ""
        };

        String[] userAnswers = {
/*                "Co robisz?",
                "Lubisz mnie?",
                "Powiedz coś",
                "Jak wyglądasz?",*/
                "O czym możesz rozmawiać?"

/*
                "Co słychać?"
*/
        };
        for(String userAnswer: userAnswers) {
            String answer = chatbot.answerQuestion(userAnswer);
            org.fest.assertions.api.Assertions.assertThat(answer).isIn(chatbotAnswersForQuestions);
        }
    }

}