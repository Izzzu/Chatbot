package com.academicprojects.model;

import com.academicprojects.model.dictionary.GrammaPerson;
import com.academicprojects.model.dictionary.PolishDictionary;
import com.academicprojects.model.dictionary.WordDetails;
import com.academicprojects.util.PreprocessString;
import com.academicprojects.util.RandomSearching;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.academicprojects.model.TypeOfSentence.FEELING_STATEMENT;
import static com.academicprojects.model.dictionary.VerbForm.INFINITIVE;
import static com.academicprojects.util.PreprocessString.replacePolishCharsAndLowerCase;

@Getter
public class Chatbot {

    private static final int IS_GOOD = 1;
    private static final int IS_BAD = 0;

	private PreprocessString pStr = new PreprocessString();
	Logger log = LoggerFactory.getLogger(Chatbot.class);

	public Map taskStates = new HashMap<>();

    public Brain brain;
	private User user;
	
	private String chatbotName = "Zbyszek";
	private Conversation conversation = new Conversation(chatbotName);

    public Chatbot() throws IOException {
		brain = new Brain();
        brain.setUpBrain();
        user = new User();
        taskStates.put("gettingTopic", State.START);
        taskStates.put("gettingUserAge", State.START);
        taskStates.put("gettingUserName", State.START);
        taskStates.put("gettingUserMood", State.START);
    }

	public Chatbot(User user) {

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

        int note = 0;
        String answer = replacePolishCharsAndLowerCase(getLastAnswer());
        String[] words = answer.split(" ");
        String result = "";

        for (int i = 0; i < words.length; i++) {
            note = catchUserAnswerNote(answer);
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
		String[] introduce = {"mam na imię ", "jestem ", "nazywam się ","mam na imie ",
				"nazywam sie ", "zwę się ", "zwe sie "};
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
                //log.info("outer for - "+phrase.getWord());

                if (phrases[i].equals(phrase.getWord())) {

                    user.updatePersonality(phrase.getIdPersonality(), phrase.getLevel());

                }
            }
		}
        System.out.println(user.getPersonality());
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
		String userAnswerToLowerCaseWithoutPolishChars = replacePolishCharsAndLowerCase(userAnswer).toLowerCase();
		TypeOfSentence typeOfSentence = recognizeTypeOfSentence(userAnswerToLowerCaseWithoutPolishChars);
		if (typeOfSentence.equals(TypeOfSentence.QUESTION)) {
			return answerQuestion(userAnswer);
		}
		if (typeOfSentence.equals(FEELING_STATEMENT)) {
			return getChatbotResponseForFeelingSentence(userAnswer);
		}
		int userAnswerNote = catchUserAnswerNote(userAnswer);
        System.out.println("user answer note: "+userAnswerNote);
        int chooseAnswer = (int)(Math.random()*10);
		//losowa logika - zwraca -1 gry nie zaleziono patternu - to nie blad
		String pharaprasizedAnswer = pharaprasize(userAnswer.toLowerCase());
		String chatbotAnswerFromAnswerPatterns = getChatbotAnswerFromAnswerPatterns(userAnswerNote);
		switch (chooseAnswer) {
			//question:

            case 7:
                return pharaprasizedAnswer + getNeutralEngagedAnswer();
            case 8:
                return pharaprasizedAnswer.length()==0 ? chatbotAnswerFromAnswerPatterns : pharaprasizedAnswer;
            default:
                return pharaprasizedAnswer + chatbotAnswerFromAnswerPatterns;
        }
    }

	public String getChatbotResponseForFeelingSentence(String userAnswer) {
		String verb = "";
		if(userAnswer.toLowerCase().contains("jestem")) {
			verb = "jestem";
		}
		else if (PreprocessString.replacePolishCharsAndLowerCase(userAnswer).contains("czuje")) {
			verb = "czuję";
		}
		else if (PreprocessString.replacePolishCharsAndLowerCase(userAnswer).contains("chce")) {
			verb = "chcę";
		}
		List<String> answerList = brain.getFeelingStatement().get(verb);
		int size = answerList.size();
		String sentence = answerList.get(RandomSearching.generateRandomIndex(size));
		String pharaprasize = pharaprasize(userAnswer);
		String output = sentence.replace("<paraphrase>", pharaprasize.replace(". ",""));
		return output;
	}

	public String answerQuestion(String userAnswer) {
		String[] ar = {userAnswer};
		String[] sentences = userAnswer.split("//?");
		int size = brain.getPatternAnswerForOpinionQuestion().size();
		try {
			if(sentences.length>0)  {
			String sentenceWithReplacedQuestionMarks = sentences[0].replace("?", " ").toLowerCase();
			String opinionVerb = questionAboutOpinion(sentenceWithReplacedQuestionMarks);
			if (!opinionVerb.isEmpty()) {
				return answerForQuestionAboutOpinion(size, sentenceWithReplacedQuestionMarks, opinionVerb);
			}
			for(String word: sentenceWithReplacedQuestionMarks.split(" ")) {

				if (verbIsInDictionary(word) && verbIsInPerson(word, GrammaPerson.SECOND)) {
					return brain.getPatternAnswersForPersonalQuestion().get(RandomSearching.generateRandomIndex(size));
				}
			}
		}
		} catch (UnrecognizeUserAnswerException e1) {
			return "Bardzo proszę pisz jaśniej. Twoja wypowiedź jest bardzo chaotyczna.";
		}
		return "Sformułuj proszę pytanie inaczej.";
	}

	private String answerForQuestionAboutOpinion(int size, String sentenceWithReplacedQuestionMarks, String opinionVerb) throws UnrecognizeUserAnswerException {
		PolishDictionary.Record record = findRecordWithVerbInDictionary(opinionVerb);
		String randomPattern = brain.getPatternAnswerForOpinionQuestion().get(RandomSearching.generateRandomIndex(size));
		String patternAnswer = randomPattern;
		patternAnswer = patternAnswer.replace("<verb>", opinionVerb);
		patternAnswer = patternAnswer.replace("<verb-infinitive>", record.getMainWord());
		if (randomPattern.contains("<paraphrase>")) {

            String paraphrasedQuestion = pharapraseQuestion(sentenceWithReplacedQuestionMarks, opinionVerb);

            if (paraphrasedQuestion.isEmpty()) {
                patternAnswer = patternAnswer.replace("<paraphrase>", "...?");
            } else {
                paraphrasedQuestion.replace("?", "");
                patternAnswer = patternAnswer.replace("<paraphrase>", paraphrasedQuestion);
            }
        }
		return patternAnswer;
	}

	private String pharapraseQuestion(String sentenceWithReplacedQuestionMarks, String opinionVerb) throws UnrecognizeUserAnswerException {
		String[] strings = sentenceWithReplacedQuestionMarks.split(opinionVerb);
		String readyToParaphrasize = strings.length>=2 ? strings[1] : "";
		if (replacePolishCharsAndLowerCase(readyToParaphrasize).replace(",","").startsWith(" ze")) {
            try {
                readyToParaphrasize = readyToParaphrasize.substring(4);
            } catch (IndexOutOfBoundsException e) {
                    throw new UnrecognizeUserAnswerException();
            }
        }
		String[] words = readyToParaphrasize.split(" ");
		Map<Integer, String> verbsRightToParaphrasize = this.findVerbsRightToParaphrasize(words);
		if (verbsRightToParaphrasize.values().contains("")) return "";
		return changeVerbsIntoOpposite(words, verbsRightToParaphrasize, 0).toString();
	}

	private String questionAboutOpinion(String sentenceWithReplacedQuestionMarks) {
		Pattern pattern = Pattern.compile("(.*(uwazasz|myslisz|twierdzisz|sadzisz)+.*)");
		Matcher matcher = pattern.matcher(replacePolishCharsAndLowerCase(sentenceWithReplacedQuestionMarks.toLowerCase()));
		if(!matcher.matches()) {
			return "";
		}
		else {
			String s = matcher.group(2);
			if(s.equals("uwazasz")) {
					return "uważasz";
				}
			if(s.equals("sadzisz")) {
					return "sądzisz";
				}
			if(s.equals("myslisz")){
					return "myślisz";
				}
			return s;
		}
	}

	private boolean verbIsInDictionary(String word) {
		Set<String> verbsNonInfinitive = FluentIterable.from(brain.getDictionary().getVerbs())
				.filter(new Predicate<PolishDictionary.Record>() {
					public boolean apply(PolishDictionary.Record record) {
						return !(record.getForm().getVerbForm().equals(INFINITIVE));
					}
				})
				.transform(new Function<PolishDictionary.Record, String>() {
					public String apply(PolishDictionary.Record record) {
						return record.getWord();
					}
				}).toSet();
		return verbsNonInfinitive.contains(word);
	}

	private boolean verbIsInPerson(String word, GrammaPerson grammaPerson) {
		PolishDictionary.Record verb = brain.getDictionary().findVerb(word);
		if (verb.getForm().getGrammaPerson().equals(grammaPerson)) {
			return true;
		}
		return false;
	}

	private String getNeutralEngagedAnswer() {
        return getChatbotAnswerFromAnswerPatterns(0);
    }

    public String pharaprasize(String userAnswer) {

        String[] ar = {userAnswer};
        String[] sentences = divideIntoSentences(userAnswer, ar);
        for (int i = 0; i < sentences.length; i++) {
            String[] words = sentences[i].split(" ");
			String changedVerb = "";
			Map<Integer, String> mapIndexToChangedVerb;
			mapIndexToChangedVerb = findVerbsRightToParaphrasize(words);
			if (mapIndexToChangedVerb.isEmpty() || mapIndexToChangedVerb.values().contains("")) return "";

			return createPharaprasizedAnswer(words, mapIndexToChangedVerb);
		}
        return "";
    }

	private Map<Integer, String> findVerbsRightToParaphrasize(String[] words) {
		String changedVerb = "";
		Map<Integer, String> mapIndexToChangedVerb = new LinkedHashMap<Integer, String>();
		Set<String> verbsPrimarySecondaryForm = FluentIterable.from(brain.getDictionary().getVerbs())
				.filter(new Predicate<PolishDictionary.Record>() {
					public boolean apply(PolishDictionary.Record record) {
						WordDetails form = record.getForm();
						return !(form.getVerbForm().equals(INFINITIVE)) && !(form.getGrammaPerson().equals(GrammaPerson.THIRD) || form.getGrammaPerson().equals(GrammaPerson.DEFAULT));
					}
				})
				.transform(new Function<PolishDictionary.Record, String>() {
					public String apply(PolishDictionary.Record record) {
						return record.getWord();
					}
				}).toSet();
		for (int j = 0; j < words.length; j++) {
            String tempWord = words[j].toLowerCase().replace(".","").replace(",","");
            if(verbsPrimarySecondaryForm.contains(tempWord)) {
                changedVerb = findPharaprasizedVerbIfVerbIsInDictionary(tempWord);
                mapIndexToChangedVerb.put(j, changedVerb);
            }
        }
		return mapIndexToChangedVerb;
	}

	private String createPharaprasizedAnswer(String[] words, Map<Integer, String> mapIndexToChangedVerb) {
		StringBuffer sb = new StringBuffer();
		sb.append(PatternUtil.addPostfixToVerbAccordingGender(brain.startParaphrase(), userIsAFemale()) + " ");

		sb.append(negativeSentence(words, mapIndexToChangedVerb));
		LinkedList<Integer> indexes = new LinkedList<Integer>(mapIndexToChangedVerb.keySet());
		int firstVerb = 0;
		if(!indexes.isEmpty()) firstVerb = indexes.getFirst();
		sb.append(changeVerbsIntoOpposite(words, mapIndexToChangedVerb, firstVerb));
		if(sb.charAt(sb.length()-1) != '.') sb.append(". ");
		return sb.toString();
	}

	private StringBuffer negativeSentence(String[] words, Map<Integer, String> mapIndexToChangedVerb) {
		StringBuffer sb = new StringBuffer();
		LinkedList<Integer> indexes = new LinkedList<Integer>(mapIndexToChangedVerb.keySet());
		Collections.sort(indexes);
		int firstVerb = indexes.getFirst();
		boolean negation = (firstVerb > 0 && words[firstVerb - 1].toLowerCase().equals("nie"));
		if (negation) {
			sb.append("nie ");
		}
		return sb;
	}

	private StringBuffer changeVerbsIntoOpposite(String[] words, Map<Integer, String> mapIndexToChangedVerb, int firstVerb) {
		StringBuffer sb = new StringBuffer();

		LinkedList<Integer> indexes = new LinkedList<Integer>(mapIndexToChangedVerb.keySet());

		//if(!indexes.isEmpty()) firstVerb =  indexes.getFirst();

		for (int i = firstVerb; i<words.length; i++) {
			if(indexes.contains(i)) {
				sb.append(mapIndexToChangedVerb.get(i));
			}
			else {
				sb.append(words[i]);
			}
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb;
	}

	private String[] divideIntoSentences(String userAnswer, String[] ar) {
        return (userAnswer.contains(".") && !userAnswer.endsWith(".")) ? userAnswer.split(".") : ar;
    }

    private String findPharaprasizedVerbIfVerbIsInDictionary(String word) {
        PolishDictionary.Record record = findRecordWithVerbInDictionary(word.toLowerCase());
        if (record.isEmpty()) return "";
        return getOppositePersonVerb(record).getWord();
    }

    private String createPharaprasizedAnswer(String[] words, int j, String changedVerb, boolean negation) {
        StringBuffer sb = new StringBuffer();
		sb.append(PatternUtil.addPostfixToVerbAccordingGender(brain.startParaphrase(), userIsAFemale()) + " ");
		if (negation) {
			sb.append("nie ");
		}
		sb.append(changedVerb + " ");
		addRestOfUserAnswerToChatbotAnswer(words, j, sb);
        return sb.toString();
    }

    private void addRestOfUserAnswerToChatbotAnswer(String[] words, int j, StringBuffer sb) {
        for (int k = j + 1; k < words.length; k++) {
            sb.append(words[k] + " ");
        }
        sb.deleteCharAt(sb.length() - 1);
        if(sb.charAt(sb.length() - 1)!='.') {
            sb.append(". ");
        }
    }

    private void prepareBeginningOfChatbotAnswer(String changedVerb, boolean negation, StringBuffer sb) {
		sb.append(PatternUtil.addPostfixToVerbAccordingGender(brain.startParaphrase(), userIsAFemale())+" ");
		if (negation) {
			sb.append("nie ");
		}
		sb.append(changedVerb + " ");
	}

    private PolishDictionary.Record findRecordWithVerbInDictionary(String userAnswer) {
        PolishDictionary.Record record = brain.getDictionary().findVerb(userAnswer);
        return record;
    }

    private PolishDictionary.Record getOppositePersonVerb(PolishDictionary.Record record) {
        return brain.getDictionary().findVerbFromMainWordAndMatchOppositePerson(record.getForm().getGrammaPerson(), record.getMainWord(), record.getForm().getSingularOrPlural(), record.getForm().getVerbForm(), record.getForm().getGenre());
    }

    private String getChatbotAnswerFromAnswerPatterns(int userAnswerNote) {
        List<String> suitedAnswers = new LinkedList<String>();
        for(ChatbotAnswer chatbotAnswer : brain.chatbotAnswers ) {
            if (chatbotAnswer.userAnswerNote == userAnswerNote) {
                    suitedAnswers.add(chatbotAnswer.getSentence());
            }
        }
        if (suitedAnswers.size()!=0) {
            int randomIndex = RandomSearching.generateRandomIndex(suitedAnswers.size());

            return suitedAnswers.get(randomIndex);
        }
        else {
            int randomIndex = RandomSearching.generateRandomIndex(brain.getExceptionsChatbotAnswers().size());
			String sentence = brain.getExceptionsChatbotAnswers().get(randomIndex).getSentence();
			if(sentence.isEmpty()) {

			}
			return sentence;
        }
    }


	private String preprocessUserAnswer() {
        return replacePolishCharsAndLowerCase(conversation.getLastAnswer().replace("  ", " ").replace("_", " "));
    }

    public int catchUserAnswerNote(String userAnswer) {
        int noteSum = 0;
        int weights = 0;
        int average = 0;

		//System.out.println("-");
        try {
            for (PatternAnswer pattern : brain.patterns) {

                if (userAnswer.contains(pattern.getSentence())) {
					System.out.println(pattern.getSentence() + " : " + pattern.getImportance());
					noteSum += (pattern.getImportance() * pattern.getNote());
					weights += pattern.getImportance();
				}
			}
            if (weights != 0) {
				average = (noteSum / weights);
			}
            else {
				//TODO: jak nie znajduje patternu to nie rozmawia czy li w ogole nie rozmawia !!!!! AAAA
                return -1;
            }
           /* System.out.println("weights=" + weights);
            System.out.println("noteSum=" + noteSum);
            System.out.println("average=" + average);*/
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
				if (userIsAFemale()) return " Jednak wydajesz się smutna. Czym się martwisz?";
				else return " Jednak wydajesz się smutny. Czym się martwisz?";
			}
			else if (moodIsBad()) return "Widać że tryskasz radością. Mimo wszystko, jakie masz w związku z tym obawy? ";
		}
		else if (suspectedMood ==IS_BAD) {
			if (moodIsGood()) {
				if (userIsAFemale()) return " Wyglądasz na smutną. Czym się martwisz?";
				else return " Wyglądasz na smutnego. Czym się martwisz?";
			}
				else if (moodIsBad()) {
					if (userIsAFemale()) return " Mimo to wydajesz się wesoła. Co jest powodem Twojej radości? ";
					else return " Mimo to wydajesz się wesoły. Co jest powodem Twojej radości? ";
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

    public TypeOfSentence recognizeTypeOfSentence(String s) {
        if (s.contains("?")) return TypeOfSentence.QUESTION;
		if (s.contains("chce") || s.contains("czuje") || s.contains("jestem")) return FEELING_STATEMENT;
        return TypeOfSentence.OTHER;
    }

}
