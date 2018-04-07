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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Rob on 4/6/2018.
 */

public class NewAssignmentActivity extends AppCompatActivity {

    private EditText mName, mDueDate;
    private TextView mFilePath, mFileName;
    private LinearLayout mFindFileLayout;
    private ListView mFilesListView;
    private Button mFindFileButton, mSubmit;
    private Class mCurrentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assignment);

        // get the current class
        Intent i = getIntent();
        mCurrentClass = (Class) i.getParcelableExtra("CURRENT_CLASS");

        mName = (EditText) findViewById(R.id.new_assignment_name);
        mDueDate = (EditText) findViewById(R.id.new_assignment_due_date);
        mFilePath = (TextView) findViewById(R.id.new_assignment_file_path);
        mFileName = (TextView) findViewById(R.id.new_assignment_file_name);
        mFindFileLayout = (LinearLayout) findViewById(R.id.new_assignment_find_file_view);
        mFilesListView = (ListView) findViewById(R.id.new_assignment_file_list);
        mFindFileButton = (Button) findViewById(R.id.new_assignment_find_file);
        mSubmit = (Button) findViewById(R.id.new_assignment_submit);

        populateListView();
        mFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView path = (TextView) view.findViewById(R.id.upload_file_item_path);
                TextView name = (TextView) view.findViewById(R.id.upload_file_item_name);
                final String fileName = name.getText().toString();
                final String filePath = path.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(NewAssignmentActivity.this);
                builder.setCancelable(true)
                        .setTitle(fileName)
                        .setMessage("Submit this file?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // update mFileView text
                                mFileName.setText(fileName);
                                mFilePath.setText(filePath);
                                // then hide this listview and display mFileView
                                mFilesListView.setVisibility(View.GONE);
                                mFileName.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mFindFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide button
                mFindFileLayout.setVisibility(View.GONE);
                // show list view
                mFilesListView.setVisibility(View.VISIBLE);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData()){

                    // upload file to storage
                    Uri uri = Uri.fromFile(new File(mFilePath.getText().toString()));

                    StorageReference ref = FirebaseStorage.getInstance().getReference()
                            .child(mCurrentClass.getCourseID() + "/" + mName.getText().toString() + ".pdf");
                    UploadTask uploadTask = ref.putFile(uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewAssignmentActivity.this, "Failed to create new assignment", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //tell them success!
                            Toast.makeText(NewAssignmentActivity.this, "Assignment created!", Toast.LENGTH_SHORT).show();
                            // update database
                            Assignment assignment = new Assignment(mDueDate.getText().toString(), "empty", mName.getText().toString());
                            DatabaseHelper databaseHelper = new DatabaseHelper();
                            databaseHelper.writeAssignment(mCurrentClass.getCourseID(),assignment);
                        }
                    });

                    // return to AssignmentActivity(go back???)
                    NewAssignmentActivity.this.finish(); // im concerned this might cause a crash

                } else {
                    Toast.makeText(NewAssignmentActivity.this,"Make sure each field is filled out",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // make sure that each field is field
    private boolean validData(){
        return mName.getText().toString() != ""
                && mDueDate.getText().toString() != ""
                && mFilePath.getText().toString() != "";
    }

    private void populateListView(){
        UploadAdapter uploadAdapter;
        ArrayList<File> files = new ArrayList<File>();

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] list = file.listFiles();

        for(int i = 0; i < list.length; i++){
            files.add(list[i]);
        }

        uploadAdapter = new UploadAdapter(this,files);
        mFilesListView.setAdapter(uploadAdapter);
    }
}
