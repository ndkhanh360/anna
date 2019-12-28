//package com.example.anna;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCanceledListener;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreSettings;
//import com.google.firebase.firestore.SetOptions;
//
//import java.util.Date;
//
//public class Register extends AppCompatActivity implements View.OnClickListener{
//    EditText name,email,password;
//    Button mRegisterbtn;
//    TextView mLoginPageBack;
//    FirebaseAuth mAuth;
//    DatabaseReference mdatabase;
//    String Name,Email,Password;
//    ProgressDialog mDialog;
//    User newUser=null;
//    String Uid="";
//    CheckBox instructorBox;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(newUser!=null){
//            clgt(Uid);}
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        //clgt("abcs");
//        setContentView(R.layout.activity_register);
//        name = (EditText)findViewById(R.id.editName);
//        email = (EditText)findViewById(R.id.editEmail);
//        password = (EditText)findViewById(R.id.editPassword);
//        mRegisterbtn = (Button)findViewById(R.id.buttonRegister);
//        instructorBox = (CheckBox) findViewById(R.id.checkBeginner);
//        // for authentication using FirebaseAuth.
//        mAuth = FirebaseAuth.getInstance();
//        mRegisterbtn.setOnClickListener(this);
//        mDialog = new ProgressDialog(this);
//        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//    }
//
//    @Override
//    public void onClick(View v) {
////        if (v==mRegisterbtn){
//        UserRegister();
////        }else if (v== mLoginPageBack){
////            startActivity(new Intent(Register.this,Login.class));
////        }
//    }
//    public void clgt(String Uid){
//
//        CollectionReference firebaseFirestore=FirebaseFirestore.getInstance().collection("Users");
//        firebaseFirestore.document(Uid).set(newUser);
//    }
//    private void UserRegister() {
//        Name = name.getText().toString().trim();
//        Email = email.getText().toString().trim();
//        Password = password.getText().toString().trim();
//
//        if (TextUtils.isEmpty(Name)){
//            Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
//            return;
//        }else if (TextUtils.isEmpty(Email)){
//            Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
//            return;
//        }else if (TextUtils.isEmpty(Password)){
//            Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
//            return;
//        }else if (Password.length()<6){
//            Toast.makeText(Register.this,"Password must be greater than 6 digit",Toast.LENGTH_SHORT).show();
//            return;
//        }
////        instructorBox = (CheckBox) findViewById(R.id.isInstructor);
//        mDialog.setMessage("Creating User please wait...");
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.show();
//        final boolean success=false;
//        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    sendEmailVerification();
//                    //clgt(mAuth.getUid());
//                    mDialog.dismiss();
//                    //OnAuth(task.getResult().getUser());
//                    mAuth.signOut();
//                    Log.d("error?", "no error?");
//                    Toast.makeText(Register.this,"CREATE SUCCESS",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Register.this,Login.class));
//                }else{
//                    mDialog.dismiss();
//                    Toast.makeText(Register.this,"error on creating user",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                    Log.i("NewUserError", " can't create user exception: " + task.getException());
//                }
//            }
//        });
//
//
//    }
//    //Email verification code using FirebaseUser object and using isSucccessful()function.
////    private void sendEmailVerification() {
////        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////        if (user!=null){
////            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
////                @Override
////                public void onComplete(@NonNull Task<Void> task) {
////                    if (task.isSuccessful()) {
////                        Toast.makeText(Register.this, "CHECK YOUR MAIL BOX", Toast.LENGTH_SHORT).show();
////                        FirebaseAuth.getInstance().signOut();
////                    } else {
////                        Toast.makeText(Register.this, "Unable to send verification mail", Toast.LENGTH_SHORT).show();
////                    }
////                }
////            });
////        }
////    }
//
//    private void sendEmailVerification() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user!=null){
//            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(Register.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
//                        FirebaseAuth.getInstance().signOut();
//                    }
//                }
//            });
//        }
//    }
//
//    private void OnAuth(FirebaseUser user) {
//        createAnewUser(user.getUid());
//    }
//
//    private void createAnewUser(String uid) {
//        com.example.anna.User user = BuildNewuser();
//        mdatabase.child(uid).setValue(user);
//        Uid=uid;
//        //Adding click listener to log in button.
//        //clgt(uid);
//    }
//
//
//    private com.example.anna.User BuildNewuser(){
//        long type = 0;
//        if (instructorBox.isChecked())
//            type = 1;
//        User user = new User(
//                getDisplayName(),
//                getUserEmail(),
//                new Date().getTime(),
//                type
//        );
//        newUser=user;
//        return user;
//    }
//
//    public String getDisplayName() {
//        return Name;
//    }
//
//    public String getUserEmail() {
//        return Email;
//    }
//}

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

import java.util.Date;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(newUser!=null){
            clgt(Uid);}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //clgt("abcs");
        setContentView(R.layout.activity_register);
        name = (EditText)findViewById(R.id.editName);
        email = (EditText)findViewById(R.id.editEmail);
        password = (EditText)findViewById(R.id.editPassword);
        mRegisterbtn = (Button)findViewById(R.id.buttonRegister);
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

        if (TextUtils.isEmpty(Name)){
            Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (Password.length()<6){
            Toast.makeText(Register.this,"Password must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        instructorBox = (CheckBox) findViewById(R.id.checkBeginner);
        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        final boolean success=false;
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("test", task.toString());
                if (task.isSuccessful()){
                    sendEmailVerification();
                    //clgt(mAuth.getUid());
                    mDialog.dismiss();
                    //OnAuth(task.getResult().getUser());
                    mAuth.signOut();
                    Toast.makeText(Register.this,"CHECK YOUR MAILBOX",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this,Login.class));
                }else{
                    Toast.makeText(Register.this,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Register.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        com.example.anna.User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
        Uid=uid;
        //Adding click listener to log in button.
        //clgt(uid);
    }


    private com.example.anna.User BuildNewuser(){
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