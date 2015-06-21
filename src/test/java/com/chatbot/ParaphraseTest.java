package com.chatbot;

import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.user.Gender;
import com.chatbot.model.user.User;
import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;


@RunWith(Parameterized.class)
public class ParaphraseTest {

    String chatbotAnswer;
    String userAnswer;

    public ParaphraseTest(String chatbotAnswer, String userAnswer) {
        this.chatbotAnswer = chatbotAnswer;
        this.userAnswer = userAnswer;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {" miałeś problemy finansowe.", "Miałem problemy finansowe."},
                {" zauważasz poprawę.", "Zauważam poprawę"},
                {" nie zauważasz poprawy.", "Nie zauważam poprawy"},
                {" chciałaś to naprawić.", "Chciałam to naprawić."},
                {" chciałaś zrobić co miałaś.", "Chciałam zrobić co miałam."},
                {" zauważasz że miałaś rację.", "Zauważam, że miałam rację."},
                {"nie wiesz co masz robić.","Nie wiem, co mam robić"},
                {"nie rozumiesz dlaczego działam.","Nie rozumiem, dlaczego działasz"},
                {" mam dobry gust.", "Masz dobry gust."},
                {" nie wiem co mówisz.", "Chyba nie wiesz co mówisz."},
                {" nie rozumiesz mnie.", "Nie rozumiem cię."},
                {" nie rozumiesz mnie.", "Nie rozumiem cie."}
        });
    }

    @Test
    public void pharaprasizeTest() throws Exception {

        Brain brain = new Brain();
        User user = new User();
        user.setGender(Gender.MALE);
        Chatbot chatbot = new Chatbot(brain, user);

        Assertions.assertThat(chatbot.paraphrase(userAnswer, false)).contains(chatbotAnswer);
    }

    @Test
    public void test() throws Exception {

        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(new Brain() ,user);

        assertEquals("", chatbot.paraphrase("Chyba nie", false));
    }


}
