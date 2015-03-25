package com.chatbot.model.exceptions;


public class NotFoundResponsesForFeelingSentence extends Exception {
    public NotFoundResponsesForFeelingSentence(String verb) {
        System.out.println("Not found responses for feeling user answer with verb "+verb);
    }
}
