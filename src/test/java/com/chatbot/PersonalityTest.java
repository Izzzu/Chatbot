package com.chatbot;

import com.chatbot.model.core.Brain;
import com.chatbot.model.user.Personality;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static com.chatbot.model.capabilities.PersonalityId.DOSTOSOWUJACY;

public class PersonalityTest {

    @Before
    public void setUpBrain() throws SQLException {
        try {
            Brain brain = new Brain();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }
    }

    @Test
    public void shouldUpdatePersonality()
    {
        Personality p = new Personality();
        p.setNewPersonalityType(DOSTOSOWUJACY, 12);

        Assert.assertEquals((int)p.getById(DOSTOSOWUJACY).getLevel(), 12);
    }

    @Test public void shouldBe18PersonalityTypes() {

        Personality p = new Personality();
        Assert.assertEquals(18, p.getTypes().size());
    }


}