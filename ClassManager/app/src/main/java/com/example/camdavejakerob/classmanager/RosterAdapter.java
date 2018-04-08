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

public class RosterAdapter extends ArrayAdapter<User> {

    public RosterAdapter(Context context, List<User> users){
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        User user = getItem(position);



        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.roster_item,parent,false);
        }

        TextView name = (TextView) view.findViewById(R.id.roster_item_name);
        name.setText(user.getName());

        return view;
    }

}
