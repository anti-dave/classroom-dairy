package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
import android.content.Intent;
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

        // assigns the currentClass
        Intent i = getIntent();

        mCurrentClass = (Class) i.getParcelableExtra("CURRENT_CLASS");

        // sets title to selected class name
        setTitle(mCurrentClass.getName());

        final LinearLayout syllabusButton = findViewById(R.id.syllabus);
        syllabusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                User user = ((ClassManagerApp) ClassInfoActivity.this.getApplication()).getCurUser();

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
                                    uploadIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                                    startActivity(uploadIntent);
                                }
                            })
                            .setNeutralButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //getSyllabus();
                                }
                            });
                    AlertDialog sylDialog = sylAlertBuilder.create();
                    sylDialog.show();
                } else {
                    // IF STUDENT
                        //download the syllabus if there is one
                    //Toast.makeText(ClassInfoActivity.this,"not an instructor we downloading this",Toast.LENGTH_SHORT);
                    Log.d(TAG, "onClick: we are a student !? :(");
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
                Intent discussionBoardIntent = new Intent(ClassInfoActivity.this, ChatActivity.class);
                startActivity(discussionBoardIntent);
            }
        });

        final LinearLayout assignmentsButton = findViewById(R.id.assignments);
        assignmentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent assignmentsIntent = new Intent(ClassInfoActivity.this, AssignmentActivity.class);
                assignmentsIntent.putExtra("TITLE", mCurrentClass.getName() + " Assignments");
                assignmentsIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                startActivity(assignmentsIntent);
            }
        });

        final LinearLayout gradesButton = findViewById(R.id.grades);
        gradesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent gradesIntent = new Intent(ClassInfoActivity.this, GradesActivity.class);
                gradesIntent.putExtra(CLASS_ID, mCurrentClass.getCourseID());
                startActivity(gradesIntent);
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
        StorageReference ref = FirebaseStorage.getInstance()
                .getReference(mCurrentClass.getCourseID())
                .child("syllabus");

        if(ref == null){
            Log.d(TAG, "getSyllabus: is null, something went wrong");
        } else {
            //Log.d(TAG, "getSyllabus: is not null!!!! we win");
            File downloadLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            try {
                File localFile = File.createTempFile(mCurrentClass.getName() + "Syllabus","pdf",downloadLocation);
                ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //do stuff
                        Log.d(TAG, "onSuccess: i guess we downloaded this thing");
                        //taskSnapshot.notify();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do error stuff i guess :(
                        Log.d(TAG, "onFailure: it seems we failed to do the download thing :(");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
