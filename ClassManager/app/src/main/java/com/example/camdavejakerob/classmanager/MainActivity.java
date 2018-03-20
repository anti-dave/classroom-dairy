package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "This Activity";

    private ListView listView;

    private static final int SIGN_IN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Don't need to build this if they're only using emial.
        // Build an arraylist of providers if we decide
        // to use more options like facebook and google
        //new AuthUI.IdpConfig.EmailBuilder().build();

        //The Auth is using Google I think
        //But
        /*******************Authorization, Registration Check**************************/
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE);
            //Alternative
            //Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            //startActivity(loginIntent);

        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG).show();
        }

        final LinearLayout myClassesButton = findViewById(R.id.my_classes);
        myClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
                startActivity(classesIntent);
            }
        });

        final LinearLayout notificationsButton = findViewById(R.id.notifications);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(notificationsIntent);
            }
        });

        final LinearLayout settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(settingsIntent);
            }
        });

        final LinearLayout messagesButton = findViewById(R.id.messages);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent messagesIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(messagesIntent);
            }
        });

        final LinearLayout calendarButton = findViewById(R.id.calendar);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent calendarIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(calendarIntent);
            }
        });

        final LinearLayout infoButton = findViewById(R.id.inforamtion);
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoIntent);
            }
        });
        // we might want to load all of there info on to a local data base so it can run faster
//        Intent classesIntent = new Intent(MainActivity.this, ClassActivity.class);
//        startActivity(classesIntent);

    } //OnCreate

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
            } else {
                Toast.makeText(this,
                        "Sign in Failed. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }
}
