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

public class RosterAdapter extends ArrayAdapter<UserInfo> {

    public RosterAdapter(Context context, List<UserInfo> users){
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        UserInfo user = getItem(position);


        //////////////////////////////// pick up here
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.roster_item,parent,false);
        }

        TextView name = (TextView) view.findViewById(R.id.roster_item_name);
        name.setText(user.first); // this is going to change when I change how UserInfo does things

        return view;
    }

}
