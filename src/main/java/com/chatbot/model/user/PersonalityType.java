package com.chatbot.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PersonalityType implements Comparable<PersonalityType> {
    public static double wholePoints;
    private int id;
    private double level;
    private String shortDescription;
    private String longDescription;
    public PersonalityType(int id, String shortDescription, String longDescription, double level) {
        this.id = id;
        this.level = level;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    @Override
    public String toString() {
        return String.format("%s : %.2f%%", longDescription, level/wholePoints*100);
    }

    public PersonalityType copyWithUpdatedType(double level) {
        return new PersonalityType(id, shortDescription, longDescription, level);
    }

    @Override
    public int compareTo(PersonalityType t1) {

        double percentage1 = this.level;
        double percentage2 = t1.getLevel();
        return  percentage1 > percentage2 ? -1 : (percentage1 < percentage2 ? 1 :0);
    }
}
