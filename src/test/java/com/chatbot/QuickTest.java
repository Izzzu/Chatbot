package com.chatbot;

import com.chatbot.model.answer.PatternAnswer;
import com.chatbot.model.user.Gender;
import com.chatbot.model.user.User;
import com.chatbot.model.util.PreprocessString;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.fest.assertions.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
    @Ignore
    public void test() throws Exception {

        File file = new File("src/main/resources/answersTemp.json");
        File fileIn = new File("src/main/resources/userAnswersAll.csv");

        file.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(fileIn));
        String s = null;
        List<PatternAnswer> answers = new ArrayList<>();
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            //answers.add(new PatternAnswer(Integer.valueOf(tab[1]),tab[0].replace("_", " "),Integer.valueOf(tab[3]), TopicId.getTopicById(Integer.valueOf(tab[2]))));
        }
        br.close();

        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new Gson();
        Type type = new TypeToken<List<PatternAnswer>>(){}.getType();
        String jsonAnswers = gson.toJson(answers, type);
        fileWriter.append(jsonAnswers);
        fileWriter.close();




    }


}
