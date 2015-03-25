package com.chatbot;

import com.chatbot.model.dictionary.PolishDictionary;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DictionaryTest {

    @Test
    public void dictionaryContainsImportantVerbs() {
        PolishDictionary dictionary = new PolishDictionary();

        assertThat(dictionary.findMainVerb("jestem")).isEqualTo("być");
        assertThat(dictionary.findMainVerb("jesteś")).isEqualTo("być");
        assertThat(dictionary.findMainVerb("jesteśmy")).isEqualTo("być");
        assertThat(dictionary.findMainVerb("jesteście")).isEqualTo("być");

        assertThat(dictionary.findMainVerb("chcę")).isEqualTo("chcieć");
        assertThat(dictionary.findMainVerb("chcesz")).isEqualTo("chcieć");
        assertThat(dictionary.findMainVerb("chcecie")).isEqualTo("chcieć");
        assertThat(dictionary.findMainVerb("chcemy")).isEqualTo("chcieć");

        assertThat(dictionary.findMainVerb("czuję")).isEqualTo("czuć");
        assertThat(dictionary.findMainVerb("czujesz")).isEqualTo("czuć");
        assertThat(dictionary.findMainVerb("czujecie")).isEqualTo("czuć");
        assertThat(dictionary.findMainVerb("czujemy")).isEqualTo("czuć");

    }
}