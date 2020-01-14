package com.example.anna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;atus

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText mailText;
    private EditText nameText;
    private EditText ageText;
    private EditText describeText;
    private Spinner sexSpinner;
    private FirebaseFirestore db;
    private String eMail;
    private String idStr;
    //khai bao
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mailText = (EditText) findViewById(R.id.displayEmail);
        nameText = (EditText) findViewById(R.id.editName);
        ageText = (EditText) findViewById(R.id.editAge);
        describeText = (EditText) findViewById(R.id.editDescription);
        sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
        Intent i = getIntent();

//code trong onCreate
        mAuth = FirebaseAuth.getInstance();

        eMail = i.getStringExtra("Email");
        db = FirebaseFirestore.getInstance();
        db.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("PROFILE GET", "doc email: " + document.get("email") +
                                        " my em:" + eMail);
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
                                Log.i("PROFILE TEST", "docId: " + document.getId() +
                                        " idStr:" + idStr);
                                if (document.getId().compareTo(idStr) == 0){
                                    mailText.setText(document.get("email").toString());
                                    nameText.setText(document.get("name").toString());
                                    sexSpinner.setSelection(Integer.parseInt(document.get("sex").toString()));
                                    ageText.setText(document.get("age").toString());
                                    describeText.setText(document.get("description").toString());
                                }
                            }
                        } else {
                            Log.i("abc", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(ProfileActivity.this,StartLearn.class);
        i.putExtra("Email",eMail);
        startActivity(i);
    }

    //TODO - update users data from input field
    public void OnUpdateProfileClicked(View v){
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(idStr).delete();

        Map<String, Object> docData = new HashMap<>();
        docData.put("name", nameText.getText().toString());
        docData.put("sex", sexSpinner.getSelectedItemPosition());
        docData.put("age", Integer.parseInt(ageText.getText().toString()));
        docData.put("description", describeText.getText().toString());
        docData.put("email", eMail);
        docData.put("type", 1);
        docData.put("time", "1");
        db.collection("Users")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fail", "Error adding document");
                    }
                });
    }

    public void OnGoPremium(View v){

    }

    public void OnLogOutClick(View v){
        SharedPreferences sp= getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("Uid","");
        Ed.putString("Email","");
        Ed.putString("Password","");
        Ed.commit();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }


    public void OnDelAcc(View v){
        SharedPreferences sp= getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("Uid","");
        Ed.putString("Email","");
        Ed.putString("Password","");
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(idStr).delete();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DELETE INFO","OK! DELETE fine!");
                    Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
