package com.academicprojects.model;

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
    public static final int NEUTRAL_ANSWER = 0;
    List<PatternAnswer> patterns = new ArrayList<PatternAnswer>();
    List<PatternAnswer> oneWordPatterns = new ArrayList<PatternAnswer>();
    Set<ChatbotAnswer> chatbotAnswers = new HashSet<ChatbotAnswer>();
    Set<ChatbotAnswer> neutralChatbotAnswer = new HashSet<ChatbotAnswer>();
    List<ChatbotAnswer> exceptionsChatbotAnswers = new LinkedList<ChatbotAnswer>();
    PersonalityRecognizer personalityRecognizer = new PersonalityRecognizer();
    private LinkedList<String> patternAnswersForPersonalQuestion = new LinkedList<String>();
    private LinkedList<String> patternAnswerForOpinionQuestion = new LinkedList<String>();
    private Map<String, List<String>> feelingStatement = new HashMap<String, List<String>>();
    private List<String> patternsForOneWordAnswers = new LinkedList<>();
    private ActiveListening activeListening;
    PolishDictionary dictionary = new PolishDictionary();



    public void setUpBrain() throws IOException {
        try {
            //getPatternsFromDatabase(conn);
//            getChatbotAnswersFromDatabase(conn);
//            getPersonalityPhrasesFromDatabase(conn);
//            getExceptionsChatbotAnswersFromDatabase(conn);
//            getPersonalitiesFromDatabase(conn);
            getUserAnswersFromFile(new File("src/main/resources/useranswers.csv"));
            getChatbotAnswersFromFile(new File("src/main/resources/chatbotanswers.csv"));
            getPersonalityPhrasesFromFile(new File("src/main/resources/personalityphrases.csv"));
            getExceptionAnswersFromFile(new File("src/main/resources/exceptionChatbotAnswers.csv"));
            fillPatternAnswersForPersonalQuestions(new File("src/main/resources/patternForPersonalQuestions.csv"));
            fillPatternAnswersForOpinionQuestions(new File("src/main/resources/patternAnswersForQuestionsAboutOpinion.csv"));
            getParaphrases(new File("src/main/resources/paraphrases.csv"));
            fillFeelingStatementMap("jestem", new File("src/main/resources/patternAnswersForFeelingStatementsWithBe.csv"));
            fillFeelingStatementMap("czuję", new File("src/main/resources/patternAnswersForFeelingStatementsWithFeel.csv"));
            fillFeelingStatementMap("chcę", new File("src/main/resources/patternAnswersForFeelingStatementsWithWant.csv"));
            fillPatternsForOneWordAnswer(new File("src/main/resources/chatbotAnswersForOneWord.csv"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
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

    private void fillFeelingStatementMap(String verb, File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        List<String> list = new ArrayList<String>();
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            list.add(tab[0].replace("_", " "));
        }
        feelingStatement.put(verb, list);
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
                patterns.add(new PatternAnswer(Integer.valueOf(tab[1]), tab[0].replace("_", " "), Integer.valueOf(tab[3])));
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
        Collections.sort(patterns,comparator);
        Collections.sort(oneWordPatterns, comparator);
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
            patterns.add(new PatternAnswer(rs.getInt(4), rs.getNString(2).replace("_", " "), rs.getInt(5)));
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


  /*  public void getPersonalitiesFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM PERSONALITY";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) personalityTypes.add(new PersonalityType(rs.getInt(1), rs.getNString(3), rs.getNString(2), 0));
        System.out.println("size: "+personalityTypes.size());
    }*/


}