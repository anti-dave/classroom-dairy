package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Davey on 4/10/2018.
 */

public class MessageListAdapter extends ArrayAdapter<User> {

    public MessageListAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        User user = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_list_item, parent, false);
        }

        DatabaseHelper mDatabase = new DatabaseHelper();

        //TextView chatId = (TextView) view.findViewById(R.id.chatId);

        /*uid.setText(
                mDatabase.getChatKeyFromUid(
                        MessageListAdapter,
                        user.getUserId().toString() ) );*/

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText(user.getUserId());

        TextView name = (TextView) view.findViewById(R.id.chatName);
        name.setText(user.getName());

        return view;
    }
}
