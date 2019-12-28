package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Topic_Vocab extends AppCompatActivity {
    private LoadedVocab loaded;
    private ProgressDialog prgDialog;
    ListView lvVocab;
    VocabAdapter adapter;

    public static ArrayList<Vocabulary> vocabArrayList = new ArrayList<>();
    public static ArrayList<String> topicArrayList = new ArrayList<>();
    public static final String Pos = "pos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic__vocab);
        this.prgDialog = new ProgressDialog(this);
        this.loaded = new LoadedVocab();
        this.loadVocab();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        IntentFilter filter = new IntentFilter("LOADED");
        registerReceiver(this.loaded, filter);
    }
    public void loadDataToListView()
    {
        lvVocab = findViewById(R.id.lvVocab);
        //vocabArrayList = initDataInArrayList();
        topicArrayList = getTopic();
        adapter = new VocabAdapter(this, R.layout.vocab_item, topicArrayList);

        lvVocab.setAdapter(adapter);


    }

    public ArrayList<Vocabulary>initDataInArrayList()
    {
        ArrayList<Vocabulary> vocab = new ArrayList<>();
        vocab.add(new Vocabulary("School", "Projector", "a piece of equipment that makes a film or picture appear on a screen or flat surface", "dbs"));
        vocab.add(new Vocabulary("School", "Ruler", "fcds", "dbs"));
        vocab.add(new Vocabulary("School", "Diploma", "bang cap", "dbasacds"));
        vocab.add(new Vocabulary("School", "Assignment", "bai tap", "dbds"));
        vocab.add(new Vocabulary("Restaurant", "Service", "fs", "dbs"));
        vocab.add(new Vocabulary("Restaurant", "Waiter", "fs", "dbs"));
        return vocab;
    }

    public ArrayList<String> getTopic()
    {
        ArrayList<String> topic = new ArrayList<>();
        topic.add(vocabArrayList.get(0).getTopic());

        for (int i = 1;i<vocabArrayList.size();i++) {
            if (!topic.contains(vocabArrayList.get(i).getTopic())) topic.add(vocabArrayList.get(i).getTopic());
        }
        return topic;
    }

    public void contact()
    {
        lvVocab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Topic_Vocab.this, DetailVocab.class);
                i.putExtra(Pos, position);
                startActivity(i);
            }
        });
    }

    private void loading(){
        prgDialog.setMessage("Loading vocabulary");
        prgDialog.show();
    }

    private void loadVocab(){
        this.loading();
        Intent intent = new Intent(this, VocabService.class);
        Log.d("start", "start r neeeeeeeeeeeeee");
        startService(intent);
    }

    private class LoadedVocab extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receive", "nhan dc data r neeeeeee");
            loadDataToListView();
            contact();
            prgDialog.dismiss();
        }
    }
}
