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
        Assert.assertEquals(329, brain.getPatterns().size());
    }

    @Test
    public void shouldFillChatbotAnswersListFromFile() throws IOException {
        Assert.assertEquals(37, brain.getChatbotAnswers().size());
    }

    @Test
    public void shouldFillPersonalityRecognizePhrasesListFromFile() throws IOException {
        Assert.assertEquals(389, brain.getPersonalityRecognizer().getPersonalityPhrases().size());
    }

    @Test
    public void shouldFillExceptionChatbptAnswerListFromFile() throws IOException {
        Assert.assertEquals(31, brain.getExceptionsChatbotAnswers().size());
    }

    @Test
    public void shouldFilPatternAnswersForPersonalQuestionFromFile() throws IOException {
        Assert.assertEquals(16, brain.getPatternAnswersForPersonalQuestion().size());
    }

    @Test
    public void shouldFillMapWithAnswersForFeelingStatements() {
        Assert.assertEquals(3, brain.getFeelingStatement().values().size());
    }

    @Test
    public void shouldFilPatternAnswersForQuestionsAboutOpinionFromFile() throws IOException {
        Assert.assertEquals(7, brain.getPatternAnswerForOpinionQuestion().size());
    }

}