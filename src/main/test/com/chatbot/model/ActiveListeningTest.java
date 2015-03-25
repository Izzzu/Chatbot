package com.chatbot.model;

import com.chatbot.model.capabilities.ActiveListening;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

public class ActiveListeningTest {

    @Test
    public void shouldFillMapWithPronouns() throws IOException {
        ActiveListening listening = new ActiveListening();
        String ja = listening.getOppositePronouns().get("ja");
        assertThat(ja).isEqualTo("ty");
        assertThat(listening.getOppositePronouns()).hasSize(13);
    }

}