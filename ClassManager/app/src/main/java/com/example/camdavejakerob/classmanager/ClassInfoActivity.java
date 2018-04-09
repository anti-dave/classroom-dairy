package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ClassInfoActivity extends AppCompatActivity {

    private String TAG = "CLASS INFO ACTIVITY";
    private String CLASS_ID = "CLASS_ID";
    private Class mCurrentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        // get current user
        final User user = ((ClassManagerApp) ClassInfoActivity.this.getApplication()).getCurUser();

        // assigns the currentClass
        Intent i = getIntent();

        mCurrentClass = (Class) i.getParcelableExtra("CURRENT_CLASS");

        // sets title to selected class name
        setTitle(mCurrentClass.getName());

        final LinearLayout syllabusButton = findViewById(R.id.syllabus);
        syllabusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // IF INSTRUCTOR
                if(user.isInstructor()) {
                    // ask if they want to view or upload syllabus
                    AlertDialog.Builder sylAlertBuilder = new AlertDialog.Builder(ClassInfoActivity.this);
                    sylAlertBuilder.setCancelable(true)
                            .setTitle("Syllabus")
                            .setMessage("View or upload Syllabus?")
                            .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent uploadIntent = new Intent(ClassInfoActivity.this,UploadSyllabusActivity.class);
                                    uploadIntent.putExtra("CURRENT_CLASS", mCurrentClass);
                                    startActivity(uploadIntent);
                                }
                            })
                            .setNeutralButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getSyllabus();
                                }
                            });
                    AlertDialog sylDialog = sylAlertBuilder.create();
                    sylDialog.show();
                } else {
                    getSyllabus();
                }
            }
        });

        final LinearLayout attendanceButton = findViewById(R.id.attendance);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent  syllabusIntent = new Intent(ClassInfoActivity.this, InfoActivity.class);
                startActivity( syllabusIntent);
            }
        });

        final LinearLayout discussionBoardButton = findViewById(R.id.discussion_board);
        discussionBoardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent discussionBoardIntent = new Intent(ClassInfoActivity.this, MessageListActivity.class);

                // Set the URI on the data field of the intent
                discussionBoardIntent.putExtra("cid", CLASS_ID);
                startActivity(discussionBoardIntent);
            }
        });

        final LinearLayout assignmentsButton = findViewById(R.id.assignments);
        assignmentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent assignmentsIntent = new Intent(ClassInfoActivity.this, AssignmentActivity.class);
                assignmentsIntent.putExtra("CURRENT_CLASS", mCurrentClass);
                startActivity(assignmentsIntent);
            }
        });

        final LinearLayout gradesButton = findViewById(R.id.grades);
        gradesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if the user is an instructor go to assignments where they can grade ELSE go to the grades to see
                if(user.isInstructor()){
                    Intent assignmentsIntent = new Intent(ClassInfoActivity.this, AssignmentActivity.class);
                    assignmentsIntent.putExtra("CURRENT_CLASS", mCurrentClass);
                    startActivity(assignmentsIntent);
                } else {
                    Intent gradesIntent = new Intent(ClassInfoActivity.this, GradesActivity.class);
                    gradesIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                    startActivity(gradesIntent);
                }
            }
        });

        final LinearLayout studentsButton = findViewById(R.id.students);
        studentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent studentsIntent = new Intent(ClassInfoActivity.this, RosterActivity.class);
                studentsIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                startActivity(studentsIntent);
            }
        });

    }

    private void getSyllabus(){

            StorageReference ref = FirebaseStorage.getInstance().getReference();

        ref.child(mCurrentClass.getCourseID()).child(mCurrentClass.getName() + " syllabus.pdf").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: we successfull!" + uri.toString());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browserIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }

}
