package com.example.camdavejakerob.classmanager;

import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
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
                Intent discussionBoardIntent = new Intent(ClassInfoActivity.this, DiscussionBoardActivity.class);

                Log.d(TAG,  mCurrentClass.getCourseID());
                Log.d(TAG,  mCurrentClass.getCourseID().toString());

                // Set the URI on the data field of the intent
                discussionBoardIntent.putExtra("cid",  mCurrentClass.getCourseID().toString() );
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

    private void getSyllabus() {
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
                Toast.makeText(ClassInfoActivity.this, "No Syllabus Found", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }
    /**
     * Creates the menu with oncreate.
     *
     * @param menu
     * @return bool on whether it was successful.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final User user = ((ClassManagerApp) ClassInfoActivity.this.getApplication()).getCurUser();

        //if instructor do instructor
        if( user.isInstructor() ) {
            getMenuInflater().inflate(R.menu.teacher_class_info_dropdown, menu);
        } else {
            //if teacher do teacher
            getMenuInflater().inflate(R.menu.student_class_info_dropdown, menu);
        }
        return true;
    }

    /**
     * Logic for when menu is pressed.
     *
     * @param item
     * @return bool on whether it was successful.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DatabaseHelper database = new DatabaseHelper();

        switch (item.getItemId()) {

            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;

            case R.id.action_cancel_class:
                //add class action
                Toast.makeText(ClassInfoActivity.this,
                        "Delete Class",
                        Toast.LENGTH_LONG)
                        .show();

                database.deleteClass( mCurrentClass.getCourseID(), FirebaseAuth.getInstance().getCurrentUser().getUid() );
                finish();

            case R.id.action_drop_class:
                //add class action
                Toast.makeText(ClassInfoActivity.this,
                        "Drop Class",
                        Toast.LENGTH_LONG)
                        .show();

                database.deleteStudentFromClass( mCurrentClass.getCourseID(), FirebaseAuth.getInstance().getCurrentUser().getUid() );
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}