package com.chatbot;

import com.chatbot.model.core.Brain;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class BrainTest {

    public Brain brain;

    @Before
    public void setUp() throws Exception {
        brain = new Brain();

    }

    @Test
    public void shouldFillChatbotAnswersListFromFile() throws IOException {
        Assert.assertFalse(brain.getChatbotAnswers().isEmpty());
    }

    @Test
    public void shouldFillPersonalityRecognizePhrasesListFromFile() throws IOException {
        Assert.assertFalse(brain.getPersonalityRecognizer().getPersonalityPhrases().isEmpty());
    }

    @Test
    public void shouldRetunrRightRandomStandardAnswer() throws Exception {

        String[] userAnswers = {

                "co porabiasz?"
        };
        String toBeReturned = "Dobranoc";
       // doReturn(toBeReturned).when(brain).getRandomStandardAnswer(anyString());
        //doReturn(toBeReturned).when(brain).get(anyInt());
        for(String userAnswer: userAnswers) {
            List<String> standardAnswersFor = brain.getConversationCapability().getStandardAnswersFor(userAnswer.replace("?",""));
            String answer = brain.getRandomStandardAnswer(userAnswer.replace("?",""));
            assertThat(standardAnswersFor).contains(answer);
        }
    }



}