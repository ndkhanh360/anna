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

public class ListViewAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<String> commentLisst;
    public ListViewAdapter(@NonNull Context context, int resource,ArrayList<String> commentList) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.commentLisst =commentList ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        TextView txtComment = convertView.findViewById(R.id.comment);
        txtComment.setText(commentLisst.get(position));
        return convertView;
    }
    @Override
    public int getCount() {
        return commentLisst.size();}
}

