package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.camdavejakerob.classmanager.Assignments.Assignment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AssignmentActivity extends AppCompatActivity {

    private String CLASS_ID = "CLASS_ID";
    private FirebaseUser curUser;
    private String classId;
    private ListView mListView;
    private DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Intent i = getIntent();
        setTitle(i.getStringExtra("TITLE"));

        //This generates the list view of all classes the user is currently enrolled in.
        //for right now it is displaying classes that user u0 is enrolled in
        // in the future we will use FirebaseAuth to get current users id and then call method using that

        curUser = FirebaseAuth.getInstance().getCurrentUser();
        classId = getIntent().getStringExtra(CLASS_ID);
        mListView = (ListView) findViewById(R.id.assignment_list);
        mDatabase = new DatabaseHelper();
        mDatabase.getUserAssignment(this, mListView, curUser.getUid(), classId);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(AssignmentActivity.this, "hey you tapped im tapping back", Toast.LENGTH_SHORT).show();
                // should download a pdf from a link

            }
        });
    }
}
