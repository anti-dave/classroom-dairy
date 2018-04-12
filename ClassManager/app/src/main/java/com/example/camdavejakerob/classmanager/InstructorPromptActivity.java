package com.example.camdavejakerob.classmanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Rob on 3/24/2018.
 */

public class InstructorPromptActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instructor_prompt);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * 0.75),(int)(height * 0.55));

        Button submit = (Button)findViewById(R.id.button_instructor_prompt_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton instructor = (RadioButton)findViewById(R.id.radio_button_instructor_prompt_instructor);
                DatabaseHelper databaseHelper = new DatabaseHelper();
                String user = FirebaseAuth.getInstance().getUid();
                databaseHelper.updateUserToInstructor(user,instructor.isChecked());
                InstructorPromptActivity.this.finish();
            }
        });
    }
}
