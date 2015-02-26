package com.academicprojects;

import com.academicprojects.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class ChatbotTest {

    Brain brain = Mockito.spy(new Brain());
    Chatbot chatbot;


    @Before
    public void setUp() throws Exception {

        chatbot = new Chatbot();
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
    public void shouldAnswerForOneWordStatement() throws Exception {

        String[] userAnswers = {
              "Interesujące.",
                "."
        };
        String toBeReturned = "Tylko_tyle?";
        doReturn(toBeReturned).when(brain).getRandomPatternForOneWordAnswer();
        doReturn(toBeReturned).when(brain).getRandomSuitedAnswersForNote(anyInt());
        for(String userAnswer: userAnswers) {
            String answer = chatbot.prepareAnswer(userAnswer);
            assertThat(answer).isEqualTo(toBeReturned);
        }
    }

    @Test
    public void shouldAnswerForPersonalQuestion() throws Exception {

        String[] userAnswers = {
                "Pomożesz mi?"
        };
        String toBeReturned = "";
        //doReturn(toBeReturned).when(brain).getPatternsForPersonalQuestions();
        doReturn(toBeReturned).when(brain).getRandomAnswerForPersonalQuestion();
        for(String userAnswer: userAnswers) {
            String answer = chatbot.prepareAnswer(userAnswer);
            assertThat(answer).isEqualTo(toBeReturned);
        }
    }

    @Test
    public void shouldAnswerForStandardDialog() throws Exception {

        String[] userAnswers = {
                "Dobranoc",
                "dobranoc",
                "jaki jest twój ulubiony kolor?",
                "co porabiasz?"
        };
        String toBeReturned = "Dobranoc";
        doReturn(toBeReturned).when(brain).getRandomStandardAnswer(anyString());
        //doReturn(toBeReturned).when(brain).get(anyInt());
        for(String userAnswer: userAnswers) {
            String answer = chatbot.prepareAnswer(userAnswer);
            assertThat(answer).isEqualTo(toBeReturned);
        }
    }




}