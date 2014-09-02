package com.academicprojects.model;

import com.academicprojects.db.DbService;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Getter
public class Brain {
    List<PatternAnswer> patterns = new LinkedList<PatternAnswer>();
    List<ChatbotAnswer> chatbotAnswers = new LinkedList<ChatbotAnswer>();
    PersonalityRecognizer personalityRecognizer = new PersonalityRecognizer();
    public DbService db = null;

    public Brain(DbService database) {
        db = database;
    }
    public void setUpBrain() {
            try {
            Connection conn = db.getConn();
            getPatternsFromDatabase(conn);
            getChatbotAnswersFromDatabase(conn);
            getPersonalityPhrasesFromDatabase(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {

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
        while (rs.next()) chatbotAnswers.add(new ChatbotAnswer(rs.getNString(2).replace('\\',' '), rs.getInt(3)));
    }

    public void getPatternsFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM USERANSWERS";
        PreparedStatement ps;// = conn.prepareStatement(sql);
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next())
        {
            patterns.add(new PatternAnswer(rs.getInt(4), rs.getNString(2), rs.getInt(5)));
        }
    }
}