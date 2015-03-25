package com.chatbot.model.capabilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


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
        return oppositePronouns.containsKey(word) ? oppositePronouns.get(word) : null;
    }

    public boolean wordIsPronounWhichCanBeParaphrased(String word) {
        return oppositePronouns.containsKey(word); } }
