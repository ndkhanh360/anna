package com.example.anna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;
import com.google.protobuf.Int32Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScoreBoardActivity extends AppCompatActivity {

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
        //asynchronously update doc, create the document if missing
        Map<String, Object> update = new HashMap<>();
        update.put("from", eMail);
        update.put("to", "minhthuy99clber@gmail.com");
        update.put("cId", cId);
        update.put("comment", editComment.getText());
        update.put("score", Integer.parseInt(editScore.getText().toString()));

        db.collection("cities").add(update).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i("DB INFO", "DocumentSnapshot added with ID: " + documentReference.getId());
                cId = documentReference.getId();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB ERROR", "Error adding document", e);
                    }
                });

        setContentView(R.layout.activity_score_board);
        textView = findViewById(R.id.scoreview);
        listView = findViewById(R.id.listcommentview);
        getScore();
        getComments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        comments = new ArrayList<String>();
        videoType = intent.getIntExtra("videoType",1);
        eMail = intent.getStringExtra("Email");
        if (videoType == 2) {
            setContentView(R.layout.activity_speaking_comment);
            editComment = (EditText) findViewById(R.id.editComment);
            editScore = (EditText) findViewById(R.id.editScore);
        } else {
            int score= intent.getIntExtra("Score",0);
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
        if (videoType == 1)
            return;

        score = -1;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting for your partner ... ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new CountDownTimer(2000, 200) {
            public void onTick(long remain) {
                db = FirebaseFirestore.getInstance();
                db.collection("Speakings").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getId().compareTo(cId) == 0)
                                            continue;
                                        score = Integer.parseInt(document.getData().get("score").toString());
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
