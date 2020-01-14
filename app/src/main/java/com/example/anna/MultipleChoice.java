package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MultipleChoice extends AppCompatActivity {

    ArrayList<Vocabulary> arrVocab = new ArrayList<>();
    ArrayList<Vocabulary> topicVocabs = new ArrayList<Vocabulary>();
    TextView question, topic_view;
    Button answer1, answer2, answer3, answer4;
    int pos_current, size, correctCount = 0;
    ProgressBar progressBar;
    Random rand = new Random();
    String topic, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        Intent intent = getIntent();
        pos_current = intent.getIntExtra(Topic_Vocab.Pos,0);
        progressBar = findViewById(R.id.test_progressBar);
        question = findViewById(R.id.test_tv_question);
        answer1 = findViewById(R.id.test_a);
        answer2 = findViewById(R.id.test_b);
        answer3 = findViewById(R.id.test_c);
        answer4 = findViewById(R.id.test_d);
        topic_view = findViewById(R.id.tv_topic_mtp);

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
        showQuestion(pos_current);
    }

    private void showQuestion(int idx) {
        if (idx >= size)
            show_result();
        else {
            progressBar.setProgress(idx+1);
            question.setText(topicVocabs.get(idx).getMean());
            answer = topicVocabs.get(idx).getWord();
            ArrayList<Integer> array = new ArrayList<>();
            array.add(idx);
            while (array.size() < 4) {
                int tmp = rand.nextInt(size);
                if (!array.contains(tmp) && tmp != idx) {
                    array.add(tmp);
                }
            }
            Collections.shuffle(array);
            answer1.setText(topicVocabs.get(array.get(0)).getWord());
            answer2.setText(topicVocabs.get(array.get(1)).getWord());
            answer3.setText(topicVocabs.get(array.get(2)).getWord());
            answer4.setText(topicVocabs.get(array.get(3)).getWord());

        }
    }

    private void show_result() {
        if (correctCount > size/2) {
            new SweetAlertDialog(MultipleChoice.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Congratulations!")
                    .setContentText("You get " + String.valueOf(correctCount) + "/" + String.valueOf(size) + " correct")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent i =  new Intent(MultipleChoice.this, Topic_Vocab.class);
                            i.putExtra("test", 1);
                            startActivity(i);
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(MultipleChoice.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Try again!")
                    .setContentText("You only get "+ String.valueOf(correctCount) + "/" + String.valueOf(size) + " correct")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent i =  new Intent(MultipleChoice.this, Topic_Vocab.class);
                            i.putExtra("test", 1);
                            startActivity(i);
                        }
                    })
                    .show();
        }


    }

    public void btn_onClick_Answer(View view) {
        String chosen = ((Button) view).getText().toString();
        if (answer.equals(chosen)) correctCount++;
        Log.d("correct", String.valueOf(correctCount));
        pos_current++;
        showQuestion(pos_current);
    }

    public void btn_onClick_submit_mtp(View view) {
        new SweetAlertDialog(MultipleChoice.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you really want to submit?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        show_result();
                    }
                })
                .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
