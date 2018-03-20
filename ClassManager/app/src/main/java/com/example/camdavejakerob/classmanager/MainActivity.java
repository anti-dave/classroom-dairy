package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout myClassesButton = findViewById(R.id.my_classes);
        myClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
                startActivity(classesIntent);
            }
        });

        final LinearLayout notificationsButton = findViewById(R.id.notifications);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(notificationsIntent);
            }
        });

        final LinearLayout settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(settingsIntent);
            }
        });

        final LinearLayout messagesButton = findViewById(R.id.messages);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent messagesIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(messagesIntent);
            }
        });

        final LinearLayout calendarButton = findViewById(R.id.calendar);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent calendarIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(calendarIntent);
            }
        });

        final LinearLayout infoButton = findViewById(R.id.inforamtion);
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoIntent);
            }
        });
        // we might want to load all of there info on to a local data base so it can run faster
//        Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
//        startActivity(classesIntent);


    }
}
