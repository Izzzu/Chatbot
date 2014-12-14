package com.academicprojects.model;

import com.academicprojects.db.DbService;
import com.academicprojects.model.dictionary.PolishDictionary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Brain {
    public static final int NEUTRAL_ANSWER = 0;
    List<PatternAnswer> patterns = new ArrayList<PatternAnswer>();
    Set<ChatbotAnswer> chatbotAnswers = new HashSet<ChatbotAnswer>();
    Set<ChatbotAnswer> neutralChatbotAnswer = new HashSet<ChatbotAnswer>();
    Set<ChatbotAnswer> exceptionsChatbotAnswers = new HashSet<ChatbotAnswer>();
    PersonalityRecognizer personalityRecognizer = new PersonalityRecognizer();
    PolishDictionary dictionary = new PolishDictionary();

    private DbService db = null;
    public static List<PersonalityType> personalityTypes = new ArrayList<PersonalityType>();

    public Brain(DbService database) {
        db = database;
    }

    public void setUpBrain() {
        try {
            Connection conn = db.getConn();
            getPatternsFromDatabase(conn);
            getChatbotAnswersFromDatabase(conn);
            getPersonalityPhrasesFromDatabase(conn);
            getExceptionsChatbotAnswersFromDatabase(conn);
            getPersonalitiesFromDatabase(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getPersonalityPhrasesFromDatabase(Connection conn) throws SQLException {
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

    public void getPersonalitiesFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM PERSONALITY";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) personalityTypes.add(new PersonalityType(rs.getInt(1), 0, rs.getNString(2), rs.getNString(3)));
        System.out.println("size: "+personalityTypes.size());
    }

}