package com.chatbot.model.core;


public class PatternUtil {
    public static String addPostfixToVerbAccordingGender(String verb, boolean isFemale) {
        if(isFemale) {
            return verb.replace("<gender>", "aś");
        } else {
            return verb.replace("<gender>", "eś");
        }
    }

    public static String replaceTags(String answer, String userAnswer, String chatbotAnswerFromAnswerPatterns) {
        String response = answer;
        answer = answer.replace("<word>", userAnswer);
        answer = answer.replace("<answerFromPatterns>", chatbotAnswerFromAnswerPatterns);
        return answer;

    }

    public static String replaceParaphraseTags(String sentence, String paraphrasize) {
        return sentence.replace("<paraphrase>", paraphrasize);
    }

    public static String removeDots(String output) {
        return output.replace(". ","").replace(".","");
    }
}
