package com.chatbot.model.answer;

public class Answer {
	
	private String sentence;


	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentence() {
		return sentence;
	}

    public Answer() {}
    public Answer(String sentence)
    {
        this.sentence = sentence;
    }
	

}
