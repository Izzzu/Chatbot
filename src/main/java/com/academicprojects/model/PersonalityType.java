package com.academicprojects.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PersonalityType {
    public PersonalityType(int id, int level, String shortDescription, String longDescription) {
        this.id = id;
        this.level = level;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    private int id;
    private int level;
    private String shortDescription;
    private String longDescription;

    public void updateType(int p) {
        this.level =+ p;
    }
}
