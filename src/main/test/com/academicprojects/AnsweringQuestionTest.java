package com.academicprojects;


import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;

public class AnsweringQuestionTest {
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
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.answerQuestion("Co tam?")).isEqualTo("Pytasz co tam.  ");
    }

    @Test
    public void shouldAnswerForMoreDificultStandardQuestionWithParaphrase() throws Exception {
        chatbot = new Chatbot();
        PolishDictionary dictionary = new PolishDictionary();
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        chatbot.brain = brain;
        Assertions.assertThat(chatbot.answerQuestion("Jaka jest dzisiaj pogoda?")).isEqualTo("Pytasz jaka jest dzisiaj pogoda.  ");
    }

    @Test
    public void shouldAnswerForMoreDificultStandardQuestionWithParaphrasedPronouns() throws Exception {
        chatbot = new Chatbot();
        chatbot.brain = brain;
        brain.setUpBrain();
        PolishDictionary dictionary = new PolishDictionary();
        doReturn(" ").when(brain).getRandomSuitedAnswersForNote(anyInt());
        doReturn(dictionary).when(brain).getDictionary();
        Assertions.assertThat(chatbot.answerQuestion("Jaki jest twój zawód?")).isEqualTo("Pytasz jaki jest mój zawód.  ");
    }

    @Test
    public void shouldRecognizeQuestion() {

        String[] userAnswers = {
                "Co robisz?",
                "Lubisz mnie?",
                "Powiedz coś",
                "Jak wyglądasz?"
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
