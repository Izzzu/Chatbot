package com.academicprojects.model;

import lombok.Getter;

/**
 * Created by Cookiemonster on 2014-08-03.
 */

@Getter
public class PatternAnswer extends Answer{
    private int note;
    private int importance;
    public PatternAnswer(int note, String answer, int importance) {

        super(answer);
        this.note = note;
        this.importance = importance;
    }

}
