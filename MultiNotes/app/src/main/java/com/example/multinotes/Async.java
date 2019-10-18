/*
 * Copyright (c) 2019 , Carol Marin
 */

package com.example.multinotes;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Async extends AsyncTask<String, Void, ArrayList<Notes>> {
    private static final String TAG = "Async";
    private MainActivity mainActivity;

    public Async(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected ArrayList<Notes> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: ");
        ArrayList<Notes> notesList;
        notesList = load(strings[0]);
        return notesList;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Notes> note) {
        Log.d(TAG, "onPostExecute: ");
        super.onPostExecute(note);
        mainActivity.doAsync(note);
    }

    private ArrayList<Notes> load(String filename) {
        Log.d(TAG, "load: Loading JSON File");
        ArrayList<Notes> n = new ArrayList<Notes>();
        try {
            InputStream in = mainActivity.getApplicationContext().openFileInput(filename);
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                String title = "";
                String content = "";
                String timestamp = "";
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("Title")) {
                        title = reader.nextString();
                    } else if (name.equals("Content")) {
                        content = reader.nextString();
                    } else if (timestamp.equals("Time")) {
                        timestamp = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                n.add(new Notes(title, content, timestamp));
            }
            reader.endArray();
            reader.close();
            in.close();
            return n;
        } catch (FileNotFoundException e) {
            return n;
        } catch (Exception e) {
            e.printStackTrace();
            return n;
        }
    }
}
