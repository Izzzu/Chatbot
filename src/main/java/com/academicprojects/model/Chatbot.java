package com.academicprojects.model;

import com.academicprojects.db.DbService;
import com.academicprojects.util.PreprocessString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
			result = "W takim razie mo�e powiesz mi o czym chcesz ze mn� porozmawia�?";
		}
		else if (user.getMood()>0 && user.getMood()<5)
		{
			result = "Widz�, �e humor Ci nie dopisuje. O czym chcesz ze mn� porozmawia�?";
		}
		else {
			result = "Ciesz� si�, �e masz dobry humor. W takim razie powiedz mi o czym chcesz teraz porozmawia�?";
		}
		return result;

	}

	/**
	 * 
	 * @param
	 * @return
	 */
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
	public String catchUserMood() {

		String answer = getLastAnswer();
		Connection conn = db.getConn();
		String result = "";
		String [] words;
		int note = 0;

		words = answer.split("[\',;/.\\s]+");
		String sql = "SELECT answernote from useranswers as a " +
		"where uanswer LIKE ?";
		for (int i = 0; i<words.length; i++)
		{

			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, pStr.replacePolishCharsAndLowerCase(pStr.removeWhite(words[i])));
				//System.out.println("adverb ktory idzie do bazy: "+ (pStr.replacePolishCharsAndLowerCase(pStr.removeWhite(answer))));
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					note = rs.getInt(1);

					setLevel("gettingUserMood", State.COMPLETED);
					user.setMood(note);
					result = "";
				}
				else 
				{
					setLevel("gettingUserMood", State.IN_PROGRESS);
					result = "";
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
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
			//System.out.println(i);
			int level = 0;
			String type = null;
			//System.out.println(phrases[i]);
			try {
				ResultSet rs = db.query("SELECT level, type FROM phrase as ph join personality as pe on ph.id_per=pe.id_per WHERE ph.word LIKE '"+phrases[i]+"'");

				ResultSetMetaData meta   = rs.getMetaData();

				while(rs.next()) {
					level = rs.getInt(1);
					type = rs.getString(2);
					log.info("Level: "+level+ " type: "+type);
					user.updatePersonality(type, level);
					log.info(user.getPerType().toString());
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	public String prepareAnswer(Conversation conversation, int prevNote)
	{
		String result = null;
		if (conversation.getChatLevel()==4 && taskStates.get("gettingTopic")==State.COMPLETED) {
			if (user.getLcu()>0 && user.getLcu()<30) result = "To do�� stresuj�ce wydarzenie. ";
			else if (user.getLcu()<60) result = "To bardzo stresuj�ce wydarzenie. ";
			else result = "To ekstremalnie stresuj�ce wydarzenie. ";
		}
		//if (getLevel("gettingTopic")==State.COMPLETED) {
		Connection conn = db.getConn();
			String sql = "SELECT * FROM CHATBOTANSWERS WHERE ID_SIT = ? AND CHATLEVEL = ? AND PREVSTATNOTE = ?";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, conversation.getTopicID());
				ps.setInt(2, conversation.getChatLevel());
				ps.setInt(3, conversation.getCurrentStatementNote());
				ResultSet rs = ps.executeQuery();
				result += rs.getString(2);			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

    public String prepareAnswer() throws Exception {
        String userAnswer = preprocessUserAnswer();
        int userAnswerNote = catchUserAnswer(userAnswer);
        System.out.println("user answer: "+userAnswer);
        List<String> suitedAnswers = new LinkedList<String>();
        for(ChatbotAnswer chatbotAnswer : brain.chatbotAnswers ) {
            System.out.println(chatbotAnswer);
            if (chatbotAnswer.userAnswerNote == userAnswerNote) {
                    suitedAnswers.add(chatbotAnswer.getSentence());
            }
        }
        if (suitedAnswers.size()!=0) {
            int randomIndex = (int)(Math.random()*suitedAnswers.size());
            return suitedAnswers.get(randomIndex);
        }
        else {

            throw new Exception("nie znalazlo odpowiedzi"); //
        }

    }
	
	public String prepareAnswer(int chatLevel, int userAnswerNote, int situationId)
	{
		String result = "";
		int expatyp = -1;
		Connection conn = db.getConn();
		//System.out.println("chatLevel "+ chatLevel);
		//System.out.println("userAnswerNote "+ userAnswerNote);
		//System.out.println("situationId "+ situationId);
		//String sql = "SELECT * FROM CHATBOTANSWERS WHERE ID_SIT = ? AND CHATLEVEL = ? AND PREVSTATNOTE = ?";
		String sql = "SELECT * FROM CHATBOTANSWERS WHERE CHATLEVEL = ? AND PREVSTATNOTE = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setInt(1, situationId);
			//ps.setInt(2, chatLevel);
			//ps.setInt(3, userAnswerNote);
			//ps.setInt(1, situationId);
			ps.setInt(1, chatLevel);
			ps.setInt(2, userAnswerNote);
			ResultSet rs = ps.executeQuery();
			//System.out.println(rs.next()==true);
			if (rs.next()) {
				result = rs.getString(2);
				expatyp = rs.getInt(6);
			}
			//conversation.setCurrentStatementNote(rs.getInt()
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conversation.setExpectedAnswerTypeId(expatyp);
		result.replace("<uname>", user.getName());
		result.replace("<uage>", String.valueOf(user.getAge()));
		result.replace("<name>", chatbotName);
		result.replace("<askmoodok>", askForMood(IS_GOOD));
		result.replace("<askmoodbad>", askForMood(IS_BAD));
		return result;
	}
	
	public void catchAnswer(int id_answertype) {
		String answer = preprocessUserAnswer();
		String sql = "SELECT * FROM USERANSWERS WHERE ID_ATYP= ?";
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
        return pStr.replacePolishCharsAndLowerCase(conversation.getLastAnswer().replace("  "," "));
    }

    public int catchUserAnswer(String userAnswer) {
        int noteSum = 0;
        int weights = 0;
        int average = 0;


        try {
            for (PatternAnswer pattern : brain.patterns) {
                System.out.println(pattern.getSentence());

                if (userAnswer.contains(pattern.getSentence())) {
                    noteSum = +pattern.getImportance() * pattern.getNote();
                    weights = +pattern.getImportance();
                }
                System.out.println(weights);
            }
            if (weights != 0) average = noteSum / weights;
            else {
                //nie znaleziono pasujacego patternu
            }

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
		conversation.addUserAnswerToCourse(s);
	}
	
	public void answer() throws Exception {
		
		int chatlevel = conversation.getChatLevel();
		switch(chatlevel) {
		case 1:
			catchUserName();
			conversation.addUserAnswerToCourse("Witaj " + getUserName() + ". Na pocz�tku naszej rozmowy chcia�bym zada� Ci kilka pyta�. Ile masz lat?");
			break;
		case 2:
			catchUserAge();			
			if (user.getAge()==0)
			conversation.addUserAnswerToCourse("Nie odpowiadaj je�li nie chcesz. Jak Twoje dzisiejsze samopoczucie?");
			else conversation.addUserAnswerToCourse("A jak Twoje dzisiejsze samopoczucie?");
			break;
		case 3:
			catchUserMood();
			conversation.addUserAnswerToCourse(commentMood());
			break;
		case 4:
			catchTopic();
			conversation.addUserAnswerToCourse(prepareAnswer(chatlevel, 0, conversation.getTopicID()));
			break;
		default:
			catchAnswer(conversation.getExpectedAnswerTypeId());
			conversation.addUserAnswerToCourse(prepareAnswer());
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
