package com.academicprojects.model.capabilities;


import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.List;
@Getter
public class FeelingStatement {

    private String feelingVerb;
    private List<String> responses;

    public FeelingStatement(String feelingVerb, List<String> responses) {
        this.feelingVerb = feelingVerb;
        this.responses = ImmutableList.copyOf(responses);
    }
}
