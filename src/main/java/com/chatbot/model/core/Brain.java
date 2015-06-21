package com.chatbot.model.core;

import com.chatbot.model.answer.ChatbotAnswer;
import com.chatbot.model.answer.PatternAnswer;
import com.chatbot.model.capabilities.*;
import com.chatbot.model.dictionary.Genre;
import com.chatbot.model.dictionary.GrammaPerson;
import com.chatbot.model.dictionary.PolishDictionary;
import com.chatbot.model.dictionary.WordDetails;
import com.chatbot.model.exceptions.NotFoundExceptionAnswer;
import com.chatbot.model.exceptions.NotFoundResponseForOneWordAnswer;
import com.chatbot.model.exceptions.NotFoundResponsesForFeelingSentence;
import com.chatbot.model.exceptions.NotFoundResponsesForOpinionQuestion;
import com.chatbot.model.user.Gender;
import com.chatbot.model.util.PreprocessString;
import com.chatbot.model.util.RandomSearching;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static com.chatbot.model.util.RandomSearching.generateRandomIndex;

@Getter
public class Brain {
    private ConversationCapability conversationCapability;
    private UnderstandingCapability understandingCapability;
    private PersonalityRecognizer personalityRecognizer;
    private ActiveListening activeListening;
    private PolishDictionary dictionary = new PolishDictionary();
    final static Logger logger = LoggerFactory.getLogger(Brain.class);

    public Brain() throws IOException, SQLException {
        try {
            conversationCapability = new ConversationCapability();
            understandingCapability = new UnderstandingCapability();
            personalityRecognizer = new PersonalityRecognizer();
            activeListening = new ActiveListening();
            logger.debug("Constructor of Brain class ");
        } finally {

        }
    }

    public String startParaphrase() {

        List<String> paraphraseStart = activeListening.getParaphraseStart();
        return paraphraseStart.get(generateRandomIndex(paraphraseStart.size()));

    }

    public String getRandomFeelingStatementForVerb(String verb) throws NotFoundResponsesForFeelingSentence {
        List<String> feelingStatementsForVerb = conversationCapability.getFeelingStatementsForVerb(verb);
        int size = feelingStatementsForVerb.size();
        if(size<=0) {
            throw new NotFoundResponsesForFeelingSentence(verb);
        }
        int randomIndex = generateRandomIndex(size);
        return feelingStatementsForVerb.get(randomIndex);
    }

    public String getRandomAnswerForOpinionQuestion() throws NotFoundResponsesForOpinionQuestion {
        List<String> answersForOpinionQuestion = conversationCapability.getPatternAnswerForOpinionQuestion();
        int size = answersForOpinionQuestion.size();
        if(size<=0) {
            throw new NotFoundResponsesForOpinionQuestion();
        }
        int randomIndex = generateRandomIndex(size);
        return answersForOpinionQuestion.get(randomIndex);
    }

    public String getRandomPatternForOneWordAnswer() throws NotFoundResponseForOneWordAnswer {
        List<String> answersForOneWordSentence = conversationCapability.getPatternsForOneWordAnswers();
        int size = answersForOneWordSentence.size();
        if(size<=0) {
            throw new NotFoundResponseForOneWordAnswer();
        }
        int randomIndex = generateRandomIndex(size);
        return answersForOneWordSentence.get(randomIndex);

    }

    public List<PatternAnswer> getComplexPatterns() {
        return understandingCapability.getComplexPatterns();
    }

    public PatternAnswer getOneWordPattern(String word) {
        return understandingCapability.getOneWordPattern(word);
    }

    public String getRandomExceptionAnswer() throws NotFoundExceptionAnswer {
        List<String> exceptionsChatbotAnswers = conversationCapability.getExceptionsChatbotAnswers();
        int size = exceptionsChatbotAnswers.size();
        if(size<=0) {
            throw new NotFoundExceptionAnswer();
        }
        int randomIndex = generateRandomIndex(size);
        return exceptionsChatbotAnswers.get(randomIndex);
    }

    public List<ChatbotAnswer> getChatbotAnswers() {
        return conversationCapability.getChatbotAnswers();
    }

    public List<String> getSuitedAnswersForNote(int userAnswerNote) {
        List<String> suitedAnswers = new LinkedList<>();
        for(ChatbotAnswer chatbotAnswer : getChatbotAnswers() ) {
            if (chatbotAnswer.getUserAnswerNote() == userAnswerNote) {
                suitedAnswers.add(chatbotAnswer.getSentence());
            }
        }
        return suitedAnswers;
    }

    public String getRandomSuitedAnswersForNote(int userAnswerNote) {
        List<String> suitedAnswers = getSuitedAnswersForNote(userAnswerNote);
        int size = suitedAnswers.size();
        int randomIndex = RandomSearching.generateRandomIndex(size);
        return size==0 ? "" : suitedAnswers.get(randomIndex);
    }

    public List<PersonalityRecognizer.Phrase> getPersonalityPhrases() {
        return ImmutableList.copyOf(personalityRecognizer.getPersonalityPhrases());
    }

    public List<String> getPatternsForPersonalQuestions() {
        return conversationCapability.getPatternAnswersForPersonalQuestion();
    }

    public String getRandomAnswerForPersonalQuestion() {
        List<String> personalQuestion = getPatternsForPersonalQuestions();
        int size = personalQuestion.size();
        int randomIndex = RandomSearching.generateRandomIndex(size);
        return size==0 ? "" : personalQuestion.get(randomIndex);
    }

    public List<String> getPatternsForOneWordAnswer() {
        return ImmutableList.copyOf(conversationCapability.getPatternsForOneWordAnswers());
    }

    public boolean isPronoun(String word) {
        return activeListening.wordIsPronounWhichCanBeParaphrased(word);
    }

    public String changePronounToOpposite(String word) {
        return activeListening.getOppositePronounOf(word);
    }

    private boolean matchOppositePronoun(WordDetails form, WordDetails recordForm) {
        return form.getGrammaCase().equals(recordForm.getGrammaCase()) && form.getSingularOrPlural().equals(recordForm.getSingularOrPlural()) && !form.getGrammaPerson().equals(recordForm.getGrammaPerson()) && !form.getGrammaPerson().equals(GrammaPerson.THIRD);
    }

    public String getRandomStandardAnswer(String i) {
        return conversationCapability.getRandomStandardAnswer(i);
    }

    public boolean standardDialogsContainsAnswer(String s) {
        return !conversationCapability.getStandardAnswersFor(s).isEmpty();
    }

    public Gender getGenderOfVerb(final String word) {
        boolean wordIsVerb = FluentIterable.from(dictionary.getVerbs()).anyMatch(new Predicate<PolishDictionary.Record>() {
            @Override
            public boolean apply(PolishDictionary.Record record) {
                return record.getWord().equals(word);
            }
        });
        if(wordIsVerb) {
            PolishDictionary.Record verb = dictionary.findVerb(word);
            Genre genre = verb.getGenre();
            switch (genre) {
                case MALE: return Gender.MALE;
                case FEMALE: return Gender.FEMALE;
                default: return Gender.NOTKNOWN;
            }
        }
        return Gender.NOTKNOWN;
    }

    public Map<String, Set<PersonalityId>> mainWordToPersonalityIdsWithoutPolishChars() {
        Map<String, Set<PersonalityId>> map = new HashMap<>();
        List<PersonalityRecognizer.Phrase> personalityPhrases = getPersonalityPhrases();
        Map<String, List<PolishDictionary.Record>> mainWordToOtherWords = dictionary.getMainWordToOtherWords();
        for (PersonalityRecognizer.Phrase personalityPhrase : personalityPhrases) {
            String word = personalityPhrase.getWord();
            if(mainWordToOtherWords.get(word) != null) {
                map.put(PreprocessString.replacePolishCharsAndLowerCase(word), personalityPhrase.getPersonalityIds());
            }
        }
        return map;
    }

    public ImmutableSet<String> toMainWords(String userWord) {
        return dictionary.convertToMainWords(userWord);
    }

    public ImmutableSet<String> toMainWordsWithoutPolishChars(String userWord) {
        return dictionary.convertToMainWordsWithoutPolishChars(userWord);
    }

    public boolean isConjuction(String s) {
        return dictionary.isConjuction(s);
    }

    public String getRandomAnswerForExclamation() {
        List<String> answers = conversationCapability.getPatternAnswersForExclamations();
        int size = answers.size();
        int randomIndex = RandomSearching.generateRandomIndex(size);
        return size==0 ? "" : answers.get(randomIndex);
    }

  /*  public void getPersonalitiesFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM PERSONALITY";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) personalityTypes.add(new PersonalityType(rs.getInt(1), rs.getNString(3), rs.getNString(2), 0));
        System.out.println("size: "+personalityTypes.size());
    }*/


}