package com.example.camdavejakerob.classmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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


        // I am using this as a place to test functions, delete this stuff if you decide to work on this
        //mDatabase = new DatabaseHelper();

        //mDatabase.addStudentToClass("c2","u0");
        //String[] days = {"Monday", "Wednesday", "Friday"};
        //mDatabase.writeNewClass("Life Science II", days, "1:00 pm", "1:50 pm","ball 214", 0);

        //mDatabase.writeNewUser("Volkan","Baga","volkan_baga@mox.opal", false);
    }
}
