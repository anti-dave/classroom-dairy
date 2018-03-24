package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        /*
        Rob was here, I am testing some stuff here please feel free to delete it if i forgot to remove it
         */
        mDatabase = new DatabaseHelper();

        mDatabase.updateListViewListOfClasses();

        //Assignment assignment = new Assignment("3/29/2018","0","Homework 1", false);
        //mDatabase.writeAssignment("c1",assignment);

        //mDatabase.addStudentToClass("c1","u0");
        //mDatabase.addStudentToClass("c1","u1");

        //mDatabase.addStudentToClass("c2","u0");
        //String[] days = {"Monday", "Wednesday", "Friday"};
        //mDatabase.writeNewClass("Life Science II", days, "1:00 pm", "1:50 pm","ball 214", 0);

        //mDatabase.writeNewUser("John","Avon","john_avon@lands.com", false);

        //TextView view = (TextView)findViewById(R.id.info_activity_test);
        //mDatabase.updateTextView("uids/u0/email",view);
        /*
        end Robs stuff
         */

    }
}
