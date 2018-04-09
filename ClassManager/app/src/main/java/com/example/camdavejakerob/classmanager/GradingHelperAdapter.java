package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rob on 4/9/2018.
 */

public class GradingHelperAdapter extends ArrayAdapter<GradingHelper>{

    public GradingHelperAdapter(Context context, List<GradingHelper> items){super(context,0,items);}

    @Override
    public View getView(int position, View view, ViewGroup parent){

        GradingHelper gradingHelper = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_grading_assignment,parent,false);
        }

        TextView studentName = (TextView) view.findViewById(R.id.grading_student_name);
        TextView submitted = (TextView) view.findViewById(R.id.grading_work_submitted);
        TextView studentId = (TextView) view.findViewById(R.id.grading_student_id);
        TextView filePath = (TextView) view.findViewById(R.id.grading_file_path);
        TextView grade = (TextView) view.findViewById(R.id.grading_grade);

        studentName.setText(gradingHelper.getStudentName());
        submitted.setText(gradingHelper.submittedWork().toString());
        studentId.setText(gradingHelper.getStudentId());
        filePath.setText(gradingHelper.getFilePath());
        grade.setText(gradingHelper.getGrade());

        return view;
    }
}
