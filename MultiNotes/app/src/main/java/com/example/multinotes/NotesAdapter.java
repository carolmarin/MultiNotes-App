package com.example.multinotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "NotesAdapter";
    private List<Notes> notesList;
    private MainActivity mainActivity;

    public NotesAdapter(List<Notes> notesList, MainActivity ma) {
        this.notesList = notesList;
        mainActivity = ma;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW ViewHolder");

        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        View itemView = inflater.inflate(R.layout.recycle_notes, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Notes" + pos);

        Notes notes = notesList.get(pos);

        holder.title.setText(notes.getTitle());
        holder.content.setText(notes.getNotePrev());
        holder.timestamp.setText(notes.getTime());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
