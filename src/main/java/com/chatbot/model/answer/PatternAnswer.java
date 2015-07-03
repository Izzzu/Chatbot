package com.chatbot.model.answer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter
public class PatternAnswer extends Answer {
    private int note;
    private int importance;
    private String topicId;
    public PatternAnswer(int note, String answer, int importance, String topicId) {
        super(answer);
        this.note = note;
        this.importance = importance;
        this.topicId = topicId;
    }

}
