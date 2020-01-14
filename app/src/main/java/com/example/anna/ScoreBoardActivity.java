package com.example.anna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScoreBoardActivity extends AppCompatActivity {

    public class Comment{
        public String eFrom;
        public String eTo;
        public int Score;
        public String Comment;

        public Comment() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }
    }

    private int score;
    private ArrayList<String> comments;
    private ListView listView;
    private TextView textView;
    ProgressDialog progressDialog;
    private EditText editComment;
    private EditText editScore;
    private int videoType;
    private FirebaseFirestore db;
    private String cId;
    private ArrayList<String> ids;
    private String eMail;
    private DatabaseReference mDatabase;
    private DatabaseReference mCommentRef;

    private  boolean skippable;

    private String myScore;
    private String myComment;
    @Override
    public void onBackPressed() {
        ids = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Speakings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ids.add(document.getId());
                            }
                        } else {
                            Log.i("abc", "Error getting documents.", task.getException());
                        }
                    }
                });
        for(String idStr : ids){
            // asynchronously delete a document
            db.collection("Speakings").document(idStr).delete();
        }
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onDonePressed(View v){
        onBackPressed();
    }

    public void OnSendComment(@Nullable View v){
        if (videoType == 1){


        }

        if (eMail == null || eMail.isEmpty()){
            Toast.makeText(this, "You must log in to do this", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        //asynchronously update doc, create the document if missing
        if (editScore.getText().toString().isEmpty()){
            Toast.makeText(this, "SCORE YOUR PARTNER", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> update = new HashMap<>();
        update.put("from", eMail);
        update.put("to", "minhthuy99clber@gmail.com");
        update.put("cId", cId);
        update.put("comment", editComment.getText().toString());
        update.put("score", Integer.parseInt(editScore.getText().toString()));
        db = FirebaseFirestore.getInstance();
        db.collection("Speakings").add(update).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                cId = documentReference.getId();
                Log.i("DB INFO", "DocumentSnapshot added with ID: " + documentReference.getId());
                Log.d("DB INFO", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DB ERROR", "Error adding document", e);
                Log.d("DB ERROR", "Error adding document");
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful())
                {

                }
            }
        });


        ShowScoreScreen();
    }

    private void ShowScoreScreen() {
        setContentView(R.layout.activity_score_board);
        textView = (TextView) findViewById(R.id.scoreview);
        listView = (ListView) findViewById(R.id.listcommentview);
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCommentRef = mDatabase.getRef().child("Speakings");
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        eMail = sp.getString("Email", null);
        if (videoType == 2) {
            setContentView(R.layout.activity_speaking_comment);
            editComment = (EditText) findViewById(R.id.editComment);
            editScore = (EditText) findViewById(R.id.editScore);
        } else {
            int score= intent.getIntExtra("Score",0);
            skippable = intent.getBooleanExtra("Skippable",true);
            if (skippable){
                onBackPressed();
            }
            ShowScoreScreen();
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
        if (videoType == 1)
            return;

        score = -1;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting for your partner ... ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new CountDownTimer(281020, 500) {
            public void onTick(long remain) {
                db = FirebaseFirestore.getInstance();
                db.collection("Speakings").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document == null)
                                            return;
                                        if (document.getId() == null)
                                            return;
                                        if (document.getId().compareTo(cId) == 0)
                                            continue;
                                        if (document.getData() == null)
                                            return;
                                        if (document.getData().get("score") == null)
                                            return;
                                        Log.i("CID", "cId: " + cId.valueOf(score));
                                        Log.d("CID", "cId: " + cId.valueOf(score));
                                        score = Integer.parseInt(document.getData().get("score").toString());
                                        Log.i("SCORE", "Score: " + String.valueOf(score));
                                        Log.d("SCORE", "Score: " + String.valueOf(score));
                                        progressDialog.dismiss();
                                        getComments();
                                    }
                                } else {
                                    Log.i("abc", "Error getting documents.", task.getException());
                                }
                            }
                        });

            }
            public void onFinish() {
                if (score == -1){
                    videoType = 1;
                    getComments();
                }
                progressDialog.dismiss();
            }
        }.start();

        return;
        // Request a string response from the provided URL.
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
            return;
        }

        db = FirebaseFirestore.getInstance();
        db.collection("Speakings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().compareTo(cId) == 0)
                                    continue;
                                comments.add(document.getData().get("comment").toString());
                                showData();
                            }
                        } else {
                            Log.i("abc", "Error getting documents.", task.getException());
                        }
                    }
                });
        return;
    }
}