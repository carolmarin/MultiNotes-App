package com.example.multinotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private EditText TitleNote;
    private EditText DescNote;
    private Notes n;
    private Button buttonSave;

    private static final int SAVED_NOTE = 3;
    private static final int NO_EDIT = 4;
    private static final int NOT_SAVED = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        TitleNote = findViewById(R.id.titleNote);
        DescNote = findViewById(R.id.descNote);
        buttonSave = findViewById(R.id.saveNote);

        DescNote.setMovementMethod(new ScrollingMovementMethod());
        DescNote.setTextIsSelectable(true);

        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_NOTE")) {
            n = (Notes) intent.getSerializableExtra("EDIT_NOTE");
            TitleNote.setText(n.getTitle());
            DescNote.setText(n.getContent());
            Log.d(TAG, "onCreate: checktext\n"+TitleNote.getText().toString());
        } else {
            n = new Notes();
        }
    }

    @Override
    protected void onResume() {
        if (n != null) {
            TitleNote.setText(n.getTitle());
            DescNote.setText(n.getContent());
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        n.setTitle(TitleNote.getText().toString());
        n.setContent(DescNote.getText().toString());

        super.onPause();
    }

    public void editSave(MenuItem item) {
        Intent exit = new Intent();

        if (TitleNote.getText().toString() == null) {
            Toast.makeText(this, "Note must have a title to save", Toast.LENGTH_LONG).show();
            exit.putExtra("NOT_SAVED", n);
            setResult(NOT_SAVED, exit);
            finish();
        }
        DateFormat df = new SimpleDateFormat("EEE MMM dd, HH:mm a");
        String date = df.format(Calendar.getInstance().getTime());
        n.setTitle(TitleNote.getText().toString());
        n.setContent(DescNote.getText().toString());
        n.setTime(date);
        exit.putExtra("SAVED_NOTE", n);
        Log.d(TAG, "editSave: put saved code + note:"+n.toString());
        setResult(SAVED_NOTE, exit);
        finish();
    }

    @Override
    public void onBackPressed() {
        final Intent data  = new Intent();
        data.putExtra("TITLE_TEXT_IDENTIFIER", TitleNote.getText().toString());
        data.putExtra("DESC_TEXT_IDENTIFIER", DescNote.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note Not Saved");
        builder.setMessage("This note will not be saved. Would you like to save the changes?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TitleNote.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Note must have a title to save", Toast.LENGTH_LONG).show();
                    data.putExtra("NOT_SAVED", n);
                    setResult(NOT_SAVED, data);
                    EditActivity.super.onBackPressed();
                    finish();

                    Toast.makeText(EditActivity.this, getString(R.string.not_saved), Toast.LENGTH_SHORT).show();
                }
                data.putExtra("SAVED_NOTE", n);
                setResult(SAVED_NOTE, data);
                EditActivity.super.onBackPressed();
                finish();

                Toast.makeText(EditActivity.this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                data.putExtra("NOT_SAVED", n);
                setResult(NOT_SAVED, data);
                EditActivity.super.onBackPressed();
                finish();

                Toast.makeText(EditActivity.this, getString(R.string.not_saved), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TITLE", TitleNote.getText().toString());
        outState.putString("NOTE DESCRIPTION", DescNote.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TitleNote.setText(savedInstanceState.getString("TITLE"));
        DescNote.setText(savedInstanceState.getString("NOTE DESCRIPTION"));
    }
}
