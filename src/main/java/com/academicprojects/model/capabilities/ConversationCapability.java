package com.academicprojects.model.capabilities;


import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConversationCapability {
    private List<FeelingStatement> feelingStatements = new LinkedList<>();
    private List<String> patternsForOneWordAnswers = new LinkedList<>();
    private List<String> patternAnswersForPersonalQuestion = new LinkedList<String>();
    private List<String> patternAnswerForOpinionQuestion = new LinkedList<String>();

    public ConversationCapability() throws IOException {
        fillFeelingStatementMap("jestem", new File("src/main/resources/patternAnswersForFeelingStatementsWithBe.csv"));
        fillFeelingStatementMap("czuję", new File("src/main/resources/patternAnswersForFeelingStatementsWithFeel.csv"));
        fillFeelingStatementMap("chcę", new File("src/main/resources/patternAnswersForFeelingStatementsWithWant.csv"));
        fillPatternsForOneWordAnswer(new File("src/main/resources/chatbotAnswersForOneWord.csv"));
        fillPatternAnswersForPersonalQuestions(new File("src/main/resources/patternForPersonalQuestions.csv"));
        fillPatternAnswersForOpinionQuestions(new File("src/main/resources/patternAnswersForQuestionsAboutOpinion.csv"));
    }

    public List<String> getPatternAnswersForPersonalQuestion() {
        return ImmutableList.copyOf(patternAnswersForPersonalQuestion);
    }
    public List<String> getPatternAnswerForOpinionQuestion() {
        return ImmutableList.copyOf(patternAnswerForOpinionQuestion);
    }
    public List<String> getPatternsForOneWordAnswers() {
        return ImmutableList.copyOf(patternsForOneWordAnswers);
    }


    private void fillFeelingStatementMap(String verb, File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        List<String> list = new ArrayList<String>();
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            list.add(tab[0].replace("_", " "));
        }
        feelingStatements.add(new FeelingStatement(verb, list));
        br.close();
    }

    private void fillPatternsForOneWordAnswer(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            patternsForOneWordAnswers.add(tab[0].replace("_", " "));
        }
        br.close();
    }

    private void fillPatternAnswersForOpinionQuestions(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            patternAnswerForOpinionQuestion.add(tab[0].replace("_", " "));
        }
        br.close();
    }

    private void fillPatternAnswersForPersonalQuestions(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            patternAnswersForPersonalQuestion.add(tab[0].replace("_", " "));
        }
        br.close();
    }

    public List<String> getFeelingStatementsForVerb(String verb) {
        for(FeelingStatement statement : feelingStatements) {
            if(statement.getFeelingVerb().equals(verb)) {
                return statement.getResponses();
            }
        }
        return ImmutableList.of();
    }
}
