package com.example.multinotes;

import android.provider.CalendarContract;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView content;
    public TextView timestamp;

    public ViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.noteTitle);
        content = view.findViewById(R.id.noteBrief);
        timestamp = view.findViewById(R.id.noteTimestamp);
    }
}
