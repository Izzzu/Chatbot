package com.chatbot.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PersonalityType {
    public PersonalityType(int id, String shortDescription, String longDescription, double level) {
        this.id = id;
        this.level = level;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    private int id;
    private double level;
    private String shortDescription;
    private String longDescription;

    @Override
    public String toString() {
        return String.format("%s : %.2f%%", longDescription, level);
    }

    public PersonalityType copyWithUpdatedType(double p) {
        double level = this.level + p;
        return new PersonalityType(id, shortDescription, longDescription, level);
    }
}
