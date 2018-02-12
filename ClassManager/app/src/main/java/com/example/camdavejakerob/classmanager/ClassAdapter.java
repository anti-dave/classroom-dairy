package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jake on 2/11/2018.
 */

public class ClassAdapter extends ArrayAdapter<Class> {

    private static final String LOG_TAG = ClassAdapter.class.getName();

    public ClassAdapter(Context context, List<Class> classes) {
        super(context, 0, classes);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Class aClass = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.class_item, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.class_name);
        TextView day = (TextView) view.findViewById(R.id.class_day);
        TextView time = (TextView) view.findViewById(R.id.class_time);
        TextView room = (TextView) view.findViewById(R.id.class_room);

        name.setText(aClass.getName());
        day.setText(aClass.getDaysOfClass());
        time.setText(aClass.getStartTime());
        room.setText(aClass.getRoom());


        return view;
    }
}
