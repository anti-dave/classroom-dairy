package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

    private String TAG = "RosterAdapter";

    public RosterAdapter(Context context, List<User> users){
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        User user = getItem(position);

        if(view == null){
            if(user.isInstructor()) {
                view = LayoutInflater.from(getContext()).inflate(
                        R.layout.roster_header,parent,false);
            } else {
                view = LayoutInflater.from(getContext()).inflate(
                        R.layout.roster_item,parent,false);
            }
        }

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText(user.getUserId());

        TextView name = (TextView) view.findViewById(R.id.roster_item_name);
        name.setText(user.getName());

        TextView chatId = (TextView) view.findViewById(R.id.chatId);
        chatId.setText(user.getChatId());

        Log.d(TAG, "chatId for" + user.getName() + " is " + user.getChatId());

        return view;
    }

}
