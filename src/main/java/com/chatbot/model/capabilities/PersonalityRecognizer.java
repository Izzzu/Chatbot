package com.chatbot.model.capabilities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class PersonalityRecognizer {

    @Getter
    private List<Phrase> personalityPhrases = new LinkedList<Phrase>();
    @Getter
    Map<PersonalityId, Double> personalityLevels = new HashMap<>();


    public PersonalityRecognizer() throws IOException {
        getPersonalityPhrasesFromFile(new File("src/main/resources/phrasesForPersonalities.json"));

    }

    private void getPersonalityPhrasesFromFile(File file) throws IOException {

        FileReader fileReader = new FileReader(file);
        Gson gson = new Gson();
        Type type = new TypeToken<List<PersonalityIdentification>>() {
        }.getType();
        List<PersonalityIdentification> personalityIdentifications = gson.fromJson(fileReader, type);

        for (PersonalityIdentification personalityIdentification : personalityIdentifications) {
            for (String phrase : personalityIdentification.getPhrases()) {
                Set<PersonalityId> ids = getIdsForPhrase(phrase);
                ids.add(personalityIdentification.getPersonalityId());
                Phrase newPhrase = new Phrase(ids, phrase, 1);
                personalityPhrases.add(newPhrase);
            }
            personalityLevels.put(personalityIdentification.getPersonalityId(), 1d/personalityIdentification.getPhrases().size());
        }
        Collections.sort(personalityPhrases, wordLengthComparator);
    }

    private Set<PersonalityId> getIdsForPhrase(String phrase) {
        for (Phrase personalityPhrase : personalityPhrases) {
            if(personalityPhrase.getWord().equals(phrase))
                return personalityPhrase.getPersonalityIds();
        }
        return Sets.newHashSet();
    }

    @Data
    @JsonDeserialize
    public class Phrase {
        String word;
        Set<PersonalityId> personalityIds;
        int level;

        private Phrase(Set<PersonalityId> personalityIds, String word, int level) {
            this.personalityIds = personalityIds;
            this.word = word;
            this.level = level;
        }
    }

    public static Comparator<? super Phrase> wordLengthComparator = new Comparator<Phrase>() {
        @Override
        public int compare(Phrase s, Phrase t1) {
            int firstSentenceLength = s.getWord().length();
            int secondSentenceLength = t1.getWord().length();
            if (firstSentenceLength > secondSentenceLength) return -1;
            if (firstSentenceLength < secondSentenceLength) return 1;
            return 0;
        }
    };


}
