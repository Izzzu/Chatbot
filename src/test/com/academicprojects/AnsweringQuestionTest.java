package com.academicprojects;


import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.LinkedList;

import static org.mockito.Mockito.doReturn;

public class AnsweringQuestionTest {
    DbService db = null;
    Brain brain1 = new Brain(db);
    Brain brain = Mockito.spy(new Brain(db));
    Chatbot chatbot = new Chatbot();

    @Test
    public void shouldAskBack() throws Exception {

        PolishDictionary dictionary = new PolishDictionary();
        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("A co Ty <verb>?"));
        doReturn(patterns).when(brain).getPatternAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        LinkedList<String> patternAnswerForOpinionQuestion = brain.getPatternAnswerForOpinionQuestion();
        chatbot.brain = brain;
        Assertions.assertThat(patternAnswerForOpinionQuestion).hasSize(1);
        Assertions.assertThat(chatbot.answerQuestion("Czu uważasz, że to fajne?")).isEqualTo("A co Ty uważasz?");
    }

    @Test
    public void shouldAskBackWithParaphrase() throws Exception {

        PolishDictionary dictionary = new PolishDictionary();
        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("A Ty <verb>, że<paraphrase>?"));
        doReturn(patterns).when(brain).getPatternAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        LinkedList<String> patternAnswerForOpinionQuestion = brain.getPatternAnswerForOpinionQuestion();
        chatbot.brain = brain;
        Assertions.assertThat(patternAnswerForOpinionQuestion).hasSize(1);
        Assertions.assertThat(chatbot.answerQuestion("Czy uważasz, że to co robię jest złe?")).isEqualTo("A Ty uważasz, że to co robisz jest złe?");
    }

    @Test
    public void shouldAskBackWithParaphrase2() throws Exception {

        PolishDictionary dictionary = new PolishDictionary();
        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("A Ty <verb>, że<paraphrase>?"));
        doReturn(patterns).when(brain).getPatternAnswerForOpinionQuestion();
        doReturn(dictionary).when(brain).getDictionary();
        LinkedList<String> patternAnswerForOpinionQuestion = brain.getPatternAnswerForOpinionQuestion();
        chatbot.brain = brain;
        Assertions.assertThat(patternAnswerForOpinionQuestion).hasSize(1);
        Assertions.assertThat(chatbot.answerQuestion(" Czy uważasz, że rozmowa z Tobą mi pomoże?")).isEqualTo("A Ty uważasz, że rozmowa z Tobą mi pomoże?");
    }
}
