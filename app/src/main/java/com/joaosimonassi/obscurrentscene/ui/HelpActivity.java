package com.joaosimonassi.obscurrentscene.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.joaosimonassi.obscurrentscene.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().hide();

    }
}