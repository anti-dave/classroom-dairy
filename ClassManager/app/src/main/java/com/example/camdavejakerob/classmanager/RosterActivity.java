package com.example.camdavejakerob.classmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by Rob on 3/26/2018.
 */

public class RosterActivity extends AppCompatActivity{

    private String CLASS_ID = "CLASS_ID";
    private String classId;
    private ListView mListView;
    private DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        classId = getIntent().getStringExtra(CLASS_ID);
        mListView = (ListView) findViewById(R.id.roster_students);
        mDatabase = new DatabaseHelper();
        mDatabase.getEnrolledStudents(this, mListView, classId);

    }
}
