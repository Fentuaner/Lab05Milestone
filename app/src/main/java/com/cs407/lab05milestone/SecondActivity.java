package com.cs407.lab05milestone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    TextView textView;
    private final ArrayList<String> displayNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        textView = (TextView) findViewById(R.id.textView);
        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);
        String str = sharedPreferences.getString("username", "");
        textView.setText("Welcome, " + str + "!");

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        ArrayList<Notes> notesList = dbHelper.readNotes(str);

        ArrayList<String> displayNotes = new ArrayList<>();

        for (Notes notes : notesList) {
            displayNotes.add(String.format("Title:%s\nDate:%s\n", notes.getTitle(), notes.getDate()));

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
            ListView notesListView = (ListView) findViewById(R.id.ListView);
            notesListView.setAdapter(adapter);
            notesListView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent intent = new Intent(getApplicationContext(), NoteWriting.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addNote) {
            Intent intent = new Intent(this, NoteWriting.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);
            String userName = sharedPreferences.getString("username", "");
            sharedPreferences.edit().clear().apply();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}