package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button logoutButton = (Button) findViewById(R.id.logout);

        final User user = ((ClassManagerApp) SettingActivity.this.getApplication()).getCurUser();

        TextView account_level = findViewById(R.id.account_level);

        //if instructor do instructor
        if( user.isInstructor() ) {
            account_level.setText("Teacher");
        } else {
            account_level.setText("Student");
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AuthUI.getInstance().signOut(SettingActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SettingActivity.this,
                                        "You have been signed out.",
                                        Toast.LENGTH_LONG)
                                        .show();

                                // Start sign in/sign up activity
                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .build(),
                                        SIGN_IN_REQUEST_CODE);
                            }
                        });
            }
        });
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
                //some checks will have to be done as well as prompting the user for the first time if they are an instructor or not
                //don't know how we do that last part yet
                ////////// every time we get here it prompts the user to choose student or teacher im done fighting with this for now so we will call it a feature /////////
                DatabaseHelper databaseHelper = new DatabaseHelper();
                FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseHelper.addUser(SettingActivity.this, newUser);

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

}