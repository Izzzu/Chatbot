package com.academicprojects.model;

import com.academicprojects.model.capabilities.ConversationCapability;
import com.academicprojects.model.dictionary.PolishDictionary;
import com.academicprojects.util.RandomSearching;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Brain {
    private ConversationCapability conversationCapability;
    List<PatternAnswer> complexPatterns = new ArrayList<PatternAnswer>();
    List<PatternAnswer> oneWordPatterns = new ArrayList<PatternAnswer>();
    Set<ChatbotAnswer> chatbotAnswers = new HashSet<ChatbotAnswer>();
    List<ChatbotAnswer> exceptionsChatbotAnswers = new LinkedList<ChatbotAnswer>();
    PersonalityRecognizer personalityRecognizer = new PersonalityRecognizer();
    private ActiveListening activeListening;
    PolishDictionary dictionary = new PolishDictionary();

    public void setUpBrain() throws IOException {
        try {
            //getPatternsFromDatabase(conn);
//            getChatbotAnswersFromDatabase(conn);
//            getPersonalityPhrasesFromDatabase(conn);
//            getExceptionsChatbotAnswersFromDatabase(conn);
//            getPersonalitiesFromDatabase(conn);
            conversationCapability = new ConversationCapability();
            getUserAnswersFromFile(new File("src/main/resources/useranswers.csv"));
            getChatbotAnswersFromFile(new File("src/main/resources/chatbotanswers.csv"));
            getPersonalityPhrasesFromFile(new File("src/main/resources/personalityphrases.csv"));
            getExceptionAnswersFromFile(new File("src/main/resources/exceptionChatbotAnswers.csv"));
            getParaphrases(new File("src/main/resources/paraphrases.csv"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void getParaphrases(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        List<String> list = new ArrayList<String>();
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            list.add(tab[0].replace("_", " "));
        }
        activeListening = new ActiveListening(list);
        br.close();
    }


    private void getExceptionAnswersFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            exceptionsChatbotAnswers.add(new ChatbotAnswer(tab[0].replace("_", " "), -1));

        }
        br.close();
    }

    private void getPersonalityPhrasesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            personalityRecognizer.addPersonalityPhrase(tab[0].replace("_", " "), Integer.valueOf(tab[1]), Integer.valueOf(tab[2]));

        }
        br.close();
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

    private void getUserAnswersFromFile(File filePattern) throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader(filePattern));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            if(tab[0].contains("_")) {
                complexPatterns.add(new PatternAnswer(Integer.valueOf(tab[1]), tab[0].replace("_", " "), Integer.valueOf(tab[3])));
            }
            else {
                oneWordPatterns.add(new PatternAnswer(Integer.valueOf(tab[1]), tab[0], Integer.valueOf(tab[3])));
            }
        }
        Comparator<PatternAnswer> comparator = new Comparator<PatternAnswer>() {
            @Override
            public int compare(PatternAnswer s, PatternAnswer t1) {
                int firstSentenceLength = s.getSentence().length();
                int secondSentenceLength = t1.getSentence().length();
                if(firstSentenceLength > secondSentenceLength) return -1;
                if(firstSentenceLength < secondSentenceLength) return 1;
                return 0;
            }
        };
        Collections.sort(complexPatterns,comparator);
        Collections.sort(oneWordPatterns, comparator);
        br.close();
    }

    /*private void getPersonalityPhrasesFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM PHRASE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) personalityRecognizer.addPersonalityPhrase(rs.getString(2), rs.getInt(3), rs.getInt(4));
    }

    public void getChatbotAnswersFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM CHATBOTANSWERS";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ChatbotAnswer chatbotAnswer = new ChatbotAnswer(rs.getNString(2).replace('\\', ' '), rs.getInt(3));
            if(chatbotAnswer.userAnswerNote== NEUTRAL_ANSWER) {
                neutralChatbotAnswer.add(chatbotAnswer);
            }
            else {
                chatbotAnswers.add(chatbotAnswer);
            }
        }
    }

    public void getPatternsFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM USERANSWERS";
        PreparedStatement ps;// = conn.prepareStatement(sql);
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            complexPatterns.add(new PatternAnswer(rs.getInt(4), rs.getNString(2).replace("_", " "), rs.getInt(5)));
        }
    }

    public void getExceptionsChatbotAnswersFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM EXCEPTIONS_CHATBOTANSWERS";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next())
            exceptionsChatbotAnswers.add(new ChatbotAnswer(rs.getNString(2).replace('\\', ' '), rs.getInt(3)));
    }
*/
    public String startParaphrase() {

        List<String> paraphraseStart = activeListening.getParaphraseStart();
        return paraphraseStart.get(RandomSearching.generateRandomIndex(paraphraseStart.size()));

    }

    public PatternAnswer getOneWordPattern(String word) {
        for(PatternAnswer answer: getOneWordPatterns()) {
            if(answer.getSentence().equals(word)) {
                return answer;
            }
        }
        return null;
    }

    public String getRandomFeelingStatementForVerb(String verb) throws NotFoundResponsesForFeelingSentence {
        List<String> feelingStatementsForVerb = conversationCapability.getFeelingStatementsForVerb(verb);
        int size = feelingStatementsForVerb.size();
        if(size<=0) {
            throw new NotFoundResponsesForFeelingSentence(verb);
        }
        int randomIndex = RandomSearching.generateRandomIndex(size);
        return feelingStatementsForVerb.get(randomIndex);
    }

    public String getRandomAnswerForOpinionQuestion() throws NotFoundResponsesForOpinionQuestion {
        List<String> answersForOpinionQuestion = conversationCapability.getPatternAnswerForOpinionQuestion();
        int size = answersForOpinionQuestion.size();
        if(size<=0) {
            throw new NotFoundResponsesForOpinionQuestion();
        }
        int randomIndex = RandomSearching.generateRandomIndex(size);
        return answersForOpinionQuestion.get(randomIndex);
    }

    public String getRandomPatteRnForOneWordAnswer() throws NotFoundResponseForOneWordAnswer {
        List<String> answersForOneWordSentence = conversationCapability.getPatternsForOneWordAnswers();
        int size = answersForOneWordSentence.size();
        if(size<=0) {
            throw new NotFoundResponseForOneWordAnswer();
        }
        int randomIndex = RandomSearching.generateRandomIndex(size);
        return answersForOneWordSentence.get(randomIndex);

    }


  /*  public void getPersonalitiesFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM PERSONALITY";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) personalityTypes.add(new PersonalityType(rs.getInt(1), rs.getNString(3), rs.getNString(2), 0));
        System.out.println("size: "+personalityTypes.size());
    }*/


}