package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listView;

    private static final int SIGN_IN_REQUEST_CODE = 1;

    private String curUserId;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*******************Authorization, Registration Check**************************/

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);

        } else {
            //User isn't verified yet
            if ( FirebaseAuth.getInstance()
                    .getCurrentUser().isEmailVerified() ) {

                // User is already signed in. Therefore, display
                // a welcome Toast
                Toast.makeText(this,
                        "Welcome " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Verify Email: " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                loginIntent.putExtra("Verify", true);
                startActivity(loginIntent);
            }
        }

        // update the global user variable
        updateCurUserData();

        final LinearLayout myClassesButton = findViewById(R.id.my_classes);

        myClassesButton.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){}});

        myClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCurUserData();
                Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
                startActivity(classesIntent);
            }
        });

        final LinearLayout notificationsButton = findViewById(R.id.notifications);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCurUserData();
                Intent notificationsIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(notificationsIntent);
            }
        });

        final LinearLayout calendarButton = findViewById(R.id.calendar);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCurUserData();
                Intent calendarIntent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(calendarIntent);
            }
        });

        //Chat Activity
        final LinearLayout messagesButton = findViewById(R.id.messages);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCurUserData();
                Intent messagesIntent = new Intent(MainActivity.this, MessageListActivity.class);
                startActivity(messagesIntent);
            }
        });

        final LinearLayout settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCurUserData();
                Intent settingsIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(settingsIntent);
            }
        });

        final LinearLayout infoButton = findViewById(R.id.inforamtion);
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCurUserData();
                Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoIntent);
            }
        });
    } //OnCreate

    private void updateCurUserData(){
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUser(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Sign in Successful",
                        Toast.LENGTH_LONG)
                        .show();

                //This is where we enter the user into the database
                final FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseHelper databaseHelper = new DatabaseHelper();
                databaseHelper.addUser(MainActivity.this, newUser);

                Log.d(TAG, "Database helper has launched");

            } else {
                Toast.makeText(this,
                        "Sign in Failed. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    } //onActivityResult


    @Override
    public void onResume(){
        super.onResume();

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);

        } else {
            //User isn't verified yet
            if ( FirebaseAuth.getInstance()
                    .getCurrentUser().isEmailVerified() ) {

                // User is already signed in. Therefore, display
                // a welcome Toast
                Toast.makeText(this,
                        "Welcome " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Verify Email: " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        }

    }
}//main
