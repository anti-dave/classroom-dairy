package com.example.camdavejakerob.classmanager;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InfoActivity extends AppCompatActivity {

    String TAG = "INFO_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        
        File[] list = file.listFiles();
        
        for(int i = 0; i < list.length; i++){
            Log.d(TAG, "onCreate: " + list[i].getAbsolutePath());
        }
/*
        File[] list = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                Log.d(TAG, "accept: " + pathname.toString());
                return pathname.toString() == "Syllabus_Sample.pdf";
            }
        });
        */
/*
        if(list.length == 0){
            Log.d(TAG, "onCreate: list length was 0 :(");
        }
        */
/*
        for(int i = 0; i < list.length; i++) {
            Log.d("INFO_ACTIVITY", "onCreate: " + i + ": " + list[i].getName());
        }
        */


        /*
        Uri file = Uri.fromFile(new File("/storage/emulated/0/Download/Syllabus_Sample.pdf"));
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("testingDir/" + file.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata().getName());
            }
        });
        */

    }
}
