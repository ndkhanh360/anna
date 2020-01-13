package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static Button btn_NewMember, btn_OldMember;
    public static LinearLayout btn_Skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);

        String uid = sp.getString("Uid", null);
        String email = sp.getString("Email", null);
        String pass = sp.getString("Password", null);

        // If user is logged in
        if (email != null)
        {
            Intent intent = new Intent(MainActivity.this, StartLearn.class);
            intent.putExtra("Email",email);
            // Sending Email to Dashboard Activity using intent.
            intent.putExtra("Uid",uid);
            startActivity(intent);
        }

        btn_NewMember = findViewById(R.id.buttonNew);
        btn_OldMember = findViewById(R.id.buttonOld);
        btn_Skip = findViewById(R.id.buttonSkip);

        btn_NewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class );
                startActivity(intent);
            }
        });

        btn_OldMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class );
                startActivity(intent);
            }
        });

        btn_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartLearn.class );
                intent.putExtra("show_profile", 0);
                startActivity(intent);
            }
        });
    }



}
