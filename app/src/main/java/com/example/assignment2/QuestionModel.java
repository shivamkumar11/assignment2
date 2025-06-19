package com.example.assignment2;

public class QuestionModel {
    private String question, category, answer;

    public QuestionModel() {}

    public QuestionModel(String question, String category, String answer) {
        this.question = question;
        this.category = category;
        this.answer = answer;
    }

    public String getQuestion() { return question; }
    public String getCategory() { return category; }
    public String getAnswer() { return answer; }

    public void setQuestion(String question) { this.question = question; }
    public void setCategory(String category) { this.category = category; }
    public void setAnswer(String answer) { this.answer = answer; }
}
