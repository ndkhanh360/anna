package com.example.anna;

import java.io.Serializable;

public class Vocabulary implements Serializable {
    private String topic;
    private String word;
    private String mean;
    private String example;

    public Vocabulary(String t, String w, String m, String e)
    {
        topic = t;
        word = w;
        mean = m;
        example = e;
    }

    public String getTopic() {
        return topic;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }

    public String getExample() {
        return example;
    }
}
