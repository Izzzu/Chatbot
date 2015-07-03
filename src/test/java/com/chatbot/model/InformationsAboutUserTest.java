package com.chatbot.model;

import com.chatbot.model.capabilities.PersonalityId;
import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.core.Topic;
import com.chatbot.model.user.Gender;
import com.chatbot.model.user.Personality;
import com.chatbot.model.user.User;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.chatbot.model.capabilities.PersonalityId.FAWORYZUJACY;
import static com.chatbot.model.capabilities.PersonalityId.WERYFIKUJACY;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class InformationsAboutUserTest {

    Brain brain;
    Chatbot chatbot;


    @Before
    public void setUp() throws Exception {
        brain = new Brain();
        chatbot = new Chatbot(brain);

    }

    @Test
    public void shouldUpdateTopicAndLcu() {
        User user = new User("R", "sdfsd", 23, Gender.FEMALE, 23, new HashSet<Topic>(), null, 0);
        Topic t = new Topic(("BRAK_PRACY"), 12, "cos tam");
        user.updateTopicAndLcu(t);
        assertTrue(user.getTopics().contains(t));
        assertTrue(user.getLcu() == 12);
    }

    @Test
    public void shouldNotUpdateTopicAndLcu() {
        Topic t = new Topic(("BRAK_PRACY"), 12, "cos tam");
        User user = new User("R", "sdfsd", 23, Gender.FEMALE, 23, Sets.newHashSet(t), null, 0);
        user.updateTopicAndLcu(t);
        assertTrue(user.getLcu() == 0);
        assertTrue(user.getTopics().contains(t));
    }

    @Test
    public void catchUserGender() {
        String userAnswer = "Widziałam dzisiaj diabła";

        chatbot.updateInformationAboutUser(userAnswer);
        assertThat(Gender.FEMALE).isEqualTo(chatbot.getUser().getGender());
    }

    @Test
    public void updateUserPersonality() {
        String userAnswer = "Szczególnie";

        chatbot.updateInformationAboutUser(userAnswer);
        Personality personality = chatbot.getUser().getPersonality();
        System.out.println(chatbot.getUser().getPersonality());
        Set<PersonalityId> personalities = ImmutableSet.of(FAWORYZUJACY, WERYFIKUJACY);
        assertThat(personalities.contains(personality.getMainType()));
    }

    @Test
    public void updateUserPersonalityAfterConvertingToMainVerb() {
        String userAnswer = "promuje";
        chatbot.updateInformationAboutUser(userAnswer);
        Personality personality = chatbot.getUser().getPersonality();
        System.out.println(personality);
        assertThat(personality.getMainType().equals(FAWORYZUJACY));
    }

    @Test
    public void updateUserPersonalityAfterWithoutPolishChars() {
        String userAnswer = "uwzglednia";

        chatbot.updateInformationAboutUser(userAnswer);
        Personality personality = chatbot.getUser().getPersonality();
        System.out.println(chatbot.getUser().getPersonality());
        assertThat(personality.getMainType().equals(FAWORYZUJACY));
        System.out.println(personality.getById(FAWORYZUJACY).getLevel());
        assertThat(personality.getById(FAWORYZUJACY).getLevel()).isGreaterThan(0);
    }

    @Test
    public void updateUserPersonalityLongSentenceManyPersonalities() {
        String userAnswer = "Po pierwsze, to był mój pomysł, a po drugie uważam, że nikt nie wykonałby tego projektu tak efektywnie.";

        chatbot.updateInformationAboutUser(userAnswer);
        Personality personality = chatbot.getUser().getPersonality();
        System.out.println(chatbot.getUser().getPersonality());
        assertThat(personality.getMainType().equals(FAWORYZUJACY));
        System.out.println(personality.getById(FAWORYZUJACY).getLevel());
    }
}
