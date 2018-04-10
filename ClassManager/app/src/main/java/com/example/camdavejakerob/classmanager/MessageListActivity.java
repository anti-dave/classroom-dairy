package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Davey on 4/8/2018.
 */

public class MessageListActivity extends AppCompatActivity {

    private static final String TAG = "MessageListActivity";
    String Messages = "Messages";
    String AllUsers = "All Users";
    String CIDS = "cids", UIDS = "uids";

    //private FirebaseListAdapter<MessageHeading> adapter;
    private FirebaseListAdapter<String> adapter;

    /** Content URI for the chats (null if it's a new chat) */
    private Uri currentMessageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // Examine the intent that was used to launch this activity
        Intent intent = getIntent();
        //mCurrentChatUri = intent.getData();
        final String cid = intent.getStringExtra("cid");

        ListView mListView = (ListView) findViewById(R.id.list_of_messages);
        DatabaseHelper mDatabase = new DatabaseHelper();
        mDatabase.getAllMessageRecipients(this, mListView, FirebaseAuth.getInstance().getUid().toString() );

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
            Toast.makeText(MessageListActivity.this,
                    "You have chosen: ",
                    Toast.LENGTH_LONG)
                    .show();

            TextView uid = view.findViewById(R.id.uid);
            String userSelectedId = uid.getText().toString();

            TextView name = view.findViewById(R.id.chatName);
            String userSelectedName = name.getText().toString();

            //TextView chatIdKey = view.findViewById(R.id.roster_item_key);
            //String chatId = chatIdKey.getText().toString();

            // Create new intent to go to {@link ChatActivity}
            Intent intent = new Intent(MessageListActivity.this, ChatActivity.class);
            //intent.putExtra("chatId", chatId);
            intent.putExtra("recipientUid", userSelectedId);
            intent.putExtra("recipientName", userSelectedName);
            startActivity(intent);
        }
    });
}
}
/*
    private void displayMessages() {

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_message_chats);

        final Query ref =
                FirebaseDatabase
                        .getInstance()
                        .getReference(UIDS)
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(Messages);

        FirebaseListOptions<String> options =
                new FirebaseListOptions.Builder<String>()
                        .setLayout(R.layout.message_list_item)
                        .setQuery(ref, String.class)
                        .setLifecycleOwner(this)
                        .build();

        //adapter = new FirebaseListAdapter<ChatMessage>(options) {

        //adapter = new FirebaseListAdapter<String>(
         //       this,
         //       String.class,
        //        R.layout.message_list_item,
         //       ref) {

        //}
            adapter = new FirebaseListAdapter<String>(options) {
                @Override
            //populateView as alternative to getView
            protected void populateView(View v, String model, int position) {
                // Get references to the views of message.xml
                TextView chatName = v.findViewById(R.id.chatName);
                //TextView lastMessage = (TextView)v.findViewById(R.id.last_message);
                //TextView lastMessageTime = (TextView)v.findViewById(R.id.last_message_time);

                // Set their text must make this come from database
                chatName.setText(model);
                //lastMessage.setText(model.getMessageUser());

                // Format the date before showing it
                //lastMessageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                //        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

        // Find a reference to the {@link ListView} in the layout
        final ListView itemListView = (ListView) findViewById(R.id.list_of_message_chats);

        /** TextView that is displayed when the list is empty
         * make it such that it says no messages found*/


  /*
        TextView mEmptyStateTextView;
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        itemListView.setEmptyView(mEmptyStateTextView);

        // Setup the item click listener to bring up popupmenu
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Toast.makeText(MessageListActivity.this,
                        "You have chosen: ",
                        Toast.LENGTH_LONG)
                        .show();

                TextView chatNameTextView = (TextView) view.findViewById(R.id.chatName);
                String chatName = chatNameTextView.getText().toString();

                // Create new intent to go to {@link ChatActivity}
                Intent intent = new Intent(MessageListActivity.this, ChatActivity.class);

                // Set the URI on the data field of the intent
                intent.setData(currentMessageUri);
                intent.putExtra("chatName", chatName);
            }
        });
    }

}*/

