package com.example.anna;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    String EmailHolder;
    TextView Email;
    Button LogOUT ;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    User user=null;
    //@SuppressLint("SetTextI18n")
    public static final String TAG="Dash";
    ArrayList<String> topicArray;
    private final ArrayList<Integer> choosedTopic = new ArrayList<Integer>();
    private ListView listView;

    void getData(String uid)
    {
        String dishplayName = null;
        FirebaseFirestore.getInstance().collection("Users").
                document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user1=documentSnapshot.toObject(User.class);
                user=user1;
                user.getInstance().setData(user.displayname,user.type,user.email);
                Email.setText(Email.getText().toString()+user.getDisplayname());
            }
                });
    }
    private boolean SendTopic(){
        if (choosedTopic.size() == 0){
            Toast.makeText(DashboardActivity.this, "Choose a topic", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void GotoSingle(View v){
        if (!SendTopic()){
            return;
        }
        Intent intent = new Intent(DashboardActivity.this, FullscreenActivity.class);
        // Sending Email to Dashboard Activity using intent.
        intent.putExtra("videoType",1);
        intent.putExtra("topic",topicArray.get(choosedTopic.get(0)));
        startActivity(intent);
    }


    public void GotoDouble(View v){
        if (!SendTopic()){
            return;
        }
        Intent intent = new Intent(DashboardActivity.this, FullscreenActivity.class);
        // Sending Email to Dashboard Activity using intent.
        intent.putExtra("videoType",2);
        intent.putExtra("topic",topicArray.get(choosedTopic.get(0)));
        startActivity(intent);
    }

    public void GotoInstructor(View v){
        if (!SendTopic()){
            return;
        }
        Intent intent = new Intent(DashboardActivity.this, FullscreenActivity.class);
        // Sending Email to Dashboard Activity using intent.
        intent.putExtra("videoType",3);
        intent.putExtra("userType",user.type);
        startActivity(intent);
    }

    void getSpeakingTopics(){
        topicArray.add("Student life");
        topicArray.add("Travel");
        topicArray.add("Health");
        topicArray.add("Lifestyles");
        topicArray.add("Urbanisation");
//        topicArray.add("The law");
//        topicArray.add("The green revolution");
//        topicArray.add("Keeping fit");
//        topicArray.add("Through the ages");
    }

    private void showTopics(){
        TopicListViewAdapter listAdapter=new TopicListViewAdapter(this.getBaseContext(),R.layout.speaking_topic_item,(ArrayList<String>) topicArray);
        listView.setClickable(true);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.i("INFO","View item: ");
                for (int i = 0; i < choosedTopic.size(); i++){
                    if (choosedTopic.get(i) == position){
                        choosedTopic.remove(i);
                        ((CheckBox)((ViewGroup)view).getChildAt(0)).setChecked(false);
                        return;
                    }
                }
                choosedTopic.add(position);
                ((CheckBox)((ViewGroup)view).getChildAt(0)).setChecked(true);
            }
        });

    }

    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        listView = (ListView) findViewById(R.id.listview_tp);
        topicArray = new ArrayList<String>();
        //choosedTopic = new ArrayList<Integer>();
        Log.i("Dashboard"," get intent: ");
        getSpeakingTopics();
        Log.i("Dashboard"," get topic: ");
        showTopics();
        Log.i("Dashboard"," show topic: ");
//        LogOUT = (Button) findViewById(R.id.logout_button);
        Intent intent = getIntent();

        Log.i("Dashboard"," get intent: ");
        // Receiving User Email Send By MainActivity.
        String Uid=intent.getStringExtra("Uid");
        Log.i("Dashboard","Uid: "+ Uid);
//        getData(Uid);
        // Setting up received email to TextView.
        // Adding click listener to Log Out button.
//        LogOUT.setOnClickListener(new View.OnClickListener() {
//            // @Override
//            public void onClick(View v) {
//
//
//                //Finishing current DashBoard activity on button click.
//                finish();
//
//                Toast.makeText(DashboardActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();
//                //Intent intent=new Intent(DashboardActivity.this,login.class);
//                //startActivity(intent);
//               /*if (v.getId() == R.id.button1) {
//                    AuthUI.getInstance()
//                            .signOut(this)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    // user is now signed out
//                                    startActivity(new Intent(DashboardActivity.this, login.class));
//                                    finish();
//                                }
//                            });
//                }*/
//
//            }
//        });


    }
}
