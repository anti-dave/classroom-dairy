package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
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

    public MessageListAdapter(Context context, List<User> chats) {
        super(context, 0, chats);
    }

    private String TAG = "This Activity";

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        User chat = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_list_item, parent, false);
        }

        TextView chatId = (TextView) view.findViewById(R.id.chatId);
        chatId.setText(chat.getChatId());

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText(chat.getUserId());

        TextView name = (TextView) view.findViewById(R.id.chatName);
        name.setText(chat.getName());

        TextView lastMesageText = (TextView) view.findViewById(R.id.last_message);
        lastMesageText.setText(chat.getLastMessageText());

        TextView lastMesageTime = (TextView) view.findViewById(R.id.last_message_time);
        lastMesageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                Long.parseLong(chat.getLastMessageTime()) ));

        return view;
    }
}
