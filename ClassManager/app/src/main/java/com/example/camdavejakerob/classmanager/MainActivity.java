package com.example.camdavejakerob.classmanager;

import android.content.Intent;
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

        final LinearLayout button = findViewById(R.id.inforamtion);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent classesIntent = new Intent(MainActivity.this, ClassInfoActivity.class);
                startActivity(classesIntent);
            }
        });
        // we might want to load all of there info on to a local data base so it can run faster
//        Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
//        startActivity(classesIntent);


    }
}
