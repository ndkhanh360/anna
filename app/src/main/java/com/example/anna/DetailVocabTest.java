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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailVocabTest extends AppCompatActivity {
    private TextView tv_topic, tv_ques, tv_result;
    private EditText ed_answers;
    private  Button btnSubmit;

    private LinearLayout ln;
    ArrayList<Vocabulary_Test> arrVocab = new ArrayList<>();

    Vocabulary_Test vocab;

    int count_right = 0; // dem so cau dung
    int count_wrong = 0; // dem so cau sai

    public static final String Topic = "t";
    public static final String Questions = "q";
    public static final String Answers = "answer1";


    GestureDetector gestureDetector;
    int SWIPE_THRESHOLD = 100;
    int SWIPE_VELOCITY_THRESHOLD = 100;

    int pos_current, end;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vocab_test);


        arrVocab = Topic_Vocab_Test.vocabTestArrayList;


        tv_topic = findViewById(R.id.tv_topic_test);
        tv_ques = findViewById(R.id.tvQuestion);
        ed_answers = findViewById(R.id.editAnswer);
        tv_result = findViewById(R.id.tvResult);
        ln = findViewById(R.id.lnQA);

        btnSubmit = findViewById(R.id.btn_submit);

        intent = getIntent();
        pos_current = intent.getIntExtra("pos",0);


        for (int i = 0;i <Topic_Vocab_Test.vocabTestArrayList.size(); i++)
        {
            if (Topic_Vocab_Test.vocabTestArrayList.get(i).getTopic() == Topic_Vocab_Test.topicTestArrayList.get(pos_current))
            {
                pos_current = i;
                break;
            }
        }
        end = getEnd(Topic_Vocab_Test.topicTestArrayList.get(pos_current), arrVocab);
        showDetail(pos_current);

        gestureDetector = new GestureDetector(this, new MyGesture());

        ln.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_result.setText("Right: " + String.valueOf(count_right) + " - " + "Wrong: "+ String.valueOf(count_wrong));

            }
        });

    }

    class MyGesture extends  GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
            {
                ed_answers.setText("");
                tv_result.setText("");

                String topic = tv_topic.getText().toString();

                pos_current++;
                if (pos_current > end)
                {
                    pos_current = end;
                    Button btnFinish = findViewById(R.id.btn_finish);
                    btnFinish.setVisibility(View.VISIBLE);
//                    Toast.makeText(DetailVocabTest.this, "This is the last question", Toast.LENGTH_LONG).show();
                }
                else {
                    while (arrVocab.get(pos_current).getTopic().compareTo(topic) != 0 && pos_current <= end) {
                        pos_current++;
                    }
                }
                showDetail(pos_current);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void showDetail(int pos)
    {
        vocab = arrVocab.get(pos);
        intent.putExtra(Topic, vocab.getTopic());
        intent.putExtra(Questions, vocab.getQuestions());
        intent.putExtra(Answers, vocab.getAnswers());

        tv_topic.setText(intent.getStringExtra(Topic));
        tv_ques.setText(intent.getStringExtra(Questions));

    }


    public void btn_onClick_Check(View view)
    {
        String yourAns = ed_answers.getText().toString();

        String s = arrVocab.get(pos_current).getAnswers();

        if (yourAns.compareTo(s) == 0)
        {
            count_right++;
            tv_result.setText("Correct!");
            tv_result.setTextColor(Color.parseColor("#3e8515"));
        }
        else {
            count_wrong++;
            tv_result.setText("Incorrect, try again!");
            tv_result.setTextColor(Color.RED);
        }
    }

    public void btn_onClick_Finish(View view)
    {
        Intent i = new Intent(DetailVocabTest.this, Topic_Vocab_Test.class);
        startActivity(i);
    }

    private int getEnd(String topic, ArrayList<Vocabulary_Test> v)
    {
        int c = 0;
        for (int i = 0;i<v.size();i++)
        {
            if (topic.compareTo(v.get(i).getTopic()) == 0) c=i;
        }
        return c;
    }

}

