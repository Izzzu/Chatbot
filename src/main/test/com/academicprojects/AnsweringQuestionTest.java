package com.academicprojects;


import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.ChatbotAnswer;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.doReturn;

public class AnsweringQuestionTest {
    Brain brain1 = new Brain();
    Brain brain = Mockito.spy(new Brain());
    Chatbot chatbot;


    @Test
    public void shouldAskBack() throws Exception {
        chatbot = new Chatbot();
        PolishDictionary dictionary = new PolishDictionary();
        String pattern = "A co Ty <verb>?";
        doReturn(pattern).when(brain).getRandomAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.answerQuestion("Czu uważasz, że to fajne?")).isEqualTo("A co Ty uważasz?");
    }

    @Test
    public void shouldAskBackWithParaphrase() throws Exception {
        chatbot = new Chatbot();
        PolishDictionary dictionary = new PolishDictionary();
        String s = "A Ty <verb>, że<paraphrase>?";
        doReturn(s).when(brain).getRandomAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.answerQuestion("Czy uważasz, że to co robię jest złe?")).isEqualTo("A Ty uważasz, że to co robisz jest złe?");
    }

    @Test
    public void shouldAskBackWithParaphrase2() throws Exception {
        chatbot = new Chatbot();
        PolishDictionary dictionary = new PolishDictionary();
        String s = "A Ty <verb>, że<paraphrase>?";
        doReturn(s).when(brain).getRandomAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.answerQuestion(" Czy uważasz, że rozmowa z Tobą mi pomoże?")).isEqualTo("A Ty uważasz, że rozmowa z tobą mi pomoże?");
    }



    @Test
    public void shouldAnswerForStandardQuestionWithParaphrase() throws Exception {
        chatbot = new Chatbot();
        PolishDictionary dictionary = new PolishDictionary();
        ChatbotAnswer chatbotAnswer = new ChatbotAnswer("", 0);
        Set<ChatbotAnswer> patterns = new HashSet<>(Arrays.asList(chatbotAnswer));
        doReturn(patterns).when(brain).getChatbotAnswers();
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.getAnswerForQuestion("co tam")).isEqualTo("Pytasz co tam. ");
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
