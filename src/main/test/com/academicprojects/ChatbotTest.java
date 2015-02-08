package com.academicprojects;

import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.TypeOfSentence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

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
    /*@Test
    public void shouldThrowExceptionWhen*/
    @Test
    public void testPersonalityRecognizer() throws Exception {

        Assert.assertFalse(chatbot.brain.getPersonalityRecognizer().getPersonalityPhrases().isEmpty());
    }

    @Test
    public void shouldRecognizeTypeOfSentenceAsSingleWord() {
        String s = "word ";
        assertThat(TypeOfSentence.SINGLE_WORD).isEqualTo(chatbot.recognizeTypeOfSentence(s));
    }

    @Test
    public void shouldNotRecognizeTypeOfSentenceAsSingleWord() {
        String s = "word cos";
        assertThat(TypeOfSentence.SINGLE_WORD).isNotEqualTo(chatbot.recognizeTypeOfSentence(s));
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
            assertThat(answer).isIn(chatbotAnswersForQuestions);
        }
    }


}