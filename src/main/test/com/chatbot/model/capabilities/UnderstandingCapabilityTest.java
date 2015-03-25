package com.chatbot.model.capabilities;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class UnderstandingCapabilityTest {

    private  UnderstandingCapability understandingCapability;

    @Before
    public void setUp() throws IOException, SQLException {
        understandingCapability = new UnderstandingCapability();
    }
    @Test
    public void shouldFillPatternsUserAnswersListFromFile() throws IOException {
        Assert.assertEquals(275, understandingCapability.getComplexPatterns().size());
        Assert.assertEquals(56, understandingCapability.getOneWordPatterns().size());
    }



}