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

public class VocabService extends Service {
    FirebaseFirestore db;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ArrayList<Vocabulary> vocab = new ArrayList<>();
        if (isNetworkAvailable()){
            db = FirebaseFirestore.getInstance();
            db.collection("Words").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    vocab.add(new Vocabulary(document.getData().get("t").toString(), document.getData().get("w").toString(), document.getData().get("m").toString(), document.getData().get("e").toString()));
                                }
                                Topic_Vocab.vocabArrayList = vocab;
                                Log.d("vocab", Topic_Vocab.vocabArrayList.get(0).toString());
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
        Intent intent = new Intent("LOADED");
        sendBroadcast(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}