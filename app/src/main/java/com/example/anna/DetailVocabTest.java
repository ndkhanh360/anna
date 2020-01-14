package com.example.anna;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailVocabTest extends AppCompatActivity {
    ArrayList<Vocabulary_Test> arrVocab = new ArrayList<>();
    ArrayList<Vocabulary_Test> topicVocabs = new ArrayList<>();
    TextView question, topic_view;
    int pos_current, size, correctCount = 0;
    ProgressBar progressBar;
    String topic, answer;
    EditText editText;

    private TextView tv_topic, tv_ques;
    private EditText ed_answers;


    public static final String Topic = "t";
    public static final String Questions = "q";
    public static final String Answers = "a";


    GestureDetector gestureDetector;
    int SWIPE_THRESHOLD = 100;
    int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vocab_test);


        Intent intent = getIntent();
        pos_current = intent.getIntExtra(Topic_Vocab.Pos, 0);
        progressBar = findViewById(R.id.fillin_progressBar);
        question = findViewById(R.id.fillin_tv_question);
        topic_view = findViewById(R.id.fillin_tv_topic);
        editText = findViewById(R.id.fillin_edit_answer);

        // get topic name
        topic = Topic_Vocab_Test.topicTestArrayList.get(pos_current).toString();
        topic_view.setText(topic);
        arrVocab = Topic_Vocab_Test.vocabTestArrayList;

        // get all vocabs belong to that topic
        for (int i = 0; i < arrVocab.size(); i++) {
            if (arrVocab.get(i).getTopic().equals(topic)) {
                topicVocabs.add(arrVocab.get(i));
            }
        }

        Collections.shuffle(topicVocabs);
        size = topicVocabs.size();
        progressBar.setMax(size);
        pos_current = 0;
        showQuestion(pos_current);
    }

    private void showQuestion(int idx) {
        if (idx >= size)
            show_result();
        else {
            editText.getText().clear();
            progressBar.setProgress(idx+1);
            question.setText(topicVocabs.get(idx).getQuestions());
            answer = topicVocabs.get(idx).getAnswers();
        }
    }

    private void show_result() {

    }

    public void btn_onClick_submit_fillin(View view) {
        String editAnswer = editText.getText().toString().toLowerCase();
        if (answer.toLowerCase().equals(editAnswer)) {
            correctCount++;
            new SweetAlertDialog(DetailVocabTest.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Correct!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            pos_current++;
                            showQuestion(pos_current);
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(DetailVocabTest.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Incorrect!")
                    .setContentText("Your answer: " + editAnswer + "\n" + "Correct answer: " + answer.toLowerCase())
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            pos_current++;
                            showQuestion(pos_current);
                        }
                    })
                    .show();
        }
    }


}
