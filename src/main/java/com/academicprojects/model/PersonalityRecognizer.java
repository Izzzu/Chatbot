package com.academicprojects.model;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PersonalityRecognizer {

    @Getter
    private List<Phrase> personalityPhrases = new LinkedList<Phrase>();

    public PersonalityRecognizer() throws IOException {
        getPersonalityPhrasesFromFile(new File("src/main/resources/personalityphrases.csv"));
    }

    void addPersonalityPhrase(String word, int idPersonality, int level) {
        personalityPhrases.add(new Phrase(idPersonality, word, level));
    }

    private void getPersonalityPhrasesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            addPersonalityPhrase(tab[0].replace("_", " "), Integer.valueOf(tab[1]), Integer.valueOf(tab[2]));
        }
        br.close();
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
