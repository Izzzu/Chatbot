package com.chatbot;

import com.chatbot.model.dictionary.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PolishDictionaryTest{

    @Test
    public void shouldReturnRightDictionarySize()
    {
        PolishDictionary dictionary = new PolishDictionary();
        assertEquals(78263, dictionary.getRecordsWithoutVerbs().size() );
    }

    @Test
    public void shouldCreateWordForm()
    {
        PolishDictionary dictionary = new PolishDictionary();
        assertFalse(dictionary.getRecordsWithoutVerbs().isEmpty());
        PolishDictionary.Record record = dictionary.getRecordsWithoutVerbs().get(1);
        assertEquals(record.getForm().getLanguagePart(), LanguagePart.SUBSTANTIV);
        assertEquals(record.getForm().getGenre(), Genre.MALE);
        assertEquals(record.getForm().getGrade(), Grade.DEFAULT);
        assertEquals(record.getForm().getGrammaCase(), GrammaCase.NOMINATIV);
        assertEquals(record.getForm().getGrammaPerson(), GrammaPerson.DEFAULT);
        assertEquals(record.getForm().getSingularOrPlural(), SingularOrPlural.SINGULAR);
        assertEquals(record.getForm().getVerbForm(), VerbForm.DEFAULT);

    }

    @Test
    public void shouldMapMainWordToOtherWords() {
        PolishDictionary dictionary = new PolishDictionary();
        Map<String, List<PolishDictionary.Record>> map = dictionary.getMainWordToOtherWords();
        List<PolishDictionary.Record> result = map.get("ja");
        assertEquals(result.size(), 4);
    }

}