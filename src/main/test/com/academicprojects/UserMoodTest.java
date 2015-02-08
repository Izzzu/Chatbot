package com.academicprojects;


import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserMoodTest {

    Brain brain;
    Chatbot chatbot;


    @Before
    public void setUp() throws Exception {
        chatbot = new Chatbot();
        brain = new Brain();
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();

    }
    @Test
    public void shouldCatchSimpleUserAnswer() throws Exception {
        String useranswer = "finanse";
        int actual = chatbot.catchUserAnswerNote(useranswer);
        //System.out.println("actual: "+ actual);
        Assert.assertEquals(5, actual);
    }

    @Test
    public void shouldCatchUserAnswerWhichIsSentence() throws Exception {
        String useranswer = "moj syn umarl";
        int actual = chatbot.catchUserAnswerNote(useranswer);
        //System.out.println("actual: "+ actual);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void shouldGetAnswerNoteForSingleWord() {
        String userAnswer = "niezle";
        int actual = chatbot.catchUserAnswerNote(userAnswer);
        Assert.assertEquals(5,actual);
    }
}
