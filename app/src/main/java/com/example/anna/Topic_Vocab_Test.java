package com.example.anna;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;


import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Topic_Vocab_Test extends AppCompatActivity {
    private Topic_Vocab_Test.LoadedVocabTest loaded;
    private ProgressDialog prgDialog;
    ListView lvVocabTest;
    VocabTestAdapter adapter;

    public static ArrayList<Vocabulary_Test> vocabTestArrayList = new ArrayList<>();
    public static ArrayList<String> topicTestArrayList = new ArrayList<>();
    public static final String Pos = "pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic__vocab__test);
        this.prgDialog = new ProgressDialog(this);
        this.loaded = new Topic_Vocab_Test.LoadedVocabTest();
        this.loadVocabTest();
    }@Override
    protected void onPostResume() {
        super.onPostResume();
        IntentFilter filter = new IntentFilter("LOADED_TEST");
        registerReceiver(this.loaded, filter);
    }

    public void loadDataToListView()
    {
        lvVocabTest = findViewById(R.id.lvVocabTest);
        //vocabTestArrayList = initDataInArrayList();

        topicTestArrayList = getTopicTest();
        adapter = new VocabTestAdapter(this, R.layout.vocab_test_item, topicTestArrayList);

        lvVocabTest.setAdapter(adapter);
    }

    public ArrayList<String> getTopicTest()
    {
        ArrayList<String> topic = new ArrayList<>();
        topic.add(vocabTestArrayList.get(0).getTopic());

        for (int i = 1;i<vocabTestArrayList.size();i++) {
            if (!topic.contains(vocabTestArrayList.get(i).getTopic())) topic.add(vocabTestArrayList.get(i).getTopic());
        }
        return topic;
    }

    public void contact()
    {
        lvVocabTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Topic_Vocab_Test.this, DetailVocabTest.class);
                i.putExtra(Pos, position);
                startActivity(i);
            }
        });
    }


    private void loading(){
        prgDialog.setMessage("Loading vocabulary test");
        prgDialog.show();
    }

    private void loadVocabTest(){
        this.loading();
        Intent intent = new Intent(this, VocabTestService.class);
        startService(intent);
    }

    private class LoadedVocabTest extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receive", "nhan dc data r neeeeeee");
            loadDataToListView();
            contact();
            prgDialog.dismiss();
        }
    }
}
