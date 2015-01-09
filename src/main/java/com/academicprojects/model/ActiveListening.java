package com.academicprojects.model;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;


@Getter
public class ActiveListening {
    private List<String> paraphraseStart = new LinkedList<String>();


    public ActiveListening(List<String> list) {
        this.paraphraseStart = ImmutableList.copyOf(list);
    }
}
