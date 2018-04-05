package com.example.camdavejakerob.classmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
    final ArrayList<String> weekdays = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creator);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.class_add_fab);

        final Button monday = (Button) findViewById(R.id.monday_button);
        final Button tuesday = (Button) findViewById(R.id.tuesday_button);;
        final Button wednesday = (Button) findViewById(R.id.wednesday_button);;
        final Button thursday = (Button) findViewById(R.id.thursday_button);;
        final Button friday = (Button) findViewById(R.id.friday_button);;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if valid
               /* if(){

                }else {

                }
*/
                if(monday.isPressed()) {
                    weekdays.add("Monday");
                }
                else if(tuesday.isPressed()) {
                    weekdays.add("Tuesday");
                }
                else if(wednesday.isPressed()) {
                    weekdays.add("Wednesday");
                }
                else if(thursday.isPressed()) {
                    weekdays.add("Thursday");
                }
                else if(friday.isPressed()) {
                    weekdays.add("Friday");
                }
                else if(weekdays.isEmpty()) {
                    weekdays.add("TBA");
                }

                EditText classId = (EditText) findViewById(R.id.input);
                EditText name = (EditText) findViewById(R.id.input);
                EditText startTime = (EditText) findViewById(R.id.input);
                EditText endTime = (EditText) findViewById(R.id.input);
                EditText room = (EditText) findViewById(R.id.input);

                database.writeNewClass(
                        classId.toString(),
                        name.toString(),
                        weekdays,
                        startTime.toString(),
                        endTime.toString(),
                        room.toString() );
            }
        });
    }
}
