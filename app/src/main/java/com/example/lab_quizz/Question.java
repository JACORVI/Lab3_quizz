package com.example.lab_quizz;

import java.io.Serializable;

public class Question implements Serializable {
    private String category;
    private String question;
    private boolean correctAnswer;
    private boolean userAnswer;
    private boolean answered;

    public Question(String category, String question, boolean correctAnswer) {
        this.category = category;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answered = false;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(boolean userAnswer) {
        this.userAnswer = userAnswer;
        this.answered = true;
    }

    public boolean isAnswered() {
        return answered;
    }

    public boolean isCorrect() {
        return answered && (userAnswer == correctAnswer);
    }
}