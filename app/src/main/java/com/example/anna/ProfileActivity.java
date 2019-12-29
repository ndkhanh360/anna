package com.example.anna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private EditText mailText;
    private EditText nameText;
    private EditText ageText;
    private Spinner sexSpinner;
    private FirebaseFirestore db;
    private String eMail;
    private String idStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mailText = (EditText) findViewById(R.id.displayEmail);
        nameText = (EditText) findViewById(R.id.editName);
        ageText = (EditText) findViewById(R.id.editAge);
        sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
        Intent i = getIntent();
        eMail = i.getStringExtra("Email");
        db = FirebaseFirestore.getInstance();
        db.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("email").toString().compareTo(eMail) == 0){
                                    idStr = document.getId();
                                    break;
                                }
                            }
                            GetProfileInfo();
                        } else {
                            Log.i("abc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //TODO - get user's data and display to screen
    private void GetProfileInfo(){
        db = FirebaseFirestore.getInstance();
        db.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().compareTo(idStr) == 0){
                                    mailText.setText(document.get("email").toString());
                                    nameText.setText(document.get("name").toString());
                                    sexSpinner.setPrompt(document.get("sex").toString());
                                    ageText.setText(String.valueOf(document.get("age").toString()));
                                }
                            }
                        } else {
                            Log.i("abc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //TODO - update users data from input field
    public void OnUpdateProfileClicked(View v){
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(idStr).delete();
    }

    public void OnGoPremium(View v){

    }
}
