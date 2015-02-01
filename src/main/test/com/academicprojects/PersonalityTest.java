package com.academicprojects;

import com.academicprojects.model.Brain;
import com.academicprojects.model.Personality;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class PersonalityTest {

    @Before
    public void setUpBrain() throws SQLException {
        try {
            Brain brain = new Brain();
            brain.setUpBrain();
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
        p.setNewPersonalityType(1, 12);

        Assert.assertEquals(p.getById(1).getLevel(), 12);
    }

    @Test public void shouldBe18PersonalityTypes() {

        Personality p = new Personality();
        Assert.assertEquals(18, p.getTypes().size());
    }


}