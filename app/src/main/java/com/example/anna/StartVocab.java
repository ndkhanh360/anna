package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class StartVocab extends AppCompatActivity {

    Button btn_flashcard, btn_fillin, btn_listening, btn_mtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vocab);

        btn_flashcard =findViewById(R.id.btnFlashcard);
        btn_fillin = findViewById(R.id.btnFillIn);
        btn_mtp = findViewById(R.id.btnMultiple);
        btn_listening = findViewById(R.id.btnListening);

        btn_flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(StartVocab.this, Topic_Vocab.class);
                startActivity(i);
            }
        });

        btn_fillin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(StartVocab.this, Topic_Vocab_Test.class);
                startActivity(i);
            }
        });

        btn_mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(StartVocab.this, Topic_Vocab.class);
                i.putExtra("test", 1);
                i.putExtra("mtp", 1);
                startActivity(i);
            }
        });

        btn_listening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(StartVocab.this, Topic_Vocab.class);
                i.putExtra("test", 1);
                i.putExtra("listening", 1);
                startActivity(i);
            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Intent i = new Intent(StartVocab.this, StartLearn.class);
//        startActivity(i);
//        return super.onKeyDown(keyCode, event);
//    }
}
