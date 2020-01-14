package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListeningTest extends AppCompatActivity {
    ArrayList<Vocabulary> arrVocab = new ArrayList<>();
    ArrayList<Vocabulary> topicVocabs = new ArrayList<Vocabulary>();
    int pos_current, size, correctCount = 0;
    ProgressBar progressBar;
    String topic, answer, definition;
    TextToSpeech textToSpeech;
    TextView topic_view;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_test);


        Intent intent = getIntent();
        pos_current = intent.getIntExtra(Topic_Vocab.Pos,0);

        topic_view = findViewById(R.id.listen_tv_topic);
        editText = findViewById(R.id.listen_et_answer);
        editText.requestFocus();
        progressBar = findViewById(R.id.listen_progressBar);

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        // get topic name
        topic = Topic_Vocab.topicArrayList.get(pos_current).toString();
        topic_view.setText(topic);
        arrVocab = Topic_Vocab.vocabArrayList;

        // get all vocabs belong to that topic
        for (int i = 0;i <arrVocab.size(); i++)
        {
            if (arrVocab.get(i).getTopic().equals(topic))
            {
                topicVocabs.add(arrVocab.get(i));
            }
        }

        Collections.shuffle(topicVocabs);
        size = topicVocabs.size();
        progressBar.setMax(size);
        pos_current = 0;

        playQuestion(pos_current);
    }

    private void playQuestion(int idx) {
        if (idx >= size)
            show_result();
        else {
            editText.getText().clear();
            progressBar.setProgress(idx+1);
            answer = topicVocabs.get(idx).getWord();
            definition = topicVocabs.get(idx).getMean();
            play(answer);
        }
    }

    private void play(String word) {
        Log.d("word", word);
        textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void btn_onClick_Play_listen(View view) {
        play(answer);
    }

    private void show_result() {
        new SweetAlertDialog(ListeningTest.this)
                .setTitleText("Your result")
                .setContentText("You get " + String.valueOf(correctCount) + "/" + String.valueOf(size) + " correct")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent i =  new Intent(ListeningTest.this, Topic_Vocab.class);
                        i.putExtra("test", 1);
                        startActivity(i);
                    }
                })
                .show();
    }

    public void btn_onClick_submit_listen(View view) {
        String editAnswer = editText.getText().toString().toLowerCase();
        if (answer.toLowerCase().equals(editAnswer)) {
            correctCount++;
            new SweetAlertDialog(ListeningTest.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Correct!")
                    .setContentText("Answer: " + answer.toLowerCase() + "\n" + "Definition: " + definition)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            pos_current++;
                            playQuestion(pos_current);
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(ListeningTest.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Incorrect!")
                    .setContentText("Your answer: " + editAnswer + "\n" + "Correct answer: " + answer.toLowerCase() + "\n" + "Definition: " + definition)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            pos_current++;
                            playQuestion(pos_current);
                        }
                    })
                    .show();
        }
    }
}
