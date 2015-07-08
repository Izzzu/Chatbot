package com.chatbot.model.capabilities;

import com.chatbot.model.core.Topic;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.api.Assertions.assertThat;

public class UnderstandingCapabilityTest {

    private UnderstandingCapability understandingCapability;

    @Before
    public void setUp() throws IOException, SQLException {
        understandingCapability = new UnderstandingCapability();
    }
    @Test
    public void shouldFillPatternsUserAnswersListFromFile() throws IOException {
        assertEquals(280, understandingCapability.getComplexPatterns().size());
        assertEquals(59, understandingCapability.getOneWordPatterns().size());
    }
    @Test
    public void shouldFillTopicsListFromFile() throws IOException {
        assertEquals(11, understandingCapability.getTopics().size());
    }

    @Test
    public void shouldFindRightTopic() {
        Topic brak_pracy = understandingCapability.getTopic("BRAK_PRACY");
        assertThat(brak_pracy.getTopicId()).isEqualTo("BRAK_PRACY");
        assertThat(brak_pracy.getLcu()).isEqualTo(47);
    }

}