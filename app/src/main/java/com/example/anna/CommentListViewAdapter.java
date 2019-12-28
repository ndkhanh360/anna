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

public class CommentListViewAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<String> commentList;

    public CommentListViewAdapter(@NonNull Context context, int resource, ArrayList<String> commentLisst) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.commentList =commentLisst ;
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
        txtComment.setText(commentList.get(position));
        return convertView;
    }
    @Override
    public int getCount() {
        return commentList.size();}
}
