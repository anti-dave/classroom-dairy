package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.*;
import java.util.List;

/**
 * Created by Jake on 3/26/2018.
 */

public class AssignmentAdapter  extends ArrayAdapter<Assignment> {
    private static final String LOG_TAG = AssignmentAdapter.class.getName();

    public AssignmentAdapter(Context context, List<Assignment> assignments) {
        super(context, 0, assignments);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Assignment aAssignment = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.assignment_item, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.assignment_name);
        TextView dueDate = (TextView) view.findViewById(R.id.assignment_due_date);

        name.setText(aAssignment.getName());
        dueDate.setText("Due on: " + aAssignment.getDueDate());


        return view;
    }
}
