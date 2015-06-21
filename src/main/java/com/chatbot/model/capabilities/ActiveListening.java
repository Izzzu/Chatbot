package com.chatbot.model.capabilities;

import com.chatbot.model.util.PreprocessString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
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
        getParaphrases(new File("src/main/resources/paraphrases.json"));
        fillMapOppositePronouns();
    }

    private void fillMapOppositePronouns() throws IOException {

        oppositePronouns = new ObjectMapper().readValue(new File("src/main/resources/oppositePronouns.json"), HashMap.class);

    }

    private void getParaphrases(File file) throws IOException {
        FileReader fileReader = new FileReader(file);

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        paraphraseStart = gson.fromJson(fileReader, type);
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
