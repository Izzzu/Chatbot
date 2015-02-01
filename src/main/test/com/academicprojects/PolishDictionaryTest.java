package com.academicprojects;

import com.academicprojects.model.dictionary.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PolishDictionaryTest{

    @Test
    public void shouldReturnRightDictionarySize()
    {
        PolishDictionary dictionary = new PolishDictionary();
        assertEquals(78262, dictionary.getRecordsWithoutVerbs().size() );
    }

    @Test
    public void shouldCreateWordForm()
    {
        PolishDictionary dictionary = new PolishDictionary();
        assertFalse(dictionary.getRecordsWithoutVerbs().isEmpty());
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getLanguagePart(), LanguagePart.SUBSTANTIV);
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getGenre(), Genre.MALE);
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getGrade(), Grade.DEFAULT);
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getGrammaCase(), GrammaCase.NOMINATIV);
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getGrammaPerson(), GrammaPerson.DEFAULT);
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getSingularOrPlural(), SingularOrPlural.SINGULAR);
        assertEquals(dictionary.getRecordsWithoutVerbs().get(0).getForm().getVerbForm(), VerbForm.DEFAULT);
    }

}