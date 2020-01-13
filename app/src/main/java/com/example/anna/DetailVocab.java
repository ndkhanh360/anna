package com.example.anna;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class DetailVocab extends AppCompatActivity {
    private TextView tv_save, tv_topic;
    String word;
    private  Button btnNext, btnFinish;
    ImageView imageView;

    ArrayList<Vocabulary> arrVocab = new ArrayList<>();
    TextToSpeech textToSpeech;
    Vocabulary vocab;

    public static final String Topic = "t";
    public static final String Word = "w";
    public static final String Mean = "m";
    public static final String Example = "e";
    public static final String Content = "c";

    GestureDetector gestureDetector;
    int SWIPE_THRESHOLD = 100;
    int SWIPE_VELOCITY_THRESHOLD = 100;

    int pos_current, start, end;
    int index = 1;
    Intent intent;

    LinearLayout ln;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vocab);

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        arrVocab = Topic_Vocab.vocabArrayList;

        tv_topic = findViewById(R.id.tv_topic);
//        tv_content = findViewById(R.id.tv_content);
        tv_save = findViewById(R.id.tv_word);

        ln = findViewById(R.id.ln_word);
        imageView = findViewById(R.id.image);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.vocab_btn_finish);

        intent = getIntent();
        pos_current = intent.getIntExtra(Topic_Vocab.Pos,0);


        for (int i = 0;i <arrVocab.size(); i++)
        {
            if (arrVocab.get(i).getTopic() == Topic_Vocab.topicArrayList.get(pos_current))
            {
                pos_current = i;
                break;
            }
        }
        showDetail(pos_current, index);
        start = pos_current;
        end = getEnd(Topic_Vocab.topicArrayList.get(pos_current), arrVocab);

        gestureDetector = new GestureDetector(this, new MyGesture());

        ln.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    public void btn_onClick_VocabFinish(View view) {
        Intent i = new Intent(DetailVocab.this, Topic_Vocab.class);
        startActivity(i);
    }

    public void btn_onClick_VocabYouglish(View view) {
        Intent i = new Intent(DetailVocab.this, YouGlish.class);
        String url = "https://youglish.com/pronounce/" + word.toLowerCase() + "/english";
        i.putExtra("url", url);
        Log.d("url", url);
        startActivity(i);
    }

    public void btn_onClick_Play(View view) {
        playVocabText();
    }

    private void playVocabText() {
        String content = tv_save.getText().toString();
        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
    }

    class MyGesture extends  GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
            {
                index = ++index % 4;
                showDetail(pos_current, index);
            }

            if (e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
            {
                index = (--index + 4) % 4;
                showDetail(pos_current, index);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void showDetail(int pos, int index)
    {
        // index = 1 -> word
        // index = 2 -> definition
        // index = 3 -> example
        vocab = arrVocab.get(pos);
        intent.putExtra(Topic, vocab.getTopic());

        intent.putExtra(Word, vocab.getWord());
        intent.putExtra(Mean, vocab.getMean());
        intent.putExtra(Example, vocab.getExample());

        if(textToSpeech !=null){
            textToSpeech.stop();
        }

        if (index == 1) {
            intent.putExtra(Content, "  Word  ");
            tv_topic.setText(intent.getStringExtra(Topic));
//            tv_content.setText(intent.getStringExtra(Content));
            tv_save.setTextSize(40);
            word = intent.getStringExtra(Word);
            tv_save.setText(word);
            imageView.setImageResource(R.drawable.im_word);
        }

        if (index == 2) {
            intent.putExtra(Content, "  Definition  ");
            tv_topic.setText(intent.getStringExtra(Topic));
//            tv_content.setText(intent.getStringExtra(Content));
            tv_save.setTextSize(20);
            tv_save.setText(intent.getStringExtra(Mean));


            imageView.setImageResource(R.drawable.im_defi);
        }

        if (index == 3) {
            intent.putExtra(Content, "  Example  ");
            tv_topic.setText(intent.getStringExtra(Topic));
//            tv_content.setText(intent.getStringExtra(Content));
            tv_save.setTextSize(20);
            tv_save.setText(intent.getStringExtra(Example));

            imageView.setImageResource(R.drawable.im_example);
        }

        playVocabText();

    }

    public void btn_onClick_Next(View view) {
        String topic = tv_topic.getText().toString();

        pos_current++;
        if (pos_current > end) {
            pos_current = end;

            btnFinish.setVisibility(View.VISIBLE);
//
//            btnNext.setText("Finish");
//            btnNext.setTextColor(Color.parseColor("#ffffff"));
//            btnNext.setBackgroundResource(R.drawable.border);
//            btnNext.setBackgroundColor(Color.parseColor("#006fff"));
//
//            btnNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(DetailVocab.this, Topic_Vocab.class);
//                    startActivity(i);
//                }
//            });
        }
        else {
//            btnFinish.setVisibility(View.INVISIBLE);
            while (arrVocab.get(pos_current).getTopic().compareTo(topic) != 0 && pos_current <= end) {
                pos_current++;
            }
        }
        index = 1;
        showDetail(pos_current, index);
    }

    public void btn_onClick_Pre(View view) {
        String topic = tv_topic.getText().toString();

        pos_current--;
        if (pos_current < start) pos_current=start;
        else {
            while (arrVocab.get(pos_current).getTopic().compareTo(topic) != 0 && pos_current >= start) {
                pos_current = pos_current - 1;
            }
        }
        index = 1;
        showDetail(pos_current, index);
    }

//    public void btn_KnowWord(View view) {
//        String topic = tv_topic.getText().toString();
//
//        pos_current++;
//        if (pos_current > end) {
////            pos_current = end;
////
////            btnNext.setText("Finish");
////            btnNext.setTextColor(Color.parseColor("#ffffff"));
////            btnNext.setBackgroundResource(R.drawable.border);
////            btnNext.setBackgroundColor(Color.parseColor("#006fff"));
////
////            btnNext.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Intent i = new Intent(DetailVocab.this, Topic_Vocab.class);
////                    startActivity(i);
////                }
////            });
//            pos_current = end;
//
//            btnFinish.setVisibility(View.VISIBLE);
//        }
//        else {
//            while (arrVocab.get(pos_current).getTopic().compareTo(topic) != 0 && pos_current <= end) {
//                pos_current++;
//            }
//        }
//        index = 1;
//        showDetail(pos_current, index);
//    }

    private int getEnd(String topic, ArrayList<Vocabulary> v)
    {
        int c = 0;
        for (int i = 0;i<v.size();i++)
        {
            if (topic.compareTo(v.get(i).getTopic()) == 0) c=i;
        }
        return c;
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}

