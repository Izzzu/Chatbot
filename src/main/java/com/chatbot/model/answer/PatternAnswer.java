package com.chatbot.model.answer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PatternAnswer extends Answer {
    private int note;
    private int importance;
    public PatternAnswer(int note, String answer, int importance) {
        super(answer);
        this.note = note;
        this.importance = importance;
    }

}
