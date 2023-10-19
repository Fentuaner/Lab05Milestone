package com.cs407.lab05milestone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteWriting extends AppCompatActivity {
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_writing);

        // 1. Get EditText view
        EditText editText = (EditText) findViewById(R.id.note);

        // 2. Get intent coming here from screen 2
        Intent intent = getIntent();

        // 3. Get the value of integer "noteid: from intent
        // 4. Initialize class car noteid with the value from intent
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            // Displaying content of the note by retrieving from ArrayList in SecondActivity
            SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            ArrayList<Notes> notesList = dbHelper.readNotes(username);

            // 5. Use editText.setText()to display the contents of this note on the screen
            String content = notesList.get(noteId).getContent();
            editText.setText(content);
        }

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. Get editText view and the content that user entered
                EditText editText = (EditText) findViewById(R.id.note);
                String content = editText.getText().toString();
                // 2. Initialize SQLiteDatabase instance.
                Context context = getApplicationContext();
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);
                // 3. Initialize DBHelper class.
                DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                // 4. Set username in the following variable by fetching it from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                // Save data in Database
                String title;
                DateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY HH:mm:ss");
                String date = dateFormat.format(new Date());
                Log.i("Info", "Printing noteid before using in condition" + noteId);
                if (noteId == -1) {
                    title = "NOTES_" + (dbHelper.readNotes(username).size() + 1);
                    Log.i("info", "printing content to be saved" + content);

                    context = getApplicationContext();
                    sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);
                    dbHelper = new DBHelper(sqLiteDatabase);

                    dbHelper.saveNotes(username, title, date, content);
                } else {
                    Log.i("Info", "Printing noteid from update condition " + noteId);
                    title = "NOTES_" + (noteId + 1);
                    dbHelper.updateNotes(content, date, title, username);
                }
                Intent intent = new Intent(NoteWriting.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickDelete(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void deleteButtonFunction(View view) {
        // 1. Initialize the sql
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        // 2. Initialize SharedPreferences to get the logged in username
        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        // 3. Get the content of the notes using getText()
        EditText editText = (EditText) findViewById(R.id.note);
        String content = editText.getText().toString();
        String title = "NOTES_" + (noteId + 1);
        dbHelper.deleteNotes(content, title);
        // 4. Use Intents to go back to screen 2
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}