package com.academicprojects;

import com.academicprojects.model.*;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class FeelingAnswersTest {

    Brain brain = Mockito.spy(new Brain());
    PolishDictionary dictionary = new PolishDictionary();


    @Test
    public void shouldResponseForJestem() throws NotFoundResponsesForFeelingSentence, IOException {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        chatbot.brain = brain;
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();
        String statement = "Co jest powodem tego, że<paraphrase>?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb("jestem");

        assertThat(chatbot.getChatbotResponseForFeelingSentence("Jestem piękna")).isEqualTo("Co jest powodem tego, że jesteś piękna?");
    }

    @Test
    public void shouldResponseForChce() throws NotFoundResponsesForFeelingSentence {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        String statement = "Czy to twoje najważniejsze pragnienie?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());

        chatbot.brain = brain;
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Chcę zarabiać więcej pieniędzy.")).isEqualTo("Czy to twoje najważniejsze pragnienie?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Chce zarabiać więcej pieniędzy.")).isEqualTo("Czy to twoje najważniejsze pragnienie?");
    }

    @Test
    public void shouldResponseForCzuje() throws NotFoundResponsesForFeelingSentence {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);


        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        String statement = "To bardzo ważne, żeby głośno mówić o swoich uczuciach.";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());


        chatbot.brain = brain;
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Czuję się fatalnie.")).isEqualTo("To bardzo ważne, żeby głośno mówić o swoich uczuciach");

    }

    @Test
    public void shouldResponseForJestemNegation() throws NotFoundResponsesForFeelingSentence {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        chatbot.brain = brain;

        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        String statement = "Co jest powodem tego, że<paraphrase>?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());


        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie jestem piękna")).isEqualTo("Co jest powodem tego, że nie jesteś piękna?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie chcę tego")).isEqualTo("Co jest powodem tego, że nie chcesz tego?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie czuję tego")).isEqualTo("Co jest powodem tego, że nie czujesz tego?");

    }



}