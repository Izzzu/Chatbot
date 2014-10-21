package com.academicprojects.model.dictionary;

import lombok.Getter;

@Getter
public class WordDetails {
    LanguagePart languagePart;
    Grade grade;
    GrammaCase grammaCase;
    SingularOrPlural singularOrPlural;
    Genre genre;
    VerbForm verbForm;
    GrammaPerson grammaPerson;

    public WordDetails(LanguagePart languagePart, Grade grade, GrammaCase grammaCase, SingularOrPlural singularOrPlural, Genre genre,VerbForm verbForm, GrammaPerson grammaPerson) {
        this.languagePart = languagePart;
        this.grade = grade;
        this.grammaCase = grammaCase;
        this.singularOrPlural = singularOrPlural;
        this.genre = genre;
        this.verbForm = verbForm;
        this.grammaPerson = grammaPerson;
    }


    public WordDetails(LanguagePart languagePart) {

        this.languagePart = languagePart;
        this.grade = Grade.DEFAULT;
        this.grammaCase = GrammaCase.DEFAULT;
        this.singularOrPlural = SingularOrPlural.N_A;
        this.genre = Genre.NEUTER;
        this.verbForm = VerbForm.DEFAULT;
        this.grammaPerson = GrammaPerson.DEFAULT;
    }
}
