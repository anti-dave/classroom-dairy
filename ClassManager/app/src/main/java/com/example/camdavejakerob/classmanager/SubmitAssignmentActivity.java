package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Rob on 4/7/2018.
 */

public class SubmitAssignmentActivity extends AppCompatActivity {

    private ListView mFiles;
    private Class mCurrentClass;
    private String mAssignmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        Intent intent = getIntent();
        mCurrentClass = (Class) intent.getParcelableExtra("CURRENT_CLASS");
        mAssignmentName = (String) intent.getStringExtra("ASSIGNMENT_NAME");


        mFiles = (ListView) findViewById(R.id.submit_assignment_list_view);

        // populate list with files found in the downloads directory
        UploadAdapter uploadAdapter;
        ArrayList<File> files = new ArrayList<File>();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] list = file.listFiles();
        for(int i = 0; i < list.length; i++){
            files.add(list[i]);
        }
        uploadAdapter = new UploadAdapter(this,files);
        mFiles.setAdapter(uploadAdapter);

        mFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView path = (TextView) view.findViewById(R.id.upload_file_item_path);
                TextView name = (TextView) view.findViewById(R.id.upload_file_item_name);
                final String fileName = name.getText().toString();
                final String filePath = path.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(SubmitAssignmentActivity.this);
                builder.setCancelable(true)
                        .setTitle(mAssignmentName)
                        .setMessage("Submit " + fileName + " for this assignment?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // hide the list view so that the user does not click something else while upload is happening
                                mFiles.setVisibility(View.GONE);

                                Uri uri = Uri.fromFile(new File(filePath));

                                // example path = comp3040/homework 1/robert cucchiara.pdf
                                StorageReference submitRef = FirebaseStorage.getInstance().getReference()
                                        .child(mCurrentClass.getCourseID() + "/" + mAssignmentName + "/"
                                        + FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                                        + ".pdf");
                                UploadTask uploadTask = submitRef.putFile(uri);

                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SubmitAssignmentActivity.this, "Submission failed", Toast.LENGTH_SHORT).show();
                                        SubmitAssignmentActivity.this.finish();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // tell them we win
                                        Toast.makeText(SubmitAssignmentActivity.this, "Assignment submitted", Toast.LENGTH_SHORT).show();
                                        //update database
                                        DatabaseHelper databaseHelper = new DatabaseHelper();
                                        databaseHelper.writeAssignmentSubmission(
                                                FirebaseAuth.getInstance().getUid(), mCurrentClass.getCourseID(),
                                                mAssignmentName, taskSnapshot.getDownloadUrl().toString());
                                        //return to previous activity
                                        SubmitAssignmentActivity.this.finish();
                                    }
                                });


                                // add this file to the storage
                                // add to the database that current user has submitted assignment with the information to download
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

}
