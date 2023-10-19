package com.cs407.lab05milestone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    static SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public static void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes" + "(ID INTEGER PRIMARY KEY, noteId INTEGER, username TEXT, date TEXT, content TEXT, title TEXT)");
    }

    public ArrayList<Notes> readNotes(String username) {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE username LIKE ?", new String[]{"%" + username + "%"});

        int dateIndex = c.getColumnIndex("date");
        int titleIndex = c.getColumnIndex("title");
        int contentIndex = c.getColumnIndex("content");
        c.moveToFirst();
        ArrayList<Notes> notesList = new ArrayList<>();

        while (!c.isAfterLast()) {
            String title = c.getString(titleIndex);
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);

            Notes notes = new Notes(date, username, title, content);
            notesList.add(notes);
            c.moveToNext();
        }

        c.close();
        sqLiteDatabase.close();

        return notesList;
    }

    public void saveNotes(String username, String title, String date, String content) {
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO notes (username, date, title, content) VALUES (?, ?, ?, ?)",
                new String[]{username, date, title, content});
    }

    public void updateNotes(String content, String date, String title, String username) {
        createTable();
        Notes note = new Notes(date, username, title, content);
        sqLiteDatabase.execSQL("UPDATE notes set content=?, date=? where title=? and username=?",
                new String[]{content, date, title, username});
    }

    public void deleteNotes(String content, String title) {
        createTable();
        String date = "";

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT date FROM notes WHERE content = ?", new String[]{content});

        if (cursor.moveToNext()) {
            date = cursor.getString(0);
        }

        sqLiteDatabase.execSQL("DELETE FROM notes WHERE content = ? AND date = ?", new String[]{content, date});

        cursor.close();
    }


}
