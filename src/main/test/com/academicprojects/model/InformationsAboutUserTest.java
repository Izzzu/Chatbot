package com.academicprojects.model;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class InformationsAboutUserTest {

    Brain brain =new Brain();
    Chatbot chatbot;


    @Before
    public void setUp() throws Exception {

        chatbot = new Chatbot();
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();

    }
    @Test
    public void catchUserGender() {
        String userAnswer = "Widziałam dzisiaj diabła";

        chatbot.updateInformationAboutUser(userAnswer);
        assertThat(Gender.FEMALE).isEqualTo(chatbot.getUser().getGender());
    }
}
