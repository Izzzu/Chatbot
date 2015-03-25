package com.chatbot.model.capabilities;


import com.chatbot.model.answer.PatternAnswer;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UnderstandingCapability {
    List<PatternAnswer> complexPatterns = new ArrayList<PatternAnswer>();
    List<PatternAnswer> oneWordPatterns = new ArrayList<PatternAnswer>();

    public UnderstandingCapability() throws IOException, SQLException {
        getUserAnswersFromFile(new File("src/main/resources/useranswers.csv"));

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
        Collections.sort(complexPatterns, comparator);
        Collections.sort(oneWordPatterns, comparator);
        br.close();
    }

    public PatternAnswer getOneWordPattern(String word) {
        for(PatternAnswer answer: oneWordPatterns) {
            if(answer.getSentence().equals(word)) {
                return answer;
            }
        }
        return null;
    }

    public List<PatternAnswer> getComplexPatterns() {
        return ImmutableList.copyOf(complexPatterns);
    }

    public List<PatternAnswer> getOneWordPatterns() {
        return ImmutableList.copyOf(oneWordPatterns);
    }

}