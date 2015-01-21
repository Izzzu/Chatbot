package com.academicprojects;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BrainTest {

    public Brain brain;
    public DbService db = null;

    @Before
    public void setUp() throws Exception {
        db = new DbService("db/chatbotDb");
        brain = new Brain(db);
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

}