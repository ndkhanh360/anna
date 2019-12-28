package com.example.anna;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VocabTestAdapter extends ArrayAdapter<String> {
    private  final Context context;
    private  int resource;
    private ArrayList<String> arrVocabTest;

    public VocabTestAdapter(Context context, int res, ArrayList<String> object)
    {
        super(context, 0, object);
        this.context = context;
        this.resource = res;
        this.arrVocabTest = object;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView ==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.vocab_test_item, parent, false);
        }

        TextView tv_topic = convertView.findViewById(R.id.tp_test);

        String vocabTest = arrVocabTest.get(position);

       /* if (position == 0) tv_topic.setText(vocabTest.getTopic());
        else
        {
            int pre = 0;
            while (pre < position)
            {
                Vocabulary_Test pre_vocab = arrVocabTest.get(pre);
                if (pre_vocab.getTopic() == vocabTest.getTopic()) break;
                else
                {
                    if (pre == position - 1) tv_topic.setText(vocabTest.getTopic());
                }
                pre++;
            }
        }*/

        tv_topic.setText(vocabTest);

        return convertView;

    }
}
