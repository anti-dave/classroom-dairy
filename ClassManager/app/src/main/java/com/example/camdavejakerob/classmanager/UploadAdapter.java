package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Rob on 4/4/2018.
 */

public class UploadAdapter extends ArrayAdapter<File> {

    public UploadAdapter(Context context, List<File> files) {super(context,0, files);}

    @Override
    public View getView(int position, View view, ViewGroup parent){

        File file = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_upload_file,parent,false);
        }

        TextView fileName = (TextView) view.findViewById(R.id.upload_file_item_name);
        TextView filePath = (TextView) view.findViewById(R.id.upload_file_item_path);
        fileName.setText(file.getName());
        filePath.setText(file.getAbsolutePath());

        return view;
    }

}
