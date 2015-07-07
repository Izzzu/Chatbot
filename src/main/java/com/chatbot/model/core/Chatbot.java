package com.chatbot.model.core;

import com.chatbot.model.answer.PatternAnswer;
import com.chatbot.model.answer.TypeOfSentence;
import com.chatbot.model.capabilities.PersonalityId;
import com.chatbot.model.capabilities.PersonalityRecognizer;
import com.chatbot.model.dictionary.GrammaPerson;
import com.chatbot.model.dictionary.PolishDictionary;
import com.chatbot.model.dictionary.WordDetails;
import com.chatbot.model.exceptions.*;
import com.chatbot.model.user.Gender;
import com.chatbot.model.user.User;
import com.chatbot.model.util.PreprocessString;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chatbot.model.answer.TypeOfSentence.*;
import static com.chatbot.model.dictionary.VerbForm.INFINITIVE;
import static com.chatbot.model.user.Gender.*;
import static com.chatbot.model.util.DomainConstants.OPENINGS_OF_QUESTION;
import static com.chatbot.model.util.PreprocessString.removePunctuationMarks;
import static com.chatbot.model.util.PreprocessString.replacePolishCharsAndLowerCase;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Chatbot {

    private Logger log = LoggerFactory.getLogger(Chatbot.class);
    public Brain brain;
    private User user;
    private String chatbotName = "Eustachy";
    private Conversation conversation = new Conversation(chatbotName);

    public Chatbot(Brain brain) {
        this.brain = brain;
        user = new User();
    }

    public Chatbot(Brain brain, User user) {
        this.brain = brain;
        this.user = user;
    }

    public String commentMood() {
        String result = "";
        if (user.getMood() == 0) {
            result = "W takim razie może powiesz mi o czym chcesz ze mną porozmawiać?";
        } else if (user.getMood() > 0 && user.getMood() < 5) {
            result = "Widzę, że humor Ci nie dopisuje. O czym chcesz ze mną porozmawiać?";
        } else {
            result = "Cieszę się, że masz dobry humor. W takim razie powiedz mi o czym chcesz teraz porozmawiać?";
        }
        return result;
    }

    public String catchUserAge() {
        String answer = getLastAnswer();
        Scanner sc = new Scanner(answer);
        String token = "";
        token = sc.findInLine("\\d+");
        if (token != null) {
            int age = Integer.parseInt(token);
            if (age > 100 || age < 4) {
                age = 0;
            }
            user.setAge(age);
        }
        return token;
    }

    public void catchUserMood() {

        int note = 0;
        String answer = replacePolishCharsAndLowerCase(getLastAnswer());
        String[] words = answer.split(" ");
        String result = "";

        for (int i = 0; i < words.length; i++) {
            note = catchUserAnswerNote(answer);
            if (note >= 0) {
                user.setMood(note);
            } else {
            }
        }
    }


    public String catchUserName() {

        String answer = getLastAnswer();
        String[] introduce = {"mam na imię ", "jestem ", "nazywam się ", "mam na imie ",
                "nazywam sie ", "zwę się ", "zwe sie "};
        for (int i = 0; i < introduce.length; i++) {
            if (answer.toLowerCase().contains(introduce[i])) {
                String[] s = answer.toLowerCase().split(introduce[i]);
                if (s.length >= 2) {
                    String[] s2 = s[1].split(" ");
                    if (s2.length > 0) {
                        String name = s2[0].substring(0, 1).toUpperCase().concat(s2[0].substring(1));

                        user.setGender(getGender(name));
                        user.setName(name);
                        return name;
                    }
                } else {
                    user.setGender(NOTKNOWN);
                    user.setName("Nieznajomy");
                    return "Nieznajomy";
                }
            }
        }
        if (!answer.toLowerCase().contains(" ")) {
            if (answer.length() >= 2) {
                String name = answer.substring(0, 1).toUpperCase().concat(answer.substring(1));
                user.setGender(getGender(name));
                user.setName(name);
                return name;
            } else {
                user.setGender(NOTKNOWN);
                user.setName("Nieznajomy");
                return "Nieznajomy";
            }
        } else {
            String[] s = answer.toLowerCase().split(" ");

            if (s.length > 0 && s[0].length() >= 2) {
                String name = s[0].substring(0, 1).toUpperCase().concat(s[0].substring(1));
                user.setGender(getGender(name));
                user.setName(name);
                return name;
            } else {
                user.setGender(NOTKNOWN);
                user.setName("Nieznajomy");
                return "Nieznajomy";
            }
        }
    }

    public void updateInformationAboutUser(String answer) {
        if (user.getGender().equals(NOTKNOWN)) tryToCatchUserGender(answer);
        updatePersonality(answer);
    }

    private void tryToCatchUserGender(String answer) {
        String[] words = answer.toLowerCase().split(" ");
        for (String word : words) {
            Gender gender = brain.getGenderOfVerb(word);
            if (!gender.equals(NOTKNOWN)) {
                user.setGender(gender);
                return;
            }
        }
    }

    public void updatePersonality(String answer) {
        List<PersonalityId> idsToUpdate = new ArrayList<>();

        String userSentence = answer.toLowerCase();
        for (PersonalityRecognizer.Phrase phrase : brain.getPersonalityPhrases()) {
            if (userSentence.contains(phrase.getWord())) {
                userSentence = removeMatchingPhraseFromSentence(userSentence, phrase);
                idsToUpdate.addAll(phrase.getPersonalityIds());
            }
        }
        Map<String, Set<PersonalityId>> personalityMainWords = brain.mainWordToPersonalityIdsWithoutPolishChars();
        String[] userWords = userSentence.split("[\',;/.\\s]+");
        Set<String> convertedToMainWords = convertToMainWords(userWords);
        for (String mainWord : convertedToMainWords) {
            Set<PersonalityId> ids = personalityMainWords.get(PreprocessString.replacePolishCharsAndLowerCase(mainWord));
            if (ids != null) {
                idsToUpdate.addAll(ids);
            }
        }
        updatePersonalities(idsToUpdate);
    }

    private Set<String> convertToMainWords(String[] userWords) {
        Set<String> convertedToMainWords = new HashSet<>();
        for (String userWord : userWords) {
            ImmutableSet<String> mainWords = brain.toMainWords(userWord);
            if (mainWords.isEmpty()) mainWords = brain.toMainWordsWithoutPolishChars(userWord);
            convertedToMainWords.addAll(mainWords);
        }
        return convertedToMainWords;
    }

    private String removeMatchingPhraseFromSentence(String userSentence, PersonalityRecognizer.Phrase phrase) {
        return userSentence.replace(phrase.getWord(), "");
    }

    private void updatePersonalities(List<PersonalityId> ids) {
        for (PersonalityId id : ids) {
            Double toAdd = brain.getPersonalityToAdd(id);
            user.updatePersonality(id, toAdd);
        }
    }

    private Gender getGender(String name) {
        if (name.charAt(name.length() - 1) == 'a') {
            return FEMALE;
        } else return MALE;
    }

    public String prepareAnswer(String lastAnswer) throws Exception {
        String userAnswer = lastAnswer.replace("  ", " ").replace("_", " ");
        String userAnswerToLowerCaseWithoutPolishChars = replacePolishCharsAndLowerCase(userAnswer).toLowerCase();
        TypeOfSentence typeOfSentence = recognizeTypeOfSentence(userAnswerToLowerCaseWithoutPolishChars);

        if (typeOfSentence.equals(TypeOfSentence.STANDARD_DIALOG)) {
            return getAnswerForStadardDialog(userAnswerToLowerCaseWithoutPolishChars);
        }
        if (typeOfSentence.equals(TypeOfSentence.QUESTION)) {
            return answerQuestion(userAnswer.toLowerCase());
        }
        if (typeOfSentence.equals(FEELING_STATEMENT)) {
            return getChatbotResponseForFeelingSentence(userAnswer);
        }
        if (typeOfSentence.equals(TypeOfSentence.SINGLE_WORD)) {
            return answerForSingleWord(removePunctuationMarks(userAnswerToLowerCaseWithoutPolishChars));
        }

        int userAnswerNote = catchUserAnswerNote(userAnswerToLowerCaseWithoutPolishChars);
        int chooseAnswer = (int) (Math.random() * 10);
        //losowa logika - zwraca -1 gry nie zaleziono patternu - to nie blad
        String pharaprasizedAnswer = paraphrase(userAnswer.toLowerCase(), true);
        String exclamation = "";
        if (typeOfSentence.equals(TypeOfSentence.EXCLAMATION)) {
            exclamation = getChatbotAnswerForExclamation();
            if (pharaprasizedAnswer.isEmpty() && !exclamation.isEmpty()) {
                return exclamation;
            }
        }
        String chatbotAnswerFromAnswerPatterns = getChatbotAnswerFromChatbotPatterns(userAnswerNote);
        if(mentionPersonality(chooseAnswer)) {
            String referringPersonality = prepareAnswerReferringPersonality();
            //System.out.println("ref personality:"+referringPersonality);
            if(!referringPersonality.isEmpty()) return referringPersonality;
        }
        switch (chooseAnswer) {
            //question:
            //case 7:
            //    return pharaprasizedAnswer /*+ getNeutralEng
            //dAnswer()*/;
            case 8:
                return pharaprasizedAnswer.isEmpty() ? chatbotAnswerFromAnswerPatterns : pharaprasizedAnswer;
            default:
                return exclamation + pharaprasizedAnswer + " " + chatbotAnswerFromAnswerPatterns;
        }
    }

    private boolean mentionPersonality(int chooseAnswer) {
        return chooseAnswer%3==0 && user.getPersonality().getMainType().getLevel()!=0 && conversation.getCourse().size()>8;
    }

    private String prepareAnswerReferringPersonality() {

        //String topic = user.getTopics().iterator().next().getTopicDescription();
        String answerRefferingPersonality = brain.getAnswerRefferingPersonality(user.getPersonality().getMainType().getPersonalityId());
        return answerRefferingPersonality;
    }

    private String getChatbotAnswerForExclamation() {
        String lastAnswer = getLastAnswer();
        String answer = "";
        do {
            answer = brain.getRandomAnswerForExclamation();
        }
        while (lastAnswer.contains(answer));
        return answer;
    }

    private String getAnswerForStadardDialog(String userAnswerToLowerCaseWithoutPolishChars) {
        String lastChatbotAnswer = tryToGetLastChatbotAnswer();
        String answerForStandardDialog = answerForStandardDialog(userAnswerToLowerCaseWithoutPolishChars);
        while (answerForStandardDialog.equals(lastChatbotAnswer)) {
            answerForStandardDialog = answerForStandardDialog(userAnswerToLowerCaseWithoutPolishChars);
        }
        return answerForStandardDialog;
    }

    private String tryToGetLastChatbotAnswer() {
        String lastChatbotAnswer;
        try {
            lastChatbotAnswer = conversation.getLastChatbotAnswer();
        } catch (Exception e) {
            lastChatbotAnswer = null;
        }
        return lastChatbotAnswer;
    }

    private String answerForStandardDialog(String userAnswerToLowerCaseWithoutPolishChars) {
        return brain.getRandomStandardAnswer(removePunctuationMarks(userAnswerToLowerCaseWithoutPolishChars));
    }

    private String getChatbotAnswerFromChatbotPatterns(int userAnswerNote) throws NotFoundExceptionAnswer {
        String chatbotAnswerFromAnswerPatterns = "";
        String lastChatbotAnswer = tryToGetLastChatbotAnswer();
        do {
            chatbotAnswerFromAnswerPatterns = getChatbotAnswerFromAnswerPatterns(userAnswerNote);
        }
        while (lastChatbotAnswer.contains(chatbotAnswerFromAnswerPatterns));
        return chatbotAnswerFromAnswerPatterns;
    }

    private String answerForSingleWord(String userAnswer) throws NotFoundResponseForOneWordAnswer, NotFoundExceptionAnswer {
        String answer = brain.getRandomPatternForOneWordAnswer();
        int note = catchUserAnswerNote(userAnswer);
        String chatbotAnswerFromAnswerPatterns = getChatbotAnswerFromAnswerPatterns(note);
        return PatternUtil.replaceTags(answer, userAnswer, chatbotAnswerFromAnswerPatterns);
    }

    public String getChatbotResponseForFeelingSentence(String userAnswer) throws NotFoundResponsesForFeelingSentence {
        String verb = "";
        if (userAnswer.toLowerCase().contains("jestem")) {
            verb = "jestem";
        } else if (replacePolishCharsAndLowerCase(userAnswer).contains("czuje")) {
            verb = "czuję";
        } else if (replacePolishCharsAndLowerCase(userAnswer).contains("chce")) {
            verb = "chcę";
        }
        String sentence = brain.getRandomFeelingStatementForVerb(verb);
        String paraphrasize = paraphrase(userAnswer, false).toLowerCase();
        String output = PatternUtil.replaceParaphraseTags(sentence, paraphrasize);
        return PatternUtil.removeDots(output);
    }

    private String tryAnswerForOpinionQuestion(String sentenceWithReplacedQuestionMarks) throws UnrecognizedUserAnswerException, NotFoundResponsesForOpinionQuestion {
        String answerForOpinionQuestion = "";
        String opinionVerb = questionAboutOpinion(sentenceWithReplacedQuestionMarks);
        if (!opinionVerb.isEmpty()) {
            answerForOpinionQuestion = answerForQuestionAboutOpinion(sentenceWithReplacedQuestionMarks, opinionVerb);
        }
        return answerForOpinionQuestion;
    }

    public String answerQuestion(String userAnswer) {
        String[] ar = {userAnswer};
        String[] sentences = userAnswer.split("//?");
        try {
            if (sentences.length > 0) {
                String sentenceWithReplacedQuestionMarks = sentences[0].replace("?", " ").toLowerCase();
                String answerForOpinionQuestion = tryAnswerForOpinionQuestion(sentenceWithReplacedQuestionMarks);
                if (!answerForOpinionQuestion.isEmpty()) return answerForOpinionQuestion;

                if (isPersonalQuestion(sentenceWithReplacedQuestionMarks)) {
                    return brain.getRandomAnswerForPersonalQuestion();
                }
                String answer = getAnswerForQuestion(sentenceWithReplacedQuestionMarks);
                if (!answer.isEmpty()) return answer;
            }
        } catch (UnrecognizedUserAnswerException e1) {
            return "Bardzo proszę pisz jaśniej. Twoja wypowiedź jest bardzo chaotyczna.";
        } catch (NotFoundResponsesForOpinionQuestion notFoundResponsesForOpinionQuestion) {
            notFoundResponsesForOpinionQuestion.printStackTrace();
        } catch (NotFoundExceptionAnswer notFoundExceptionAnswer) {
            notFoundExceptionAnswer.printStackTrace();
        }
        return "Sformułuj proszę pytanie inaczej.";
    }

    private boolean isAnswerForBackQuestion(String sentenceWithReplacedQuestionMarks) {

        return sentenceWithReplacedQuestionMarks.contains("a ty");
    }

    private boolean isPersonalQuestion(String sentenceWithReplacedQuestionMarks) {
        String[] wordsInSentence = sentenceWithReplacedQuestionMarks.split(" ");
        for (String word : wordsInSentence) {
            if (verbIsInDictionary(word) && verbIsInPerson(word, GrammaPerson.SECOND)) {
                return true;
            }
        }
        if (isAnswerForBackQuestion(sentenceWithReplacedQuestionMarks)) return true;
        return false;
    }

    public String getAnswerForQuestion(String sentenceWithReplacedQuestionMarks) throws NotFoundExceptionAnswer {
        String answer = "";
        List<String> wordsInSentence = removeConjuctionFromBeginingOfQuestion(sentenceWithReplacedQuestionMarks.split(" "));

        Map<Integer, String> mapIndexToChangedVerb = findVerbsRightToParaphrasize(wordsInSentence);
        if (mapIndexToChangedVerb.values().contains("")) return "";
        String changedVerbs = changeVerbsIntoOpposite(wordsInSentence, mapIndexToChangedVerb, 0).toString();
        String paraphrasedQuestion = changePronouns(changedVerbs);
        if (!paraphrasedQuestion.isEmpty()) {
            answer = answer + paraphrasedQuestion + " " + getChatbotAnswerFromAnswerPatterns(0);
        }
        if (answer.isEmpty()) {
            return "";
        } else {
            List<String> questions = OPENINGS_OF_QUESTION;
            if (wordsInSentence.size() >= 1 && !questions.contains(wordsInSentence.get(0))) {
                answer = "czy " + answer;
            }
            answer = "Pytasz " + answer;
        }
        return answer;
    }

    private List<String> removeConjuctionFromBeginingOfQuestion(String[] wordsInSentence) {
        List<String> wordsList = Lists.newArrayList(wordsInSentence);
        if (wordsList.size() >= 1 && brain.isConjuction(wordsList.get(0))) {
            wordsList.remove(0);
        }
        return wordsList;
    }

    private String answerForQuestionAboutOpinion(String sentenceWithReplacedQuestionMarks, String opinionVerb) throws UnrecognizedUserAnswerException, NotFoundResponsesForOpinionQuestion {
        PolishDictionary.Record record = findRecordWithVerbInDictionary(opinionVerb);
        String randomPattern = brain.getRandomAnswerForOpinionQuestion();
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

    private String pharapraseQuestion(String sentenceWithReplacedQuestionMarks, String opinionVerb) throws UnrecognizedUserAnswerException {
        String[] strings = sentenceWithReplacedQuestionMarks.split(opinionVerb);
        String readyToParaphrasize = strings.length >= 2 ? strings[1] : "";
        if (replacePolishCharsAndLowerCase(readyToParaphrasize).replace(",", "").startsWith(" ze")) {
            try {
                readyToParaphrasize = readyToParaphrasize.substring(4);
            } catch (IndexOutOfBoundsException e) {
                throw new UnrecognizedUserAnswerException();
            }
        }
        List<String> words = Lists.newArrayList(readyToParaphrasize.split(" "));
        Map<Integer, String> verbsRightToParaphrasize = this.findVerbsRightToParaphrasize(words);
        if (verbsRightToParaphrasize.values().contains("")) return "";
        return changeVerbsIntoOpposite(words, verbsRightToParaphrasize, 0).toString();
    }

    private String questionAboutOpinion(String sentenceWithReplacedQuestionMarks) {
        Pattern pattern = Pattern.compile("(.*(uwazasz|myslisz|twierdzisz|sadzisz)+.*)");
        Matcher matcher = pattern.matcher(replacePolishCharsAndLowerCase(sentenceWithReplacedQuestionMarks.toLowerCase()));
        if (!matcher.matches()) {
            return "";
        } else {
            String s = matcher.group(2);
            if (s.equals("uwazasz")) {
                return "uważasz";
            }
            if (s.equals("sadzisz")) {
                return "sądzisz";
            }
            if (s.equals("myslisz")) {
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

    public String paraphrase(String userAnswer, boolean addBeginParaphrase) {

        String[] ar = {userAnswer.replace("!", "").replace("?", "")};
        String[] sentences = divideIntoSentences(userAnswer, ar);
        for (int i = 0; i < sentences.length; i++) {
            List<String> words = Lists.newArrayList(sentences[i].split(" "));
            String changedVerb = "";
            Map<Integer, String> mapIndexToChangedVerb;
            mapIndexToChangedVerb = findVerbsRightToParaphrasize(words);
            if (mapIndexToChangedVerb.isEmpty() || mapIndexToChangedVerb.values().contains("")) return "";
            String begin = " ";
            if (addBeginParaphrase)
                begin = PatternUtil.addPostfixToVerbAccordingGender(brain.startParaphrase(), userIsAFemale()) + " ";
            /*if(mapIndexToChangedVerb.size() == 1 && mapIndexToChangedVerb.keySet().iterator().next().equals(Integer.valueOf(words.length-1))) {

            }*/
            String answer = createPharaprasizedAnswer(words, mapIndexToChangedVerb, begin);
            return changePronouns(answer);
        }
        return "";
    }

    private String changePronouns(String answer) {
        String[] words = answer.replace(".", "").replace(",", "").split(" ");
        StringBuffer buffer = new StringBuffer();
        for (String word : words) {
            String changedPronoun = "";
            if (!word.isEmpty() && brain.isPronoun(word)) {
                changedPronoun = changeToOpposite(word);
                if (changedPronoun != null) {
                    buffer.append(changedPronoun);
                }
            } else {
                buffer.append(word);
            }
            buffer.append(" ");
        }
        int length = buffer.length() - 1;
        if (buffer.substring(length).equals(" ")) {
            buffer.replace(length, length + 1, ".");
        }
        return buffer.toString();
    }

    private String changeToOpposite(String word) {
        return brain.changePronounToOpposite(word);
    }


    private Map<Integer, String> findVerbsRightToParaphrasize(List<String> words) {
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
        for (int j = 0; j < words.size(); j++) {
            String tempWord = words.get(j).toLowerCase().replace(".", "").replace(",", "");
            if (verbsPrimarySecondaryForm.contains(tempWord)) {
                changedVerb = findPharaprasizedVerbIfVerbIsInDictionary(tempWord);
                mapIndexToChangedVerb.put(j, changedVerb);
            }
        }
        return mapIndexToChangedVerb;
    }

    private String createPharaprasizedAnswer(List<String> words, Map<Integer, String> mapIndexToChangedVerb, String begin) {
        StringBuffer sb = new StringBuffer();
        sb.append(begin);

        //sb.append(negativeSentence(words, mapIndexToChangedVerb));
        LinkedList<Integer> indexes = new LinkedList<Integer>(mapIndexToChangedVerb.keySet());
        int firstVerb = 0;
        //if (!indexes.isEmpty()) firstVerb = indexes.getFirst();
        sb.append(changeVerbsIntoOpposite(words, mapIndexToChangedVerb, firstVerb).toString().toLowerCase());
        if (sb.charAt(sb.length() - 1) != '.') sb.append(". ");
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

    private StringBuffer changeVerbsIntoOpposite(List<String> words, Map<Integer, String> mapIndexToChangedVerb, int firstVerb) {
        StringBuffer sb = new StringBuffer();

        LinkedList<Integer> indexes = new LinkedList<Integer>(mapIndexToChangedVerb.keySet());

        //if(!indexes.isEmpty()) firstVerb =  indexes.getFirst();

        for (int i = firstVerb; i < words.size(); i++) {
            if (indexes.contains(i)) {
                sb.append(mapIndexToChangedVerb.get(i));
            } else {
                sb.append(words.get(i));
            }
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
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

    private void addRestOfUserAnswerToChatbotAnswer(String[] words, int j, StringBuffer sb) {
        for (int k = j + 1; k < words.length; k++) {
            sb.append(words[k] + " ");
        }
        sb.deleteCharAt(sb.length() - 1);
        if (sb.charAt(sb.length() - 1) != '.') {
            sb.append(". ");
        }
    }

    private void prepareBeginningOfChatbotAnswer(String changedVerb, boolean negation, StringBuffer sb) {
        sb.append(PatternUtil.addPostfixToVerbAccordingGender(brain.startParaphrase(), userIsAFemale()) + " ");
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

    public String getChatbotAnswerFromAnswerPatterns(int userAnswerNote) throws NotFoundExceptionAnswer {
        String suitedAnswer = brain.getRandomSuitedAnswersForNote(userAnswerNote);
        if (!suitedAnswer.isEmpty()) {
            return suitedAnswer;
        } else {
            return getRandomExceptionAnswer();
        }
    }

    private String getRandomExceptionAnswer() throws NotFoundExceptionAnswer {
        return brain.getRandomExceptionAnswer();
    }

    public int catchUserAnswerNote(String userAnswer) {
        int noteSum = 0;
        int weights = 0;
        int average = 0;

        //System.out.println("-");
        String tempAnswer = userAnswer;
        try {
            for (PatternAnswer pattern : brain.getComplexPatterns()) {
                if (PreprocessString.computeLevenshteinDistance(tempAnswer, pattern.getSentence()) < 2) {
                    //if (tempAnswer.contains(pattern.getSentence())) {
                    noteSum += (pattern.getImportance() * pattern.getNote());
                    weights += pattern.getImportance();
                    tempAnswer = tempAnswer.replace(pattern.getSentence(), "");

                    updateTopicsAndLcu(pattern);
                }
            }
            String[] splittedWords = tempAnswer.split(" ");
            for (String word : splittedWords) {
                PatternAnswer oneWord = brain.getOneWordPattern(word);

                if (oneWord != null) {
                    noteSum += (oneWord.getImportance() * oneWord.getNote());
                    weights += oneWord.getImportance();
                    tempAnswer = tempAnswer.replace(oneWord.getSentence(), "");

                    updateTopicsAndLcu(oneWord);
                }
            }
            if (weights != 0) {
                average = (noteSum / weights);
            } else {
                //TODO: jak nie znajduje patternu to nie rozmawia czy li w ogole nie rozmawia !!!!! AAAA
                return -1;
            }
            return average;
        } catch (Exception e) {
            System.out.println(e);
        }
        return average;
    }

    private void updateTopicsAndLcu(PatternAnswer pattern) {
        Topic topic = brain.getTopic(pattern.getTopicId());
        user.updateTopicAndLcu(topic);
    }

    public boolean isUserTurn() {
        return conversation.isUserTurn();
    }

    public String getLastAnswer() {
        return conversation.getLastAnswer();
    }

    public void addUserAnswer(String s) {
        conversation.addChatbotAnswerToCourse(s);
    }

    public void answer() throws Exception {

        int chatlevel = conversation.getChatLevel();
        String answer ="";
        switch (chatlevel) {
            case 1:
                catchUserName();
                answer = "Witaj " + getUserName() + ". Na początku naszej rozmowy chciałbym zadać Ci kilka pytań. Ile masz lat?";
                System.out.print("Chatbot: "+answer);
                conversation.addChatbotAnswerToCourse(answer);
                break;
            case 2:
                catchUserAge();
                answer = "";
                if (user.getAge() == 0) {
                    answer = "Nie odpowiadaj jeśli nie chcesz. "+brain.getRandomStandardAnswer("2");
                }
                else {
                    answer = brain.getRandomStandardAnswer("2");
                }
                System.out.print("Chatbot: "+answer);
                conversation.addChatbotAnswerToCourse(answer);
                break;
            case 3:
                catchUserMood();
                answer = commentMood();
                System.out.print("Chatbot: "+answer);
                conversation.addChatbotAnswerToCourse(answer);
                break;
        /*case 4:
            catchTopic();
			conversation.addChatbotAnswerToCourse(prepareAnswer(chatlevel, 0, conversation.getTopicID()));
			break;*/
            default:
                answer = prepareAnswer(conversation.getLastAnswer());
                System.out.print("Chatbot: "+answer);
                conversation.addChatbotAnswerToCourse(answer);
                break;
        }
        conversation.chatLevelUp();
    }

    public List<String> getConversationCourse() {
        return conversation.getCourse();
    }

    public String getUserName() {
        return user.getName();
    }

    private boolean userIsAFemale() {
        return user.getGender() == FEMALE;
    }

    public TypeOfSentence recognizeTypeOfSentence(String s) {
        if (brain.standardDialogsContainsAnswer(s.replace("?", "").replace(".", ""))) return STANDARD_DIALOG;
        if (s.split(" ").length == 1) return SINGLE_WORD;
        if (s.charAt(s.length() - 1) == '!') return EXCLAMATION;
        if (s.contains("?")) return TypeOfSentence.QUESTION;
        if (s.contains("chce") || s.contains("czuje") || s.contains("jestem")) return FEELING_STATEMENT;
        return TypeOfSentence.OTHER;
    }

}
