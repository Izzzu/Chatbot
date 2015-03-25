package com.chatbot;

import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.dictionary.PolishDictionary;
import com.chatbot.model.exceptions.NotFoundResponsesForFeelingSentence;
import com.chatbot.model.user.Gender;
import com.chatbot.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.SQLException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class FeelingAnswersTest {

    PolishDictionary dictionary = new PolishDictionary();
    Brain brain;

    @Before
    public void setUp() throws IOException, SQLException {
        brain = Mockito.spy(new Brain());
    }

    @Test
    public void shouldResponseForJestem() throws NotFoundResponsesForFeelingSentence, IOException {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(brain ,user);
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();
        doReturn(false).when(brain).isPronoun(anyString());

        String statement = "Co jest powodem tego, że<paraphrase>?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb("jestem");

        assertThat(chatbot.getChatbotResponseForFeelingSentence("Jestem piękna")).isEqualTo("Co jest powodem tego, że jesteś piękna?");
    }

    @Test
    public void shouldResponseForChce() throws NotFoundResponsesForFeelingSentence {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(brain,user);
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();
        doReturn(false).when(brain).isPronoun(anyString());

        String statement = "Czy to twoje najważniejsze pragnienie?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());

        assertThat(chatbot.getChatbotResponseForFeelingSentence("Chcę zarabiać więcej pieniędzy.")).isEqualTo("Czy to twoje najważniejsze pragnienie?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Chce zarabiać więcej pieniędzy.")).isEqualTo("Czy to twoje najważniejsze pragnienie?");
    }

    @Test
    public void shouldResponseForCzuje() throws NotFoundResponsesForFeelingSentence {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(brain,user);


        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();
        doReturn(false).when(brain).isPronoun(anyString());

        String statement = "To bardzo ważne, żeby głośno mówić o swoich uczuciach.";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());


        assertThat(chatbot.getChatbotResponseForFeelingSentence("Czuję się fatalnie.")).isEqualTo("To bardzo ważne, żeby głośno mówić o swoich uczuciach");

    }

    @Test
    public void shouldResponseForJestemNegation() throws NotFoundResponsesForFeelingSentence {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(brain, user);

        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();
        doReturn(false).when(brain).isPronoun(anyString());

        String statement = "Co jest powodem tego, że<paraphrase>?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());


        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie jestem piękna")).isEqualTo("Co jest powodem tego, że nie jesteś piękna?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie chcę tego")).isEqualTo("Co jest powodem tego, że nie chcesz tego?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie czuję tego")).isEqualTo("Co jest powodem tego, że nie czujesz tego?");

    }

    @Test
    public void shouldResponseForNegativeFeeling() throws Exception {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(brain,user);
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();
        doReturn(false).when(brain).isPronoun(anyString());
        doReturn(false).when(brain).standardDialogsContainsAnswer(anyString());
        String statement = "Często czujesz, że<paraphrase>?";
        doReturn(statement).when(brain).getRandomFeelingStatementForVerb(anyString());

        assertThat(chatbot.prepareAnswer("Nie chcę już żyć.")).isEqualTo("Często czujesz, że nie chcesz już żyć?");
    }



}