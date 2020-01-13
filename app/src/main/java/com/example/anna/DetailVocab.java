package com.example.anna;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class DetailVocab extends AppCompatActivity {
    private TextView tv_save, tv_topic;
    String word;
    private  Button btnNext, btnFinish;
    ImageView imageView;

    ArrayList<Vocabulary> arrVocab = new ArrayList<>();
    ArrayList<Vocabulary> topicVocabs = new ArrayList<Vocabulary>();
    TextToSpeech textToSpeech;
    Vocabulary vocab;
    ProgressBar progressBar;
    String topic;

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

        intent = getIntent();
        pos_current = intent.getIntExtra(Topic_Vocab.Pos,0);

        // get topic name
        topic = Topic_Vocab.topicArrayList.get(pos_current).toString();
        // get all vocabs belong to that topic
        for (int i = 0;i <arrVocab.size(); i++)
        {
            if (arrVocab.get(i).getTopic().equals(topic))
            {
                topicVocabs.add(arrVocab.get(i));
            }
        }

        // set vocab position in topic vocab
        pos_current = 0;

        tv_topic = findViewById(R.id.tv_topic);
//        tv_content = findViewById(R.id.tv_content);
        tv_save = findViewById(R.id.tv_word);

        ln = findViewById(R.id.ln_word);
        imageView = findViewById(R.id.image);
        btnNext = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.vocab_progressBar);
        start = 0;
        end = topicVocabs.size();
        progressBar.setMax(end);
        showDetail(pos_current, index);
        gestureDetector = new GestureDetector(this, new MyGesture());

        ln.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

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
        Log.d("pos", String.valueOf(pos)+","+String.valueOf(end));
        progressBar.setProgress(pos+1);
        vocab = topicVocabs.get(pos);
        intent.putExtra(Topic, topic);

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
        pos_current++;
        if (pos_current >= end) pos_current = end-1;
        showDetail(pos_current, 1);
    }

    public void btn_onClick_Pre(View view) {
        pos_current--;
        if (pos_current < 0) pos_current = 0;
        showDetail(pos_current, index);
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}

