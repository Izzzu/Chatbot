package com.chatbot.model.capabilities;


import com.chatbot.model.answer.PatternAnswer;
import com.chatbot.model.core.Topic;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;

import static com.chatbot.model.util.PreprocessString.computeLevenshteinDistance;

public class UnderstandingCapability {
    List<PatternAnswer> complexPatterns = new ArrayList<PatternAnswer>();
    List<PatternAnswer> oneWordPatterns = new ArrayList<PatternAnswer>();
    List<Topic> topics = new ArrayList<>();

    public UnderstandingCapability() throws IOException, SQLException {
        getUserAnswersFromFile(new File("src/main/resources/userAnswers.json"));
        getTopicsFromFile(new File("src/main/resources/topics.json"));

    }

    private void getTopicsFromFile(File file) throws FileNotFoundException {
        FileReader fileReader = new FileReader(file);

        Gson gson = new Gson();
        Type type = new TypeToken<List<Topic>>(){}.getType();
        List<Topic> topics = gson.fromJson(fileReader, type);
        this.topics = topics;
    }

    private void getUserAnswersFromFile(File filePattern) throws IOException, SQLException {
        FileReader fileReader = new FileReader(filePattern);

        Gson gson = new Gson();
        Type type = new TypeToken<List<PatternAnswer>>(){}.getType();
        List<PatternAnswer> patterns = gson.fromJson(fileReader, type);
        for (PatternAnswer pattern : patterns) {
            if (pattern.getSentence().contains(" ")) {
                complexPatterns.add(pattern);
            }
            else {
                oneWordPatterns.add(pattern);
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
    }

    public PatternAnswer getOneWordPattern(String word) {
        for(PatternAnswer answer: oneWordPatterns) {
            if (computeLevenshteinDistance(word, answer.getSentence()) <= 2) {

                //(answer.getSentence().equals(word)) {
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

    public Collection getTopics() {
        return ImmutableList.copyOf(topics);
    }

    public Topic getTopic(String topicId) {
        for (Topic topic : topics) {
            if (topic.getTopicId().equals(topicId)) return topic;
        }
        return null;
    }
}