package com.cs407.lab05milestone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void clickLogin(View view) {
        EditText userid = (EditText) findViewById(R.id.note);
        goToActivity();
        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);
        sharedPreferences.edit().putString("username", userid.getText().toString()).apply();
    }

    public void goToActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.lab05milestone", MODE_PRIVATE);

        if (!sharedPreferences.getString("username", "").equals("")) {
            goToActivity();
        } else {
            setContentView(R.layout.activity_main);
        }
    }
}