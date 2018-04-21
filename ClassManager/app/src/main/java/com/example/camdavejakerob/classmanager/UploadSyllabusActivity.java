package com.example.camdavejakerob.classmanager;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Rob on 3/30/2018.
 */

public class UploadSyllabusActivity extends AppCompatActivity {

    private ListView mListView;
    private String TAG = "SYLLABUS_UPLOAD";
    private String CLASS_ID = "CLASS_ID";
    private Class mCurrentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_syllabus);

        // remove the up arrow at the top
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        Intent intent = getIntent();
        //classId = getIntent().getStringExtra(CLASS_ID);
        mCurrentClass = (Class) intent.getParcelableExtra("CURRENT_CLASS");

        // set up the list view
        mListView = (ListView) findViewById(R.id.list_of_files);

        UploadAdapter uploadAdapter;
        ArrayList<File> files = new ArrayList<File>();

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] list = file.listFiles();

        for(int i = 0; i < list.length; i++){
            files.add(list[i]);
        }

        uploadAdapter = new UploadAdapter(this,files);
        mListView.setAdapter(uploadAdapter);

        //set up an onclick listener for the list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //ask user once an item is selected if they are sure they want to upload this item
                TextView path = (TextView) view.findViewById(R.id.upload_file_item_path);
                TextView name = (TextView) view.findViewById(R.id.upload_file_item_name);
                String fileName = name.getText().toString();
                final String filePath = path.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadSyllabusActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Upload this file?");
                builder.setMessage(fileName);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // hide list of files and display progress bar
                        mListView.setVisibility(View.GONE);
                        ((RelativeLayout) findViewById(R.id.upload_syllabus_loading_panel))
                                .setVisibility(View.VISIBLE);

                        Uri file = Uri.fromFile(new File(filePath));
                        StorageReference ref = FirebaseStorage.getInstance().getReference().
                                child(mCurrentClass.getCourseID() + "/" + mCurrentClass.getName() + " syllabus.pdf");
                        UploadTask uploadTask = ref.putFile(file);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AlertDialog.Builder failBuilder = new AlertDialog.Builder(UploadSyllabusActivity.this);
                                failBuilder.setCancelable(true);
                                failBuilder.setTitle("Upload Failed");
                                failBuilder.setMessage("Something went wrong, failed to upload file");
                                failBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                                AlertDialog confirmDialog = failBuilder.create();
                                confirmDialog.show();
                                Log.e(TAG, "onFailure: ", e);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Toast.makeText(UploadSyllabusActivity.this, "Upload Complete!", Toast.LENGTH_SHORT).show();
                                UploadSyllabusActivity.this.finish();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                }).show();

            }
        });
    }
}
