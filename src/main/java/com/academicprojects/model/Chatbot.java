package com.academicprojects.model;

import com.academicprojects.db.DbService;
import com.academicprojects.util.PreprocessString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Chatbot {

    private static final int IS_GOOD = 1;
    private static final int IS_BAD = 0;

	private PreprocessString pStr = new PreprocessString();
	Logger log = LoggerFactory.getLogger(Chatbot.class);

	public Map taskStates = new HashMap<String, State>();
    public DbService db = null;

	private User user = new User();
	
	private String chatbotName = "Zbyszek";
	private Conversation conversation = new Conversation(chatbotName);
	public Brain brain = new Brain(db);

	public Chatbot() {
		taskStates.put("gettingTopic", State.START);
		taskStates.put("gettingUserAge", State.START);
		taskStates.put("gettingUserName", State.START);
		taskStates.put("gettingUserMood", State.START);

	}

	
	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String commentMood()
	{
		String result="";
		if (user.getMood()==0)
		{
			result = "W takim razie może powiesz mi o czym chcesz ze mną porozmawiać?";
		}
		else if (user.getMood()>0 && user.getMood()<5)
		{
			result = "Widzę, że humor Ci nie dopisuje. O czym chcesz ze mną porozmawiać?";
		}
		else {
			result = "Cieszę się, że masz dobry humor. W takim razie powiedz mi o czym chcesz teraz porozmawiać?";
		}
		return result;

	}


	public String catchTopic() {
		// TODO Auto-generated method stub
		String answer = getLastAnswer();

		Connection conn = db.getConn();
		String result = "";
		int resId;
		int lcu;

	/*	*//*String sql = "SELECT id_sit, term, lcu from PATTERNS1 as p left join SITUATIONS as s" +
		" on p.id_sit=s.id_sit where pattern LIKE ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pStr.replacePolishCharsAndLowerCase(pStr.removeWhite(answer)));
			System.out.println("string ktory idzie do bazy: "+ (pStr.replacePolishCharsAndLowerCase(pStr.removeWhite(answer))));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				result = rs.getString(2);
				resId = rs.getInt(1);
				lcu = rs.getInt(3);
				setLevel("gettingTopic", State.COMPLETED);
				conversation.setTopic(result);
				conversation.setTopicID(resId);
				user.setLcu(lcu);
			}
			else
			{
				setLevel("gettingTopic", State.IN_PROGRESS);
				result = "Nie rozumiem o czym m�wisz:)";
			}*//*

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    result = "niewazne";
		return result;
	}

	public String catchUserAge() {
		String answer = getLastAnswer();
		Scanner sc = new Scanner(answer);
		String token = "";
		token = sc.findInLine("\\d+");
		if (token==null) 
		{
			setLevel("gettingUserAge", State.IN_PROGRESS);
		}
		else 
		{
			user.setAge(Integer.parseInt(token));
			setLevel("gettingUserAge", State.COMPLETED);
		}
		return token;
	}

	/*
	 * 
	 */
	public void catchUserMood() {

        Connection conn = db.getConn();
        int note = 0;
        String answer = pStr.replacePolishCharsAndLowerCase(getLastAnswer());
        String[] words = answer.split(" ");
        String result = "";

        for (int i = 0; i < words.length; i++) {
            note = catchUserAnswer(answer);
            if (note >= 0) {
                setLevel("gettingUserMood", State.COMPLETED);
                user.setMood(note);
            } else {
                setLevel("gettingUserMood", State.IN_PROGRESS);
            }
        }
    }

	/*
	 * 
	 */
	public String catchUserName() {
		
		String answer = getLastAnswer();
		String[] introduce = {"mam na imi� ", "jestem ", "nazywam si� ","mam na imie ",
				"nazywam sie ", "zw� si� ", "zwe sie "};
		for (int i=0; i<introduce.length; i++)
		{
			if (answer.toLowerCase().contains(introduce[i])) {
				String [] s = answer.toLowerCase().split(introduce[i]);
				if(s.length>=2) {
					String[] s2 = s[1].split(" ");
					if (s2.length>0) 
					{
						String name = s2[0].substring(0,1).toUpperCase().concat(s2[0].substring(1));

						setLevel("getingUserName", State.COMPLETED);
						user.setGender(getGender(name));
						user.setName(name);
						return name;
					}
				}
				else {
					setLevel("getingUserName", State.IN_PROGRESS);
					user.setGender(Gender.NOTKNOWN);
					user.setName("Nieznajomy");
					return "Nieznajomy";
				}
			}
		}
		if (!answer.toLowerCase().contains(" "))
		{
			if (answer.length()>=2) {
				setLevel("getingUserName", State.COMPLETED);
				String name = answer.substring(0,1).toUpperCase().concat(answer.substring(1));
				user.setGender(getGender(name));
				user.setName(name);
				return name;
			}
			else 
			{
				setLevel("getingUserName", State.IN_PROGRESS);
				user.setGender(Gender.NOTKNOWN);
				user.setName("Nieznajomy");
				return "Nieznajomy";
			}

		}
		else  {
			String [] s = answer.toLowerCase().split(" ");

			if(s.length>0 && s[0].length()>=2) {
				String name = s[0].substring(0,1).toUpperCase().concat(s[0].substring(1));
				user.setGender(getGender(name));
				user.setName(name);
				return name;
			}
			else {
				setLevel("getingUserName", State.IN_PROGRESS);
				user.setGender(Gender.NOTKNOWN);
				user.setName("Nieznajomy");
				return "Nieznajomy";
			}
		}
	}

	public void updatePersonality() {
		String [] phrases = conversation.getLastAnswer().split("[\',;/.\\s]+");
		for(int i = 0; i<phrases.length; i++) {

			for(PersonalityRecognizer.Phrase phrase: brain.personalityRecognizer.getPersonalityPhrases())
            {
                if (phrases[i].equals(phrase))
                    user.updatePersonality(phrase.getWord(), phrase.getLevel());
            }
		}
	}
	private Gender getGender(String name) {
		if (name.charAt(name.length()-1)=='a')
		{
			return Gender.FEMALE;
		}
		else return Gender.MALE;

	}
	/*
	 * 
	 */
	public void setLevel(String task, State level)
	{
		taskStates.put(task, level);
	}

	public State getLevel(String task) {
		// TODO Auto-generated method stub
		return (State) taskStates.get(task);
	}

    public String prepareAnswer() throws Exception {
        String userAnswer = preprocessUserAnswer();
        int userAnswerNote = catchUserAnswer(userAnswer);
        System.out.println("user answer note: "+userAnswerNote);

        List<String> suitedAnswers = new LinkedList<String>();
        for(ChatbotAnswer chatbotAnswer : brain.chatbotAnswers ) {
            if (chatbotAnswer.userAnswerNote == userAnswerNote) {
                    suitedAnswers.add(chatbotAnswer.getSentence());
            }
        }
        if (suitedAnswers.size()!=0) {
            int randomIndex = (int)(Math.random()*suitedAnswers.size());
            return suitedAnswers.get(randomIndex);
        }
        else {

            return "Heheszki";
        }

    }

	public void catchAnswer(int id_answertype) {
		String answer = preprocessUserAnswer();
		String sql = "SELECT * FROM USERANSWERS WHERE ID= ?";
		Connection conn = db.getConn();
		try {
			PreparedStatement ps;// = conn.prepareStatement(sql);

				ps = conn.prepareStatement(sql);
				ps.setInt(1, id_answertype);
				ResultSet rs = ps.executeQuery();
				int minleven = 1000;
				int currStatNote = -1;
				while (rs.next()) {
					int levenDist = pStr.computeLevenshteinDistance(answer, rs.getString(2));
					
					if( levenDist<minleven) {
						minleven = levenDist;
						currStatNote = rs.getInt(3);
						if(levenDist==0) break;
					}
				}
				conversation.setCurrentStatementNote(currStatNote);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private String preprocessUserAnswer() {
        return pStr.replacePolishCharsAndLowerCase(conversation.getLastAnswer().replace("  "," ").replace("_"," "));
    }

    public int catchUserAnswer(String userAnswer) {
        int noteSum = 0;
        int weights = 0;
        int average = 0;

        System.out.println("-");
        try {
            for (PatternAnswer pattern : brain.patterns) {

                if (userAnswer.contains(pattern.getSentence())) {
                    noteSum += (pattern.getImportance() * pattern.getNote());
                    weights += pattern.getImportance();
                }

            }
            if (weights != 0) average = (noteSum / weights);
            else {
                return -1;
            }
            System.out.println("weights=" + weights);
            System.out.println("noteSum=" + noteSum);
            System.out.println("average=" + average);
            return average;
        }
        catch (Exception e)
        {

        }
        return average;

    }


	
	public boolean isUserTurn()
	{
		return conversation.isUserTurn();
	}
	
	public String getLastAnswer()
	{
		return conversation.getLastAnswer();
	}
	
	public void addUserAnswer(String s)
	{
		conversation.addChatbotAnswerToCourse(s);
	}
	
	public void answer() throws Exception {
		
		int chatlevel = conversation.getChatLevel();
		switch(chatlevel) {
		case 1:
			catchUserName();
			conversation.addChatbotAnswerToCourse("Witaj " + getUserName() + ". Na początku naszej rozmowy chciałbym zadać Ci kilka pytań. Ile masz lat?");
			break;
		case 2:
			catchUserAge();			
			if (user.getAge()==0)
			conversation.addChatbotAnswerToCourse("Nie odpowiadaj jeśli nie chcesz. Jak Twoje dzisiejsze samopoczucie?");
			else conversation.addChatbotAnswerToCourse("A jak Twoje dzisiejsze samopoczucie?");
			break;
		case 3:
			catchUserMood();
			conversation.addChatbotAnswerToCourse(commentMood());
			break;
		/*case 4:
			catchTopic();
			conversation.addChatbotAnswerToCourse(prepareAnswer(chatlevel, 0, conversation.getTopicID()));
			break;*/
		default:
			catchAnswer(conversation.getExpectedAnswerTypeId());
			conversation.addChatbotAnswerToCourse(prepareAnswer());
			break;
		}
		conversation.chatLevelUp();
	}
	
	public List<String> getConversationCourse()
	{
		return conversation.getCourse();
	}
	
	public String getUserName()
	{
		return user.getName();
	}
	
	public String askForMood(int suspectedMood) {
		//przypuszczenie dobrego nastroju
		if (suspectedMood==IS_GOOD) {
			if (moodIsGood()) {
				if (userIsAFemale()) return " Jednak wydajesz si� smutna. Czym si� martwisz?";
				else return " Jednak wydajesz si� smutny. Czym si� martwisz?";
			}
			else if (moodIsBad()) return "Widać że tryskasz radości�. Mimo wszystko, jakie masz w zwi�zku z tym obawy? ";
		}
		else if (suspectedMood ==IS_BAD) {
			if (moodIsGood()) {
				if (userIsAFemale()) return " Wygl�dasz na smutn�. Czym si� martwisz?";
				else return " Wygl�dasz na smutnego. Czym si� martwisz?";
			}
				else if (moodIsBad()) {
					if (userIsAFemale()) return " Mimo to wydajesz si� weso�a. Co jest powodem Twojej rado�ci? ";
					else return " Mimo to wydajesz si� weso�y. Co jest powodem Twojej rado�ci? ";
				}
		}
		return "";
	}

    private boolean userIsAFemale() {
        return user.getGender()== Gender.FEMALE;
    }

    private boolean moodIsBad() {
        return user.getMood()>5;
    }

    private boolean moodIsGood() {
        return (user.getMood()>0 && user.getMood()<5);
    }


}
