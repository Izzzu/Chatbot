package com.chatbot.model.capabilities;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class UnderstandingCapabilityTest {

    private UnderstandingCapability understandingCapability;

    @Before
    public void setUp() throws IOException, SQLException {
        understandingCapability = new UnderstandingCapability();
    }
    @Test
    public void shouldFillPatternsUserAnswersListFromFile() throws IOException {
        Assert.assertEquals(280, understandingCapability.getComplexPatterns().size());
        Assert.assertEquals(59, understandingCapability.getOneWordPatterns().size());
    }
    @Test
    public void shouldFillTopicsListFromFile() throws IOException {
        Assert.assertEquals(11, understandingCapability.getTopics().size());
    }

}