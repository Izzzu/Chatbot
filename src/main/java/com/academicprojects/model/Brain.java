package com.academicprojects.model;

import com.academicprojects.model.capabilities.ActiveListening;
import com.academicprojects.model.capabilities.ConversationCapability;
import com.academicprojects.model.capabilities.UnderstandingCapability;
import com.academicprojects.model.dictionary.GrammaPerson;
import com.academicprojects.model.dictionary.PolishDictionary;
import com.academicprojects.model.dictionary.WordDetails;
import com.academicprojects.util.RandomSearching;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.academicprojects.util.RandomSearching.generateRandomIndex;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Brain {
    private ConversationCapability conversationCapability;
    private UnderstandingCapability understandingCapability;
    private PersonalityRecognizer personalityRecognizer;
    private ActiveListening activeListening;
    private PolishDictionary dictionary = new PolishDictionary();

    public void setUpBrain() throws IOException, SQLException {
        try {
            conversationCapability = new ConversationCapability();
            understandingCapability = new UnderstandingCapability();
            personalityRecognizer = new PersonalityRecognizer();
            activeListening = new ActiveListening();

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
            if (chatbotAnswer.userAnswerNote == userAnswerNote) {
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
        return personalityRecognizer.getPersonalityPhrases();
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

  /*  public void getPersonalitiesFromDatabase(Connection conn) throws SQLException {
        String sql = "SELECT * FROM PERSONALITY";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) personalityTypes.add(new PersonalityType(rs.getInt(1), rs.getNString(3), rs.getNString(2), 0));
        System.out.println("size: "+personalityTypes.size());
    }*/


}