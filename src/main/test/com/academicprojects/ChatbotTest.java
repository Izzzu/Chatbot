package com.academicprojects;

import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatbotTest {

    Brain brain;
    Chatbot chatbot;


    @Before
    public void setUp() throws Exception {
        chatbot = new Chatbot();
        brain = new Brain();
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();

    }
    @Test
    public void testCatchUserAnswer() throws Exception {
        String useranswer = "finanse";
        int actual = chatbot.catchUserAnswerNote(useranswer);
        //System.out.println("actual: "+ actual);
        Assert.assertEquals(5, actual);
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
                "Sformułuj proszę pytanie inaczej."
        };

        String[] userAnswers = {
/*                "Co robisz?",
                "Lubisz mnie?",
                "Powiedz coś",
                "Jak wyglądasz?",*/
                ""

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