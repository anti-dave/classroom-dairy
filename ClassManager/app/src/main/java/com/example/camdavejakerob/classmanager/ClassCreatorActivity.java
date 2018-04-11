package com.example.camdavejakerob.classmanager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TimePicker;

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
    String weekdays = "";
    private static final String TAG = "ClassCreatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creator);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.class_add_fab);

        final CheckBox monday = findViewById(R.id.monday_button);
        final CheckBox tuesday = findViewById(R.id.tuesday_button);;
        final CheckBox wednesday = findViewById(R.id.wednesday_button);;
        final CheckBox thursday = findViewById(R.id.thursday_button);;
        final CheckBox friday = findViewById(R.id.friday_button);;

        final EditText classIdEditText = (EditText) findViewById(R.id.class_id_form);
        final EditText nameEditText = (EditText) findViewById(R.id.class_name_form);
        final EditText roomEditText = (EditText) findViewById(R.id.class_room_form);
        
        final TimePicker startTimepicker = (TimePicker) findViewById(R.id.startTimePicker);
        final TimePicker endTimepicker = (TimePicker) findViewById(R.id.endTimePicker);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String classId = classIdEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String room = roomEditText.getText().toString();

                //check if inputs are valid
                if( paramsAreValid(classId, name, room) ){

                    if(monday.isChecked() ) {
                        weekdays += ("M ");
                        Log.d(TAG, "Monday");
                    }
                    if(tuesday.isChecked()) {
                        weekdays += ("Tu ");
                        Log.d(TAG, "Tuesday");
                    }
                    if(wednesday.isChecked()) {
                        weekdays += ("W ");
                        Log.d(TAG, "Wednesday");
                    }
                    if(thursday.isChecked()) {
                        weekdays += ("T ");
                        Log.d(TAG, "Thursday");
                    }
                    if(friday.isChecked()) {
                        weekdays += ("F ");
                        Log.d(TAG, "Friday");
                    }
                    if(weekdays.isEmpty()) {
                        weekdays += ("TBA");
                        Log.d(TAG, "TBA" );
                    }

                    String startTime;
                    String endTime;

                    int startHour = startTimepicker.getCurrentHour();
                    int startMinute = startTimepicker.getCurrentMinute();

                    int endHour = endTimepicker.getCurrentHour();
                    int endMinute = endTimepicker.getCurrentMinute();

                    startTime =
                            String.valueOf(startHour)
                            + ":"
                            + String.valueOf(startMinute) ;

                    endTime =
                            String.valueOf(endHour)
                                    + ":"
                                    + String.valueOf(endMinute) ;

                    database.writeNewClass(
                            classId,
                            name,
                            weekdays,
                            startTime,
                            endTime,
                            room);

                    Toast.makeText(ClassCreatorActivity.this,
                            name + " Created",
                            Toast.LENGTH_LONG)
                            .show();

                    finish();
                } else {
                    Toast.makeText(ClassCreatorActivity.this,
                            "Incomplete Form",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private Boolean paramsAreValid(String classId,
                                   String name,
                                   String room) {

        if( !classId.isEmpty()
                && !name.isEmpty()
                && !room.isEmpty() ){
            return true;
        } else { return false; }

    }
}
