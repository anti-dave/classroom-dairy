package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    private ClassAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // some dummy data
        String[] today = {"today"};
        String[] tomorrow = {"tomorrow"};

        // Create a list of classes
        final ArrayList<Class> classes = new ArrayList<Class>();

        // add classes here yo
        classes.add(new Class("class1", today, "1pm","2pm","os101"));

        classes.add(new Class("class2", tomorrow, "11pm","2pm","os1021"));

        classes.add(new Class("class3", tomorrow, "12pm","2pm","os1031"));

        classes.add(new Class("class4", today, "13pm","2pm","os1041"));

        adapter = new ClassAdapter(this, classes);
        listView = (ListView) findViewById(R.id.classes);
        listView.setAdapter(adapter);

        final TextView dummyButton = findViewById(R.id.dummy);
        dummyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent dummyIntent = new Intent(ClassActivity.this, ClassInfoActivity.class);
                startActivity(dummyIntent); // probaly should pass the class to so we can change the title but this is just a dummy
            }
        });
    }
}
