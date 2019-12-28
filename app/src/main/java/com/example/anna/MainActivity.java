package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static Button btn_NewMember, btn_OldMember, btn_Skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 

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
                startActivity(intent);
            }
        });
    }



}
