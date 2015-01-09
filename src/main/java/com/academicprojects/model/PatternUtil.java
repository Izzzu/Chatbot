package com.academicprojects.model;


public class PatternUtil {
    public static String addPostfixToVerbAccordingGender(String verb, boolean isFemale) {
        if(isFemale) {
            return verb.replace("<gender>", "aś");
        } else {
            return verb.replace("<gender>", "eś");
        }
    }
}
