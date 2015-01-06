package com.academicprojects.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PersonalityType {
    public PersonalityType(int id, String shortDescription, String longDescription, int level) {
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
