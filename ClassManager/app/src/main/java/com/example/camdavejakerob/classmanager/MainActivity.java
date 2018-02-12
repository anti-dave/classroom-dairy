package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // we might want to load all of there info on to a local data base so it can run faster
        Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
        startActivity(classesIntent);
    }
}
