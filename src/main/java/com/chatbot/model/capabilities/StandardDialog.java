package com.chatbot.model.capabilities;


import com.chatbot.model.util.RandomSearching;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.util.List;

@Data
@JsonDeserialize
public class StandardDialog {
    private String userAnswer;
    private List<String> chatbotResponses;


    public List<String> getChatbotResponses() {
        return ImmutableList.copyOf(chatbotResponses);
    }

    public String getRandomChatbotResponse() {
        int size = chatbotResponses.size();
        int i = RandomSearching.generateRandomIndex(size);
        return chatbotResponses.get(i);
    }

}
