package com.academicprojects;

import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.Gender;
import com.academicprojects.model.User;
import com.academicprojects.model.dictionary.PolishDictionary;
import com.academicprojects.util.PreprocessString;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuickTest {



    public QuickTest() {

    }



    @Test
    public void shouldMAtchGroup() throws Exception {

        Brain brain;
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        brain = new Brain();
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();
        PolishDictionary dictionary = new PolishDictionary();

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
        Chatbot chatbot = new Chatbot(user);
        brain = new Brain();
        chatbot.brain = brain;
        chatbot.brain.setUpBrain();
        PolishDictionary dictionary = new PolishDictionary();
        String userAnswer = "Chyba się zakochałam";


    }


}
