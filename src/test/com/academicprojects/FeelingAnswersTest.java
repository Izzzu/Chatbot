import com.academicprojects.db.DbService;
import com.academicprojects.model.Brain;
import com.academicprojects.model.Chatbot;
import com.academicprojects.model.Gender;
import com.academicprojects.model.User;
import com.academicprojects.model.dictionary.PolishDictionary;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

public class FeelingAnswersTest {

    DbService db = null;
    Brain brain = Mockito.spy(new Brain(db));
    PolishDictionary dictionary = new PolishDictionary();


    @Test
    public void shouldResponseForJestem() {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        chatbot.brain = brain;

        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("Co jest powodem tego, że<paraphrase>?"));
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("jestem", patterns);
        map.put("chcę", Arrays.asList("Dlaczego?"));
        doReturn(map).when(brain).getFeelingStatement();


        assertThat(chatbot.getChatbotResponseForFeelingSentence("Jestem piękna")).isEqualTo("Co jest powodem tego, że jesteś piękna?");
    }

    @Test
    public void shouldResponseForChce() {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);

        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("Czy to twoje najważniejsze pragnienie?"));
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        Map<String, List<String>> map = new HashMap<String, List<String>>();
      //  map.put("jestem", patterns);
        map.put("chcę", patterns);
        doReturn(map).when(brain).getFeelingStatement();

        chatbot.brain = brain;
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Chcę zarabiać więcej pieniędzy.")).isEqualTo("Czy to twoje najważniejsze pragnienie?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Chce zarabiać więcej pieniędzy.")).isEqualTo("Czy to twoje najważniejsze pragnienie?");
    }

    @Test
    public void shouldResponseForCzuje() {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);


        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("To bardzo ważne, żeby głośno mówić o swoich uczuciach."));
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        //  map.put("jestem", patterns);
        map.put("czuję", patterns);
        doReturn(map).when(brain).getFeelingStatement();

        chatbot.brain = brain;
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Czuję się fatalnie.")).isEqualTo("To bardzo ważne, żeby głośno mówić o swoich uczuciach.");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Czuje się fatalnie.")).isEqualTo("To bardzo ważne, żeby głośno mówić o swoich uczuciach.");

    }

    @Test
    public void shouldResponseForJestemNegation() {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        chatbot.brain = brain;

        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("Co jest powodem tego, że<paraphrase>?"));
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("jestem", patterns);
        map.put("chcę", patterns);
        map.put("czuję", patterns);
        doReturn(map).when(brain).getFeelingStatement();


        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie jestem piękna")).isEqualTo("Co jest powodem tego, że nie jesteś piękna?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie chcę tego")).isEqualTo("Co jest powodem tego, że nie chcesz tego?");
        assertThat(chatbot.getChatbotResponseForFeelingSentence("Nie czuję tego")).isEqualTo("Co jest powodem tego, że nie czujesz tego?");

    }

    @Test(expected=NullPointerException.class)
    public void shouldNotRecognizeAsFeelingSentence() {
        User user = new User();
        user.setGender(Gender.FEMALE);
        Chatbot chatbot = new Chatbot(user);
        chatbot.brain = brain;

        LinkedList<String> patterns = new LinkedList<String>(Arrays.asList("Co jest powodem tego, że<paraphrase>?"));
        doReturn(dictionary).when(brain).getDictionary();
        doReturn("").when(brain).startParaphrase();

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("jestem", patterns);
        map.put("chcę", Arrays.asList("Dlaczego?"));
        doReturn(map).when(brain).getFeelingStatement();


        assertThat(chatbot.getChatbotResponseForFeelingSentence("Jesteś piękna")).isNotEqualTo("Co jest powodem tego, że jestem piękna?");
    }


}