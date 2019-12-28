package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartPronun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_pronun);
    }

    public void btn_onClick_YouGlish(View view)
    {
        Intent i = new Intent(StartPronun.this, YouGlish.class);
        startActivity(i);
    }
}
