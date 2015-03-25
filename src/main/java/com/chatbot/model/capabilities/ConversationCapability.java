package com.chatbot.model.capabilities;


import com.chatbot.model.answer.ChatbotAnswer;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConversationCapability {
    private List<FeelingStatement> feelingStatements = new LinkedList<>();
    private List<String> patternsForOneWordAnswers = new LinkedList<>();
    private List<String> patternAnswersForPersonalQuestion = new LinkedList<String>();
    private List<String> patternAnswerForOpinionQuestion = new LinkedList<String>();
    List<StandardDialog> standardDialogs = new ArrayList<>();
    List<ChatbotAnswer> chatbotAnswers = new LinkedList<>();
    List<String> exceptionsChatbotAnswers = new LinkedList<String>();

    public ConversationCapability() throws IOException {
        fillFeelingStatementMap("jestem", new File("src/main/resources/patternAnswersForFeelingStatementsWithBe.csv"));
        fillFeelingStatementMap("czuję", new File("src/main/resources/patternAnswersForFeelingStatementsWithFeel.csv"));
        fillFeelingStatementMap("chcę", new File("src/main/resources/patternAnswersForFeelingStatementsWithWant.csv"));
        fillPatternsForOneWordAnswer(new File("src/main/resources/chatbotAnswersForOneWord.csv"));
        fillPatternAnswersForPersonalQuestions(new File("src/main/resources/patternForPersonalQuestions.csv"));
        fillPatternAnswersForOpinionQuestions(new File("src/main/resources/patternAnswersForQuestionsAboutOpinion.csv"));
        getChatbotAnswersFromFile(new File("src/main/resources/chatbotanswers.csv"));
        getExceptionAnswersFromFile(new File("src/main/resources/exceptionChatbotAnswers.csv"));
        getStandardDialogFromFile(new File("src/main/resources/standardDialogs.json"));
    }

    private void getStandardDialogFromFile(File file) throws IOException {
/*
        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
*/
        FileReader fileReader = new FileReader(file);

        Gson gson = new Gson();
        Type type = new TypeToken<List<StandardDialog>>(){}.getType();
        standardDialogs = gson.fromJson(fileReader, type);
    }

    public List<String> getPatternAnswersForPersonalQuestion() {
        return ImmutableList.copyOf(patternAnswersForPersonalQuestion);
    }
    public List<String> getPatternAnswerForOpinionQuestion() {
        return ImmutableList.copyOf(patternAnswerForOpinionQuestion);
    }

    public List<ChatbotAnswer> getChatbotAnswers() {
        return ImmutableList.copyOf(chatbotAnswers);
    }

    public List<String> getExceptionsChatbotAnswers() {

        return ImmutableList.copyOf(exceptionsChatbotAnswers);
    }

    private void getChatbotAnswersFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            chatbotAnswers.add(new ChatbotAnswer(tab[0].replace("_", " "), Integer.valueOf(tab[1])));
        }
        br.close();
    }

    private void getExceptionAnswersFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            exceptionsChatbotAnswers.add(tab[0].replace("_", " "));

        }
        br.close();
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

    public List<String> getStandardAnswersFor(String answer) {
        for (StandardDialog standardDialog : standardDialogs) {
            if(answer.equals(standardDialog.getUserAnswer())) {
                return standardDialog.getChatbotResponses();
            }
        }
        return ImmutableList.of();
    }

    public String getRandomStandardAnswer(String s) {
        for (StandardDialog standardDialog : standardDialogs) {
            if(standardDialog.getUserAnswer().equals(s)) {
                return standardDialog.getRandomChatbotResponse();
            }
        }
        return "";
    }
}
