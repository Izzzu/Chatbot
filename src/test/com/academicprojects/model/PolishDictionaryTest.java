package com.academicprojects.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PolishDictionaryTest{

    @Test
    public void shouldReturnRightDictionarySize()
    {
        PolishDictionary dictionary = new PolishDictionary();
        assertEquals(98402, dictionary.getRecords().size() );
    }

    @Test
    public void shouldCreateWordForm()
    {
        PolishDictionary dictionary = new PolishDictionary();
        assertFalse(dictionary.getRecords().isEmpty());
        assertEquals(dictionary.getRecords().get(0).getForm(), LanguagePart.SUBSTANTIV);
    }

}