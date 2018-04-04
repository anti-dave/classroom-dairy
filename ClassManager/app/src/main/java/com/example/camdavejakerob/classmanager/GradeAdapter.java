package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rob on 3/26/2018.
 */

public class GradeAdapter extends ArrayAdapter<Assignment> {

    public GradeAdapter(Context context, List<Assignment> assignments){
        super(context, 0, assignments);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        Assignment assignment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.grade_item,parent,false);
        }

        TextView name = (TextView) view.findViewById(R.id.assignment_name);
        TextView grade = (TextView) view.findViewById(R.id.assignment_grade);

        name.setText(assignment.getName());
        grade.setText(assignment.getGrade());

        return view;
    }
}
