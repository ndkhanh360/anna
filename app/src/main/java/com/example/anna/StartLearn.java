package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StartLearn extends AppCompatActivity {

    Button btnVocab, btnSpeaking, btnPronun;
    ImageView avatar;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_learn);

        btnVocab = findViewById(R.id.btnVoca);
        btnSpeaking = findViewById(R.id.btnSpeaking);
        btnPronun = findViewById(R.id.btnPronun);

        avatar= findViewById(R.id.imAvatar);
        tvTitle=findViewById(R.id.tvTitle);

        //avatar.setImageResource(getAvatarFromUser());

        tvTitle.setText("Learn & Practice");
        
        btnVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartLearn.this, StartVocab.class);
                startActivity(i);
            }
        });

        btnPronun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartLearn.this, StartPronun.class);
                startActivity(i);
            }
        });
        btnSpeaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartLearn.this, DashboardActivity.class);
                i.putExtra("Uid","123");
                startActivity(i);
            }
        });

    }

    public void btn_onClick_Speaking(View view)
    {
        Intent i = new Intent(StartLearn.this, DashboardActivity.class);
        i.putExtra("Uid","123");
        startActivity(i);
    }

}
