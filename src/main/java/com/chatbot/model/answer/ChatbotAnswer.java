package com.chatbot.model.answer;

import lombok.Data;

@Data
public class ChatbotAnswer extends Answer {

    private int userAnswerNote;
    public ChatbotAnswer(String sentence, int userAnswerNote) {
        super(sentence);
        this.userAnswerNote = userAnswerNote;

    }
}
