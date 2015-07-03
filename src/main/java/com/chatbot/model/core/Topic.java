package com.chatbot.model.core;

import lombok.Getter;

@Getter
public class Topic {
    private String topicId;
    private int lcu;
    private String topicDescription;

    public Topic(String topicId, int lcu, String topicDesc) {
        this.topicId = topicId;
        this.lcu = lcu;
        this.topicDescription = topicDesc;

    }

    @Override
    public String toString() {
        return topicDescription;
    }


}
