package com.academicprojects.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class PersonalityRecognizer {

    @Getter
    private List<Phrase> personalityPhrases = new LinkedList<Phrase>();

    void addPersonalityPhrase(String word, int idPersonality, int level) {
        personalityPhrases.add(new Phrase(idPersonality, word, level));
    }

    @Getter
    public class Phrase {
        String word;
        int idPersonality;
        int level;

        private Phrase(int idPersonality, String word, int level) {
            this.idPersonality = idPersonality;
            this.word = word;
            this.level = level;
        }
    }
}
