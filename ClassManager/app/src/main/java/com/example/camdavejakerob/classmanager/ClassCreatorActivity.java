package com.example.camdavejakerob.classmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Davey on 3/20/2018.
 */
/*
* This Activity allows the user to create and add themselves to a class
* */
public class ClassCreatorActivity extends AppCompatActivity {

    //query firebase DB for getComponentName()
    DatabaseHelper database = new DatabaseHelper();
    private String CIDS = "cids", UIDS = "uids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creator);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.message_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if valid
                if(){

                }else {

                }

                EditText classId = (EditText) findViewById(R.id.input);
                EditText name = (EditText) findViewById(R.id.input);
                EditText daysOfClass = (EditText) findViewById(R.id.input);
                EditText startTime = (EditText) findViewById(R.id.input);
                EditText endTime = (EditText) findViewById(R.id.input);
                EditText room = (EditText) findViewById(R.id.input);

                database.writeNewClass(
                        classId.toString(),
                        name.toString(),
                        daysOfClass.toString(),
                        startTime.toString(),
                        endTime.toString(),
                        room.toString() );
            }
        });
    }
}
