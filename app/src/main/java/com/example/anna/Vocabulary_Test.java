package com.example.anna;

import java.io.Serializable;

public class Vocabulary_Test implements Serializable {
    private String topic;
    private String questions;
    private String answers;


    public Vocabulary_Test(String t, String q, String a)
    {
        topic = t;
        questions = q;
        answers = a;
    }

    public String getTopic() {
        return topic;
    }

    public String getQuestions() {
        return questions;
    }

    public String getAnswers() {
        return answers;
    }
}
