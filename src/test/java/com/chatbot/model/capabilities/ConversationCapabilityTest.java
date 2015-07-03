package com.chatbot.model.capabilities;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
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
        Assert.assertEquals(17, conversationCapability.getPatternAnswersForPersonalQuestion().size());
    }

    @Test
    public void shouldFillExceptionChatbptAnswerListFromFile() throws IOException {
        Assert.assertEquals(24, conversationCapability.getExceptionsChatbotAnswers().size());
    }

    @Test
    public void shouldFillStandardDialogs() {
        org.fest.assertions.api.Assertions.assertThat(conversationCapability.getStandardAnswersFor("dobranoc")).contains("dobranoc");        ;
    }
    @Test
    public void shouldFillPatternsReferringPersonality() {
        for (PersonalityId type: PersonalityId.values()) {

        org.fest.assertions.api.Assertions.assertThat(conversationCapability.getAnswersReferringPersonality().keySet()).contains(type.toString());
        }
    }
    @Test
    public void shouldReturnEmptyCollectionWhenThereIsNoMoreAnswers() {
        for (PersonalityId type: PersonalityId.values()) {

            conversationCapability.getAnswersReferringPersonality().get(PersonalityId.ASEKURACYJNY.toString()).clear();
        org.fest.assertions.api.Assertions.assertThat(conversationCapability.getAnswerReferringPersonality(PersonalityId.ASEKURACYJNY)).hasSize(0);
        }
    }

    @Test
    public void shouldgetAnswerById() {
        String answerReferringPersonality = conversationCapability.getAnswerReferringPersonality(PersonalityId.DOSTOSOWUJACY);
        assertNotNull(answerReferringPersonality);

    }
}