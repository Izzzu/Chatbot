package com.academicprojects.model.capabilities;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class ConversationCapabilityTest{

    ConversationCapability conversationCapability;
    @Before
    public void setUp() throws IOException {
        conversationCapability = new ConversationCapability();
    }

    @Test
    public void shouldReturnListOfFeelingResponses() throws Exception {

        List<String> lines = conversationCapability.getFeelingStatementsForVerb("chcę");
        assertEquals(lines.size(), 7);
    }

    @Test
    public void shouldReturnEmptyListWhenFeelingVerbIsNotFound() throws IOException {

        List<String> lines = conversationCapability.getFeelingStatementsForVerb("robię");
        assertEquals(lines.size(), 0);
    }
    @Test
    public void shouldFilPatternAnswersForQuestionsAboutOpinionFromFile() throws IOException {
        Assert.assertEquals(7, conversationCapability.getPatternAnswerForOpinionQuestion().size());
    }

    @Test
    public void shouldFilPatternAnswersForPersonalQuestionFromFile() throws IOException {
        Assert.assertEquals(16, conversationCapability.getPatternAnswersForPersonalQuestion().size());
    }

    @Test
    public void shouldFillExceptionChatbptAnswerListFromFile() throws IOException {
        Assert.assertEquals(24, conversationCapability.getExceptionsChatbotAnswers().size());
    }

    @Test
    public void shouldFillStandardDialogs() {
        org.fest.assertions.api.Assertions.assertThat(conversationCapability.getStandardAnswersFor("dobranoc")).contains("dobranoc");        ;
    }
}