package com.example.anna;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.content.Context;
import android.net.NetworkInfo;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VocabTestService extends Service {
    FirebaseFirestore db;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ArrayList<Vocabulary_Test> vocab_test = new ArrayList<>();
        if (isNetworkAvailable()){
            db = FirebaseFirestore.getInstance();
            db.collection("TestWords").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    vocab_test.add(new Vocabulary_Test(document.getData().get("t").toString(), document.getData().get("q").toString(), document.getData().get("a").toString()));
                                }
                                Topic_Vocab_Test.vocabTestArrayList = vocab_test;
                                Log.d("vocab", Topic_Vocab_Test.vocabTestArrayList.get(0).toString());
                                sendFinish();
                            } else {
                                Log.w("abc", "Error getting documents.", task.getException());
                                sendFinish();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            sendFinish();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    private void sendFinish() {
        Intent intent = new Intent("LOADED_TEST");
        sendBroadcast(intent);
        Log.d("send", "send roi neeeeeee");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}