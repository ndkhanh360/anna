package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartVocab extends AppCompatActivity {

    Button btn_study, btn_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vocab);

        btn_study=findViewById(R.id.btnStudy);
        btn_test= findViewById(R.id.btnTest);

        btn_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(StartVocab.this, Topic_Vocab.class);
                startActivity(i);
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(StartVocab.this, Topic_Vocab_Test.class);
                startActivity(i);
            }
        });
    }
}
