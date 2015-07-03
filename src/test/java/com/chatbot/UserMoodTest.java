package com.chatbot;


import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.core.Topic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class UserMoodTest {

    Brain brain;
    Chatbot chatbot;


    @Before
    public void setUp() throws Exception {
        brain = Mockito.spy(new Brain());
        chatbot = new Chatbot(brain);


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
        doReturn(new Topic(("BRAK_PRACY"), 12, "")).when(brain).getTopic(any(String.class));
        String useranswer = "moja dziewczyna zaszla w ciaze";
        int actual = chatbot.catchUserAnswerNote(useranswer);
        //System.out.println("actual: "+ actual);
        Assert.assertEquals(6, actual);

    }

    @Test
    public void shouldGetAnswerNoteForSingleWord() {
        String userAnswer = "finanse";
        int actual = chatbot.catchUserAnswerNote(userAnswer);
        Assert.assertEquals(5,actual);
    }
}
