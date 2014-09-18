package com.academicprojects.model;

import com.academicprojects.db.DbService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Brain {
    List<PatternAnswer> patterns = new LinkedList<PatternAnswer>();
    List<ChatbotAnswer> chatbotAnswers = new LinkedList<ChatbotAnswer>();
    List<ChatbotAnswer> exceptionsChatbotAnswers = new LinkedList<ChatbotAnswer>();
    PersonalityRecognizer personalityRecognizer = new PersonalityRecognizer();
    PolishDictionary dictionary = new PolishDictionary();

    private DbService db = null;
    public static List<PersonalityType> personalityTypes = new LinkedList<PersonalityType>();

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
        while (rs.next()) chatbotAnswers.add(new ChatbotAnswer(rs.getNString(2).replace('\\', ' '), rs.getInt(3)));
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