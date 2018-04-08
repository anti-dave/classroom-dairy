package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AssignmentActivity extends AppCompatActivity {

    private String TAG = "ASSIGNMENT ACTIVITY";

    private ListView mListView;
    private DatabaseHelper mDatabase;
    private User mCurUser;
    private LinearLayout mLayout;
    private Button mNewAssignmentButton;
    private Class mCurrentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        // get the current class
        Intent i = getIntent();
        mCurrentClass = (Class) i.getParcelableExtra("CURRENT_CLASS");

        // get the current user
        mCurUser = ((ClassManagerApp) AssignmentActivity.this.getApplication()).getCurUser();

        // get the layout that holds the new assignment button
        mLayout = (LinearLayout) findViewById(R.id.assignment_teacher_button_layout);

        // display the button if the user is the instructor
        if(mCurUser.isInstructor()){
            mLayout.setVisibility(View.VISIBLE);

            mNewAssignmentButton = (Button) findViewById(R.id.assignment_teacher_button);
            mNewAssignmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //launch newAssignmentActivity
                    Intent newAssignmentIntent = new Intent(AssignmentActivity.this,NewAssignmentActivity.class);
                    newAssignmentIntent.putExtra("CURRENT_CLASS",mCurrentClass);
                    startActivity(newAssignmentIntent);
                }
            });

        } else {
            mLayout.setVisibility(View.GONE);
        }

        mListView = (ListView) findViewById(R.id.assignment_list);
        mDatabase = new DatabaseHelper();
        mDatabase.getUserAssignment(this, mListView, mCurUser.getUserId(), mCurrentClass.getCourseID());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(AssignmentActivity.this, "hey you tapped im tapping back", Toast.LENGTH_SHORT).show();

                if(mCurUser.isInstructor()) {
                    //view homework submissions
                } else {

                    TextView textView = (TextView) view.findViewById(R.id.assignment_name);

                    AlertDialog.Builder studentAlertBuilder = new AlertDialog.Builder(AssignmentActivity.this);
                    studentAlertBuilder.setCancelable(true)
                            .setTitle(textView.getText().toString())
                            .setMessage("View assignment or submit work?")
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //launch assignmentSubmission activity
                                }
                            })
                            .setNeutralButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //download the assignment
                                }
                            });

                    AlertDialog studentAlert = studentAlertBuilder.create();
                    studentAlert.show();
                }
            }
        });

    }
}
