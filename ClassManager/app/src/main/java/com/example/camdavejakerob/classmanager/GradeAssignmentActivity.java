package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Rob on 4/7/2018.
 */

public class GradeAssignmentActivity extends AppCompatActivity{

    private ListView mSubmissionList;
    private DatabaseHelper mDatabaseHelper;
    private Class mCurrentClass;
    private String mAssignmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        // using activity_class.xml because we only really need a list view
        // and it seemed silly to keep making identical layouts

        Intent i = getIntent();
        mCurrentClass = (Class) i.getParcelableExtra("CURRENT_CLASS");
        mAssignmentName = (String) i.getStringExtra("ASSIGNMENT_NAME");

        mDatabaseHelper = new DatabaseHelper();
        mSubmissionList = (ListView) findViewById(R.id.classes);
        mDatabaseHelper.getAllAssignmentSubmissions(mCurrentClass.getCourseID(),
                mAssignmentName, mSubmissionList, GradeAssignmentActivity.this);

        // if an item is clicked the instructor chooses to grade or download an assignment
        mSubmissionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {

                final TextView studentName = (TextView) view.findViewById(R.id.grading_student_name);
                TextView filePath = (TextView) view.findViewById(R.id.grading_file_path);

                AlertDialog.Builder builder = new AlertDialog.Builder(GradeAssignmentActivity.this);
                builder.setCancelable(true)
                        .setTitle(studentName.getText().toString())
                        .setMessage("Grade or view this assignment?")
                        .setPositiveButton("Grade", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ask them for a grade
                                // call the database.writegrade or whatever i called it
                                final EditText editText = new EditText(GradeAssignmentActivity.this);

                                AlertDialog.Builder builder = new AlertDialog.Builder(GradeAssignmentActivity.this);
                                builder.setTitle("Enter Grade")
                                        .setView(editText)
                                        .setPositiveButton("Submit Grade", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do the grade thing
                                                String cid, uid,grade;
                                                cid = mCurrentClass.getCourseID();
                                                TextView idTextView = view.findViewById(R.id.grading_student_id);
                                                uid = idTextView.getText().toString();
                                                grade = editText.getText().toString();
                                                mDatabaseHelper.writeAssignmentGrade(cid,uid,grade,mAssignmentName);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //do nothing
                                            }
                                        }).show();
                            }
                        })
                        .setNegativeButton("View", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //download the file if they have one
                                StorageReference assignmentRef = FirebaseStorage.getInstance().getReference();

                                assignmentRef.child(mCurrentClass.getCourseID()).child(mAssignmentName)
                                        .child(studentName.getText().toString() + ".pdf").getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(browserIntent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Grading Activity", "onFailure: ", e);
                                                Toast.makeText(GradeAssignmentActivity.this,
                                                        "Couldn't view this submission", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}
