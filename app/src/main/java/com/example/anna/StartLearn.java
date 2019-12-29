package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartLearn extends AppCompatActivity {

    Button btnVocab, btnSpeaking, btnPronun;
    ImageView avatar;
    TextView tvTitle;
    private String eMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_learn);

        btnVocab = findViewById(R.id.btnVoca);
        btnSpeaking = findViewById(R.id.btnSpeaking);
        btnPronun = findViewById(R.id.btnPronun);

        avatar= findViewById(R.id.imAvatar);
        tvTitle=findViewById(R.id.tvTitle);

        Intent intent = getIntent();
        int  show_profile = intent.getIntExtra("show_profile", 1);
        eMail = intent.getStringExtra("Email");
        if (show_profile == 1) {
            Log.i("Extra", "1");
            ImageView iv = findViewById(R.id.imAvatar);
            TextView tv = findViewById(R.id.tvProfile);
            iv.setVisibility(View.VISIBLE);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_onClick_Profile(v);
                }
            });
            tv.setVisibility(View.VISIBLE);
        }
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

    public void btn_onClick_Profile(View view)
    {
        if ( eMail.isEmpty()){
            Toast.makeText(StartLearn.this, "You haven't login", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(StartLearn.this, ProfileActivity.class);
        i.putExtra("Email",eMail);
        startActivity(i);
    }

}
