package com.chatbot.model.user;

import com.chatbot.model.capabilities.PersonalityId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PersonalityType implements Comparable<PersonalityType> {
    public static double wholePoints;
    private int id;
    private double level;
    private String shortDescription;
    private PersonalityId personalityId;
    public PersonalityType(int id, String shortDescription, PersonalityId personalityId, double level) {
        this.id = id;
        this.level = level;
        this.shortDescription = shortDescription;
        this.personalityId = personalityId;
    }

    @Override
    public String toString() {
        return String.format("%s : %.2f%%", personalityId, level/wholePoints*100);
    }

    public PersonalityType copyWithUpdatedType(double level) {
        return new PersonalityType(id, shortDescription, personalityId, level);
    }

    @Override
    public int compareTo(PersonalityType t1) {

        double percentage1 = this.level;
        double percentage2 = t1.getLevel();
        return  percentage1 > percentage2 ? -1 : (percentage1 < percentage2 ? 1 :0);
    }
}
