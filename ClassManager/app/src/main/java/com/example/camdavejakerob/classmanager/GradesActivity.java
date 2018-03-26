package com.example.camdavejakerob.classmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Rob on 3/26/2018.
 */

public class GradesActivity extends AppCompatActivity{

    private String CLASS_ID = "CLASS_ID";
    private FirebaseUser curUser;
    private String classId;
    private ListView mListView;
    private DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        curUser = FirebaseAuth.getInstance().getCurrentUser();
        classId = getIntent().getStringExtra(CLASS_ID);
        mListView = (ListView) findViewById(R.id.grades);
        mDatabase = new DatabaseHelper();
        mDatabase.getUserGrades(this, mListView, curUser.getUid(), classId);


    }
}
