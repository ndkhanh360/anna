package com.example.anna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;
import com.example.anna.R;
import com.google.firestore.v1.WriteResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class Register extends AppCompatActivity implements View.OnClickListener{
    EditText name,email,password;
    Button mRegisterbtn;
    TextView mLoginPageBack;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    String Name,Email,Password;
    ProgressDialog mDialog;
    User newUser=null;
    String Uid="";
    CheckBox instructorBox;
    Spinner sexSpinner;
    EditText ageEdit;
    FirebaseFirestore db;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(newUser!=null){
            clgt(Uid);}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Alan");
//        user.put("middle", "Mathison");
//        user.put("last", "Turing");
//        user.put("born", 1912);
//
//// Add a new document with a generated ID
//        db.collection("Users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("test", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("test", "Error adding document");
//                    }
//                });
        super.onCreate(savedInstanceState);
        //clgt("abcs");
        setContentView(R.layout.activity_register);
        name = (EditText)findViewById(R.id.editName);
        email = (EditText)findViewById(R.id.editEmail);
        password = (EditText)findViewById(R.id.editPassword);
        mRegisterbtn = (Button)findViewById(R.id.buttonRegister);
        sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
        ageEdit = (EditText)findViewById(R.id.editAge);
        //instructorBox = (CheckBox)findViewById(R.id.checkBeginner);
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    public void onClick(View v) {
//        if (v==mRegisterbtn){
        UserRegister();
//        }else if (v== mLoginPageBack){
//            startActivity(new Intent(Register.this,Login.class));
//        }
    }
    public void clgt(String Uid){

        CollectionReference firebaseFirestore=FirebaseFirestore.getInstance().collection("Users");
        firebaseFirestore.document(Uid).set(newUser);
    }
    private void UserRegister() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Email)){
            Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Name)){
            Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (Password.length()<6){
            Toast.makeText(Register.this,"Password must have at least 6 characters",Toast.LENGTH_SHORT).show();
            return;
        }
        instructorBox = (CheckBox) findViewById(R.id.checkBeginner);
        mDialog.setMessage("Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        final boolean success=false;
        Log.i("test", "creating user ... " );
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("test", task.toString());
                if (task.isSuccessful()){
                    sendEmailVerification();
                    //clgt(mAuth.getUid());
                    mDialog.dismiss();
                    //mAuth.signOut();
                }else{
                    mDialog.dismiss();

                    Toast.makeText(Register.this,"Failed to sign up!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i("test", "mAuth sent " );

    }
    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        Log.i("test", "sendEmailVerification ... " );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("test", "sendEmailVerification ... COMPLETED " );
                    if (task.isSuccessful()) {
                        OnAuth();
                    } else {
                        Toast.makeText(Register.this, "Unable to send verification mail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void OnAuth() {
        Log.i("INFO","OnAuth called");
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", getDisplayName());
        docData.put("sex", sexSpinner.getPrompt());
        docData.put("age", Integer.parseInt(ageEdit.getText().toString()));
        docData.put("email", getUserEmail());;
        docData.put("type", instructorBox.isChecked());
        docData.put("time", "1");
        db.collection("Users")
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mAuth.signOut();
                        Toast.makeText(Register.this,"Check your email for verification",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,Login.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fail", "Error adding document");
                    }
                });
    }

    private void createAnewUser(String uid) {
        com.example.anna.User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
        Uid=uid;
        //Adding click listener to log in button.
        //clgt(uid);
    }


    private User BuildNewuser(){
        long type = 0;
        if (instructorBox.isChecked())
            type = 1;
        User user = new User(
                getDisplayName(),
                getUserEmail(),
                new Date().getTime(),
                type
        );
        newUser=user;
        return user;
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }
}