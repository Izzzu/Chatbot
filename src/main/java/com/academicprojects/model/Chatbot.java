package com.academicprojects.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.academicprojects.controller.ChatController;
import com.academicprojects.db.DbService;
import com.academicprojects.util.PreprocessString;

public class Chatbot {

	public DbService db = null;
	private PreprocessString pStr = new PreprocessString();
	Logger log = LoggerFactory.getLogger(Chatbot.class);

	public Map taskStates = new HashMap<String, State>();
	
	

	private User user = new User();
	
	private String name = "Zbyszek";
	private Conversation conversation = new Conversation(name);
	
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
			result = "W takim razie mo¿e powiesz mi o czym chcesz ze mn¹ porozmawiaæ?";
		}
		else if (user.getMood()>0 && user.getMood()<5)
		{
			result = "Widzê, ¿e humor Ci nie dopisuje. O czym chcesz ze mn¹ porozmawiaæ?";
		}
		else {
			result = "Cieszê siê, ¿e masz dobry humor. W takim razie powiedz mi o czym chcesz teraz porozmawiaæ?";
		}
		return result;

	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public String catchTopic() {
		// TODO Auto-generated method stub
		String answer = getLastAnswer();
		Connection conn = db.getConn();
		String result = "";
		int resId;
		int lcu;

		String sql = "SELECT id_sit, term, lcu from PATTERNS1 as p left join SITUATIONS as s" +
		" on p.id_sit=s.id_sit where pattern LIKE ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pStr.replacePolChars(pStr.removeWhite(answer)));
			System.out.println("string ktory idzie do bazy: "+ (pStr.replacePolChars(pStr.removeWhite(answer))));
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
				result = "Nie rozumiem o czym mówisz:)";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}


	/*
	 * 
	 */
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
				ps.setString(1, pStr.replacePolChars(pStr.removeWhite(words[i])));
				System.out.println("adverb ktory idzie do bazy: "+ (pStr.replacePolChars(pStr.removeWhite(answer))));
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
		String[] introduce = {"mam na imiê ", "jestem ", "nazywam siê ","mam na imie ",
				"nazywam sie ", "zwê siê ", "zwe sie "};
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
		String [] phrases = conversation.getLast().split("[\',;/.\\s]+");
		for(int i = 0; i<phrases.length; i++) {
			System.out.println(i);
			int level = 0;
			String type = null;
			System.out.println(phrases[i]);
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
		if (conversation.getChatLevel()==1 && taskStates.get("gettingTopic")==State.COMPLETED) {
			if (user.getLcu()>0 && user.getLcu()<30) result = "To doœæ stresuj¹ce wydarzenie. ";
			else if (user.getLcu()<60) result = "To bardzo stresuj¹ce wydarzenie. ";
			else result = "To ekstremalnie stresuj¹ce wydarzenie. ";
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
	
	public String prepareAnswer(int chatlevel, int prevstatnote, int id_sit) 
	{
		String result = "";
		int expatyp = -1;
		Connection conn = db.getConn();
		System.out.println("chatlevel "+chatlevel);
		System.out.println("prevstatnote "+prevstatnote);
		System.out.println("id_sit "+id_sit);
		//String sql = "SELECT * FROM CHATBOTANSWERS WHERE ID_SIT = ? AND CHATLEVEL = ? AND PREVSTATNOTE = ?";
		String sql = "SELECT * FROM CHATBOTANSWERS WHERE CHATLEVEL = ? AND PREVSTATNOTE = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setInt(1, id_sit);
			//ps.setInt(2, chatlevel);
			//ps.setInt(3, prevstatnote);
			//ps.setInt(1, id_sit);
			ps.setInt(1, chatlevel);
			ps.setInt(2, prevstatnote);
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
		conversation.setCurrentExpAnsTypeId(expatyp);
		result.replace("<uname>", user.getName());
		result.replace("<uage>", String.valueOf(user.getAge()));
		result.replace("<name>", name);
		result.replace("<askmoodok>", askMood(1));
		result.replace("<askmoodbad>", askMood(0));
		return result;
	}
	
	public void catchAnswer(int id_answertype) {
		String answer = pStr.replacePolChars(pStr.removeWhite(conversation.getLast()));
		String sql = "SELECT * FROM USERANSWERS WHERE ID_ATYP= ? AND UANSWER LIKE ?";
		Connection conn = db.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id_answertype);
			ps.setString(2, answer);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				//jezeli nie znalazlo to levensteinem
				sql = "SELECT * FROM USERANSWERS WHERE ID_ATYP= ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, id_answertype);
				ResultSet rs2 = ps.executeQuery();
				int minleven = 100;
				int currStatNote = -1;
				//String ans = "Nie mam kompetencji ¿eby o tym mówiæ";
				//dodac co jesli rs2==null
				while (rs2.next()) {
					int levenDist = pStr.computeLevenshteinDistance(answer, rs.getString(2));
					
					if( levenDist<minleven) {
						minleven = levenDist;
						currStatNote = rs.getInt(3);
						if(levenDist==0) break;
						
					}
				}
				conversation.setCurrentStatementNote(currStatNote);
			}else {
				rs.relative((int) (Math.random()*rs.getFetchSize()));
				conversation.setCurrentStatementNote(rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isUserTurn()
	{
		return conversation.isUserTurn();
	}
	
	public String getLastAnswer()
	{
		return conversation.getLast();
	}
	
	public void addUserAnswer(String s)
	{
		conversation.add(s);
	}
	
	public void answer() {
		
		int chatlevel = conversation.getChatLevel();
		switch(chatlevel) {
		case 1:
			catchUserName();
			conversation.add("Witaj "+getUserName()+". Na pocz¹tku naszej rozmowy chcia³bym zadaæ Ci kilka pytañ. Ile masz lat?");
			break;
		case 2:
			catchUserAge();			
			if (user.getAge()==0)
			conversation.add("Nie odpowiadaj jeœli nie chcesz. Jak Twoje dzisiejsze samopoczucie?");
			else conversation.add("A jak Twoje dzisiejsze samopoczucie?");
			break;
		case 3:
			catchUserMood();
			conversation.add(commentMood());
			break;
		case 4:
			catchTopic();
			conversation.add(prepareAnswer(chatlevel, 0, conversation.getTopicID()));
			break;
		default:
			catchAnswer(conversation.getCurrentExpAnsTypeId());
			conversation.add(prepareAnswer(chatlevel, conversation.getCurrentStatementNote(), conversation.getTopicID()));
			
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
	
	public String askMood(int m) {
		//przypuszczenie dobrego nastroju
		if (m==1 ) {
			if ((user.getMood()>0 && user.getMood()<5) ) {
				if (user.getGender()==Gender.FEMALE) return " Jednak wydajesz siê smutna. Czym siê martwisz?";
				else return " Jednak wydajesz siê smutny. Czym siê martwisz?";
			}
			else if (user.getMood()>5 ) return "Widaæ ¿e tryskasz radoœci¹. Mimo wszystko, jakie masz w zwi¹zku z tym obawy? ";
			
			
		}
		else if (m==0) {
			if ((user.getMood()>0 && user.getMood()<5) ) {
				if (user.getGender()==Gender.FEMALE) return " Wygl¹dasz na smutn¹. Czym siê martwisz?";
				else return " Wygl¹dasz na smutnego. Czym siê martwisz?";
			}
				else if (user.getMood()>5 ) {
					if (user.getGender()==Gender.FEMALE) return " Mimo to wydajesz siê weso³a. Co jest powodem Twojej radoœci? ";
					else return " Mimo to wydajesz siê weso³y. Co jest powodem Twojej radoœci? ";
				}
				
		}
		return "";
	}
	




}
