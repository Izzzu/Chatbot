package com.chatbot.model.capabilities;

import lombok.Data;

import java.util.List;

/**
 * Created by izabela on 15.03.15.
 */
@Data
public class PersonalityIdentification {
    PersonalityId personalityId;
    List<String> phrases;
}
