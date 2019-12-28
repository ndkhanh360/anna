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

public class VocabAdapter extends ArrayAdapter<String> {
    private  final Context context;
    private  int resource;
    private ArrayList<String> arrVocab;

    public VocabAdapter(Context context, int res, ArrayList<String> object)
    {
        super(context, 0, object);
        this.context = context;
        this.resource = res;
        this.arrVocab = object;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView ==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.vocab_item, parent, false);
        }

        TextView tv_topic = convertView.findViewById(R.id.tp);

        String vocab = arrVocab.get(position);

        /*if (position == 0) tv_topic.setText(vocab.getTopic());
        else
        {
            int pre = 0;
            while (pre < position)
            {
                Vocabulary pre_vocab = arrVocab.get(pre);
                if (pre_vocab.getTopic() == vocab.getTopic()) break;
                else
                {
                    if (pre == position - 1) tv_topic.setText(vocab.getTopic());
                }
                pre++;
            }
        }*/

        tv_topic.setText(vocab);


        return convertView;

    }
}
