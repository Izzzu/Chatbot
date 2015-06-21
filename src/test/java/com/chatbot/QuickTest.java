package com.chatbot;

import com.chatbot.model.core.Brain;
import com.chatbot.model.core.Chatbot;
import com.chatbot.model.user.Gender;
import com.chatbot.model.user.User;
import com.chatbot.model.dictionary.PolishDictionary;
import com.chatbot.model.util.PreprocessString;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuickTest {



    public QuickTest() {

    }



    @Test
    public void shouldMAtchGroup() throws Exception {

        User user = new User();
        user.setGender(Gender.FEMALE);

        String s = PreprocessString.replacePolishCharsAndLowerCase("Uważasz".toLowerCase());
        Pattern pattern = Pattern.compile("(.*(uwazasz|myslisz|twierdzisz|sadzisz)+.*)");
        Matcher matcher = pattern.matcher(s);
        boolean b = matcher.matches();
        matcher.group(0);
        Assertions.assertThat(s).matches("(.*(uwazasz|myslisz|twierdzisz|sadzisz)+.*)");
    }

    @Test
    public void test() throws Exception {

        Brain brain;
        User user = new User();
        user.setGender(Gender.FEMALE);
        brain = new Brain();
        Chatbot chatbot = new Chatbot(brain, user);
        PolishDictionary dictionary = new PolishDictionary();
        String userAnswer = "Chyba się zakochałam";


    }


}
