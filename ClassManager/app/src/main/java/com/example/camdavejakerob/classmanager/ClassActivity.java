package com.example.camdavejakerob.classmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    private ClassAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // Create a list of songs
        final ArrayList<Class> classes = new ArrayList<Class>();

        // add songs here yo
        classes.add(new Class("class1", "today", "1pm","2pm", 50,"os101"));

        classes.add(new Class("class2", "today++", "11pm","2pm",  50,"os1021"));

        classes.add(new Class("class3", "today++", "12pm","2pm",  50,"os1031"));

        classes.add(new Class("class4", "today+", "13pm","2pm",  50,"os1041"));

        adapter = new ClassAdapter(this, classes);
        listView = (ListView) findViewById(R.id.classes);
        listView.setAdapter(adapter);
    }
}
