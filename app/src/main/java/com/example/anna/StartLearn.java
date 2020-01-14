package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartLearn extends AppCompatActivity {

    Button btnVocab, btnSpeaking;
    TextView tvTitle;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_learn);

        btnVocab = findViewById(R.id.btnVoca);
        btnSpeaking = findViewById(R.id.btnSpeaking);

        LinearLayout layout = findViewById(R.id.profile_layout);

        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        if (email != null && email.length() > 0) {
            layout.setVisibility(View.VISIBLE);
        }

        btnVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartLearn.this, StartVocab.class);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Intent i = new Intent(StartLearn.this, MainActivity.class);
//        startActivity(i);
//        return super.onKeyDown(keyCode, event);
//    }

    public void btn_onClick_profile(View view) {
        Intent i = new Intent(StartLearn.this, ProfileActivity.class);
        i.putExtra("Email", email);
        startActivity(i);
    }
}
