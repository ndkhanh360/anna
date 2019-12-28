package com.example.anna;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TopicListViewAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<String> topicList;

    public TopicListViewAdapter(@NonNull Context context, int resource, ArrayList<String> commentLisst) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.topicList =commentLisst ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        TextView topicTit = convertView.findViewById(R.id.topicTitle);
        topicTit.setText(topicList.get(position));
        return convertView;
    }
    @Override
    public int getCount() {
        return topicList.size();}
}
