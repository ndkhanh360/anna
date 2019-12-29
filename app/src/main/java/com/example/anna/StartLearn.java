package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private static final int CODE_REQUEST_CAMERA = 2810;
    private static final int CODE_REQUEST_RECORD_AUDIO = 2811;
    private static final int CODE_REQUEST_READ_PHONE_STATE = 2812;


    private void checkAndRequestPermissions() {
        // check and grant permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CODE_REQUEST_CAMERA);
        } else {
            // Permission has already been granted
        }
        // check and grant permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    CODE_REQUEST_RECORD_AUDIO);

        } else {
            // Permission has already been granted
        }


        // check and grant permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.RECORD_AUDIO)) {
//            } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    CODE_REQUEST_READ_PHONE_STATE);

        } else {
            // Permission has already been granted
        }
    }
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
                checkAndRequestPermissions();
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
