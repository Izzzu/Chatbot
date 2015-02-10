package com.academicprojects;

import com.academicprojects.model.Brain;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BrainTest {

    public Brain brain;

    @Before
    public void setUp() throws Exception {
        brain = new Brain();
        brain.setUpBrain();

    }


    @Test
    public void shouldFillPatternsUserAnswersListFromFile() throws IOException {
        Assert.assertEquals(274, brain.getComplexPatterns().size());
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
    public void shouldFillExceptionChatbptAnswerListFromFile() throws IOException {
        Assert.assertEquals(24, brain.getExceptionsChatbotAnswers().size());
    }





}