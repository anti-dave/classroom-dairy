package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listView;

    private static final int SIGN_IN_REQUEST_CODE = 1;

    private String curUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Don't need to build this if they're only using emial.
        //new AuthUI.IdpConfig.EmailBuilder().build();
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
            //Log.d("MAIN ACTIVITY", "onCreate: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG).show();
        }

        // update the global user variable
        updateCurUserData();

        final LinearLayout myClassesButton = findViewById(R.id.my_classes);

        myClassesButton.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                Toast.makeText(MainActivity.this,"hey i have focus", Toast.LENGTH_LONG).show();
                //LinearLayout ln = (LinearLayout) findViewById(R.id.my_classes);
                //ln.setBackgroundColor(0xfff);
            }
        });

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
                Intent calendarIntent = new Intent(MainActivity.this, InfoActivity.class);
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

    
////////////////////// deprecated moved to setting////////////////////////////
//    /**
//     * Creates the menu with oncreate.
//     *
//     * @param menu
//     * @return bool on whether it was successful.
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_sign_in_dropdown, menu);
//        return true;
//    }//onCreateOptionsMenu
//
//
//    /**
//     * Logic for when menu is pressed.
//     *
//     * @param item
//     * @return bool on whether it was successful.
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_sign_out) {
//            AuthUI.getInstance().signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(MainActivity.this,
//                                    "You have been signed out.",
//                                    Toast.LENGTH_LONG)
//                                    .show();
//
//                            // Close activity, isneatd of close activity, make go back
//                            //finish();
//                        }
//                    });
//            // Start sign in/sign up activity
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .build(),
//                    SIGN_IN_REQUEST_CODE);
//        }
//        if(item.getItemId() == R.id.action_change_account) {
//            AuthUI.getInstance().signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(MainActivity.this,
//                                    "You have been signed out.",
//                                    Toast.LENGTH_LONG)
//                                    .show();
//
//                            // Close activity, isneatd of close activity, make go back
//                            //finish();
//                        }
//                    });
//            // Start sign in/sign up activity
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .build(),
//                    SIGN_IN_REQUEST_CODE);
//        }
//        return true;
//    }

}//main
