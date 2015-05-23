package com.chatbot.model.capabilities;

import com.chatbot.model.util.PreprocessString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@Getter
public class ActiveListening {
    private List<String> paraphraseStart = new LinkedList<String>();
    private HashMap<String, String> oppositePronouns = new HashMap<>();


    public ActiveListening(List<String> list) {
        this.paraphraseStart = ImmutableList.copyOf(list);
    }
    public ActiveListening() throws IOException {
        getParaphrases(new File("src/main/resources/paraphrases.csv"));
        fillMapOppositePronouns();
    }

    private void fillMapOppositePronouns() throws IOException {

        oppositePronouns = new ObjectMapper().readValue(new File("src/main/resources/oppositePronouns.json"), HashMap.class);

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
        paraphraseStart = ImmutableList.copyOf(list);
        br.close();
    }

    public String getOppositePronounOf(String word) {
        return oppositePronouns.containsKey(word) ? oppositePronouns.get(word) : tryToGetOppositePronounMatchingWithoutPolishChars(word);
    }

    private String tryToGetOppositePronounMatchingWithoutPolishChars(final String singleWord) {
        Predicate<String> stringPredicate = filterWithoutPolishWords(singleWord);
        Optional<String> matchingWithoutPolishChars = pronouns().filter(stringPredicate).first();
        if(!matchingWithoutPolishChars.isPresent()) {
            return null;
        }
        return oppositePronouns.get(matchingWithoutPolishChars.get());
    }

    private FluentIterable<String> pronouns() {
        return FluentIterable.from(oppositePronouns.keySet());
    }

    public boolean wordIsPronounWhichCanBeParaphrased(final String singleWord) {
        Predicate<String> stringPredicate = filterWithoutPolishWords(singleWord);
        return oppositePronouns.containsKey(singleWord) || pronouns().anyMatch(stringPredicate); }

    private Predicate<String> filterWithoutPolishWords(final String singleWord) {
        return new Predicate<String>() {
                @Override
                public boolean apply(String s) {
                    return PreprocessString.replacePolishCharsAndLowerCase(s).equals(singleWord);
                }
            };
    }
}
