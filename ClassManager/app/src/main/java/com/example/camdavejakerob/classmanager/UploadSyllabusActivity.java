package com.example.camdavejakerob.classmanager;

import android.content.DialogInterface;
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
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_syllabus);

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
                //TextView item = (TextView)adapterView.getItemAtPosition(position);
                TextView path = (TextView) view.findViewById(R.id.upload_file_item_path);
                TextView name = (TextView) view.findViewById(R.id.upload_file_item_name);
                String fileName = name.getText().toString();
                final String filePath = path.getText().toString();

                //confirmationPopup(name.getText().toString(), path.getText().toString(),"COMP3040");

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadSyllabusActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Upload this file?");
                builder.setMessage(fileName);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // upload item to the correct location in FirebaseStorage
                        ////not sure how to do this last part, i think im going to need a class id or something.......

                        Uri file = Uri.fromFile(new File(filePath));
                        StorageReference ref = FirebaseStorage.getInstance().getReference().child("COMP3040/syllabus");
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
                                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(UploadSyllabusActivity.this);
                                confirmBuilder.setCancelable(true);
                                confirmBuilder.setTitle("Success!");
                                confirmBuilder.setMessage("File finished uploading");
                                confirmBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                                AlertDialog confirmDialog = confirmBuilder.create();
                                confirmDialog.show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
