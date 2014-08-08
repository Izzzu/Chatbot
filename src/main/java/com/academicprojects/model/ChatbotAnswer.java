package com.academicprojects.model;

/**
 * Created by Cookiemonster on 2014-08-04.
 */
public class ChatbotAnswer extends Answer {

    int userAnswerNote;
    public ChatbotAnswer(String sentence, int userAnswerNote) {
        super(sentence);
        this.userAnswerNote = userAnswerNote;

    }
}
