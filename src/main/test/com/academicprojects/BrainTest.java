package com.academicprojects;

import com.academicprojects.model.Brain;
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
        brain.setUpBrain();

    }

    @Test
    public void shouldFillChatbotAnswersListFromFile() throws IOException {
        Assert.assertEquals(51, brain.getChatbotAnswers().size());
    }

    @Test
    public void shouldFillPersonalityRecognizePhrasesListFromFile() throws IOException {
        Assert.assertEquals(389, brain.getPersonalityRecognizer().getPersonalityPhrases().size());
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