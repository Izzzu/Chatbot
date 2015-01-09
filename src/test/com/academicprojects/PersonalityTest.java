package com.academicprojects;

import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Personality;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class PersonalityTest {

    @Before
    public void setUpBrain() throws SQLException {
        DbService db = null;
        try {
            db = new DbService("db/chatbotDb");
            Brain brain = new Brain(db);
            brain.setUpBrain();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.shutdown();

        }
    }

    @Test
    public void shouldUpdatePersonality()
    {
        Personality p = new Personality();
        p.setNewPersonalityType(1, 12);

        Assert.assertEquals(p.getById(1), 12);
    }

    @Test public void shouldBe18PersonalityTypes() {

        Personality p = new Personality();
        Assert.assertEquals(18, p.getTypes().size());
    }


}