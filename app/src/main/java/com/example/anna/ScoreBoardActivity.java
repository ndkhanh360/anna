package com.example.anna;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Random;

public class ScoreBoardActivity extends AppCompatActivity {

    private int score;
    private ArrayList<String> comments;
    private ListView listView;
    private TextView textView;
    private int videoType;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onDonePressed(View v){
        onBackPressed();
    }

    public void OnSendComment(@Nullable View v){
        setContentView(R.layout.activity_score_board);
        textView = findViewById(R.id.scoreview);
        listView = findViewById(R.id.listcommentview);
        getScore();
        getComments();
        showData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        comments = new ArrayList<String>();
        videoType = intent.getIntExtra("videoType",1);
        int score= intent.getIntExtra("Score",0);
        if (videoType == 2) {
            setContentView(R.layout.activity_speaking_comment);
        } else {
            OnSendComment(null);
        }
//        int commentCount=intent.getIntExtra("CommentCount",0);
//
//        textView.setText(textView.getText()+String.valueOf(score));
//        ArrayList<String> commentList = new ArrayList<String>();
//        for(int i=0;i<commentCount;i++)
//        {
//            String item=intent.getStringExtra("Comment_"+String.valueOf(i));
//            commentList.add(item);
//        }
    }

    private void showData(){
        textView.setText(textView.getText()+ String.valueOf(score));
        CommentListViewAdapter listAdapter=new CommentListViewAdapter(this.getBaseContext(),R.layout.speaking_comment_item,(ArrayList<String>) comments);
        listView.setAdapter(listAdapter);
    }

    private void getScore(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://devcchotot.herokuapp.com/user/getScore";
        score = (new Random()).nextInt(3) + 4;
        return;
// Request answer1 string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        score = Integer.parseInt(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("ERROR", "no score :"+error.toString());
//                System.out.print(error.getStackTrace());
//
//            }
//        });
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
    }
    private void getComments(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://devcchotot.herokuapp.com/user/getComment";
        if (videoType == 1) {
            comments.add("Need more complex words");
            comments.add("Use some idoms or phrase of verbs");
            comments.add("Be careful with your grammar");
        }else{
            comments.add("Need more complex words");
        }
        return;
//// Request answer1 string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        comments.clear();
//                        comments.add(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("ERROR", "no comment :"+error.toString());
//                System.out.print(error.getStackTrace());
//
//            }
//        });
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
    }
}
