package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AssignmentActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Intent i = getIntent();
        setTitle(i.getStringExtra("TITLE"));

        //This generates the list view of all classes the user is currently enrolled in.
        //for right now it is displaying classes that user u0 is enrolled in
        // in the future we will use FirebaseAuth to get current users id and then call method using that
        mListView = (ListView) findViewById(R.id.assignment_list);


        // DEMO YO
        ArrayList<Assignment> locations = new ArrayList<Assignment>();

        locations.add(new Assignment("02/24/2049", "GET GUD", "Juice Tasting 101"));
        locations.add(new Assignment("Yesterday", "Yu GOT GUD", "The JUICE Test"));
        locations.add(new Assignment("Today", "GG", "STOP DROP AND ROLL"));
        locations.add(new Assignment("BBQ EVERYDAY", "BRING THE DOGS", "BBQ LIFE"));

        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(this, locations);

        mListView.setAdapter(assignmentAdapter);

        //DatabaseHelper database = new DatabaseHelper();
        // maybe pass in the CID
        //database.updateListViewUserClasses(this,mListView,"u0");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(AssignmentActivity.this, "hey you tapped im tapping back", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
