package com.chatbot;


import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.dictionary.PolishDictionary;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.SQLException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class AnsweringQuestionTest {
    Brain brain;
    Chatbot chatbot;

    @Before
    public void setUp() throws IOException, SQLException {
        brain = Mockito.spy(new Brain());
        chatbot = new Chatbot(brain);
    }

    @Test
    public void shouldAskBack() throws Exception {
        PolishDictionary dictionary = new PolishDictionary();
        String pattern = "A co Ty <verb>?";
        doReturn(pattern).when(brain).getRandomAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        Assertions.assertThat(chatbot.answerQuestion("Czu uważasz, że to fajne?")).isEqualTo("A co Ty uważasz?");
    }

    @Test
    public void shouldAskBackWithParaphrase() throws Exception {
        chatbot = new Chatbot(brain);
        PolishDictionary dictionary = new PolishDictionary();
        String s = "A Ty <verb>, że<paraphrase>?";
        doReturn(s).when(brain).getRandomAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        Assertions.assertThat(chatbot.answerQuestion("Czy uważasz, że to co robię jest złe?")).isEqualTo("A Ty uważasz, że to co robisz jest złe?");
    }

    @Test
    public void shouldAskBackWithParaphrase2() throws Exception {
        chatbot = new Chatbot(brain);
        PolishDictionary dictionary = new PolishDictionary();
        String s = "A Ty <verb>, że<paraphrase>?";
        doReturn(s).when(brain).getRandomAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        Assertions.assertThat(chatbot.answerQuestion(" Czy uważasz, że rozmowa z Tobą mi pomoże?")).isEqualTo("A Ty uważasz, że rozmowa z tobą mi pomoże?");
    }

    @Test
    public void shouldAnswerForStandardQuestionWithParaphrase() throws Exception {
        chatbot = new Chatbot(brain);
        PolishDictionary dictionary = new PolishDictionary();
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        doReturn(false).when(brain).isPronoun(anyString());
        Assertions.assertThat(chatbot.answerQuestion("Co tam?")).isEqualTo("Pytasz co tam.  ");
    }

    @Test
    public void shouldAnswerForMoreDificultStandardQuestionWithParaphrase() throws Exception {
        chatbot = new Chatbot(brain);
        PolishDictionary dictionary = new PolishDictionary();
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        doReturn(false).when(brain).isPronoun(anyString());
        Assertions.assertThat(chatbot.answerQuestion("Jaka jest dzisiaj pogoda?")).isEqualTo("Pytasz jaka jest dzisiaj pogoda.  ");
    }

    @Test
    public void shouldAnswerForMoreDificultStandardQuestionWithParaphrasedPronouns() throws Exception {
        chatbot = new Chatbot(brain);
        PolishDictionary dictionary = new PolishDictionary();
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        Assertions.assertThat(chatbot.answerQuestion("Jaki jest twój zawód?")).isEqualTo("Pytasz jaki jest mój zawód.  ");
    }

    @Test
    public void shouldRemoveConjuctionFromBegginingOfQuestionIfParaphrase() throws Exception {
        chatbot = new Chatbot(brain);
        PolishDictionary dictionary = new PolishDictionary();
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        Assertions.assertThat(chatbot.answerQuestion("A co mam mówić?")).isEqualTo("Pytasz co masz mówić.  ");
    }


    @Test
    public void shouldRecognizeQuestion() throws IOException, SQLException {
        chatbot = new Chatbot(brain);

        String[] userAnswers = {
                "Co robisz?",
                "Lubisz mnie?",
                //"Powiesz coś?",
                "Jak wyglądasz?",
                "Jestem kobietą, a Ty?"
/*
                "Co słychać?"
*/
        };
        for(String userAnswer: userAnswers) {
            String answer = chatbot.answerQuestion(userAnswer);
            assertThat(answer).isIn(brain.getPatternsForPersonalQuestions());
        }
    }


    /*@Test
    public void shouldAnswerForStandardQuestionWithParaphraseBe() throws Exception {
        chatbot = new Chatbot();
        PolishDictionary dictionary = new PolishDictionary();
        String chatbotAnswer = "<paraphrase>";
        doReturn(chatbotAnswer).when(brain).();
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.answerQuestion("Jesteś policjantem?")).isEqualTo(" ");
    }*/
}
