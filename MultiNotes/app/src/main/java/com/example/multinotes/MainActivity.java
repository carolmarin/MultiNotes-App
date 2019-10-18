package com.example.multinotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "Main Activity";

    private List<Notes> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private static final int NEW_NOTE = 1;
    private static final int EDIT_NOTE = 2;
    private static final int SAVED_NOTE = 3;
    private static final int NO_EDIT = 4;

    private Notes n;
    private int position;

    private Notes editedNote;
    private int enPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Async(this).execute("Notes.json");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public void onPause() {
        List();
        super.onPause();
    }

    public void openAboutActivity(MenuItem item) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void openEditActivity(MenuItem item) {
        Intent intent = new Intent(this, EditActivity.class);
        Log.d(TAG, "openEditActivity: ");
        startActivityForResult(intent, NEW_NOTE);
    }

    @Override
    public void onClick(View v) {
        enPos = recyclerView.getChildLayoutPosition(v);
        editedNote = notesList.get(enPos);
        Intent edit = new Intent(MainActivity.this, EditActivity.class);
        edit.putExtra("EDIT_NOTE", editedNote);
        Log.d(TAG, "onClick: put old tate in new intent:"+editedNote.toString());
        startActivityForResult(edit, EDIT_NOTE);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        Notes n = notesList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this note?");
        setTitle("Delete Note");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notesList.remove(pos);
                notesAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note Deleted", LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "No Changes Made", LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onActivityResult(int code, int result, Intent data) {
        Log.d(TAG, "onActivityResult: code, res"+code+","+result);
        if (code == NEW_NOTE) {
            if (result == SAVED_NOTE) {
                Notes newNote = (Notes) data.getSerializableExtra("SAVED_NOTE");
                notesList.add(0, newNote);
                Log.d(TAG, "onActivityResult: saved new note");
                notesAdapter.notifyDataSetChanged();
                Toast.makeText(this,"Note Saved", LENGTH_SHORT).show();
            }
        } else if (code == EDIT_NOTE) {
            if (result == SAVED_NOTE) {
                editedNote = (Notes) data.getSerializableExtra("SAVED_NOTE");
                Log.d(TAG, "onActivityResult: saved note");
                notesList.remove(enPos);
                notesList.add(0, editedNote);
                notesAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Saved", LENGTH_SHORT).show();
            } else if (result == NO_EDIT) {
                Toast.makeText(this, "No Changes Made", LENGTH_SHORT).show();
            }
        }
        notesAdapter.notifyDataSetChanged();
        Log.d(TAG, "onActivityResult: completed " + code);
    }

    private void List() {
        Log.d(TAG, "List: Loading All Notes");
        try {
            FileOutputStream out = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, getString(R.string.encoding)));
            writer.setIndent(" ");
            writer.beginArray();
            for (Notes n: notesList) {
                writer.beginObject();
                writer.name("Title").value(n.getTitle());
                writer.name("Content").value(n.getContent());
                writer.name("Timestamp").value(n.getTime());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void doAsync(List<Notes> n) {
        Log.d(TAG, "doAsync: ");
        notesList = n;

        if (notesList != null) {
            recyclerView = findViewById(R.id.recycler);
            notesAdapter = new NotesAdapter(notesList, this);
            recyclerView.setAdapter(notesAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            notesAdapter.notifyDataSetChanged();
            Log.d(TAG, "doAsync: data changed");
        }
    }
}
