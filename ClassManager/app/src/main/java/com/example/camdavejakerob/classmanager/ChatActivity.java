package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.security.MessageDigestSpi;
import java.util.Random;

import static java.lang.Math.random;

/**
 * Created by Davey on 2/27/2018.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    private String ChatKeys = "Chat Keys";
    private String MessageRoom = "MessageRooms";
    private String Messages = "Messages";
    private String CIDS = "cids", UIDS = "uids";
    String AllUsers = "All Users";

    /** Content URI for the chats (null if it's a new chat) */
    private Uri mCurrentChatUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // See which chat this is & get End User from intent
        Intent intent = getIntent();
        mCurrentChatUri = intent.getData();
        final String chatId = intent.getStringExtra("chatId");
        final String endUser = intent.getStringExtra("recipientUid");

        Random rand = new Random();
        String key;

        DatabaseReference db = FirebaseDatabase
                .getInstance()
                .getReference();

        Log.d("hello", endUser);

        //Database references for user and recipient
        DatabaseReference user =
                db
                        .child(UIDS)
                        .child(FirebaseAuth.getInstance().getUid());

        DatabaseReference recipient =
                db
                        .child(UIDS)
                        .child(endUser);

        //may have to add a mechanism that checks for class discussion boards
        //Check if the recipients name is in the users message queue
        //if(user.child(Messages).child( recipient.child("name").toString() ) == null ) {
        if( chatId == null ) {

            key = rand.toString();

            //Push key into message queue of recipients chat
            user
                    .child(Messages)
                    .child( recipient.child("name").toString() )
                    .push()
                    .setValue(key);

            //Push key into message queue of users chat in recipients chat folder
            recipient
                    .child(Messages)
                    .child( user.child("name").toString() )
                    .push()
                    .setValue(key);

            displayChatMessages(key);
        } else {
            //Retrieve key from message queue with recipients name on it
            /*key = user
                    .child(Messages)
                    .child( recipient.child("name").toString() )
                    .toString();*/
            displayChatMessages(chatId);
        }

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.message_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference(MessageRoom)
                        .child(chatId)
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });
    }

    private void displayChatMessages(String chatId) {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

            Query ref =
                    FirebaseDatabase
                            .getInstance()
                            .getReference(MessageRoom)
                            .child(chatId);

            FirebaseListOptions<ChatMessage> options =
                    new FirebaseListOptions.Builder<ChatMessage>()
                            .setLayout(R.layout.chat_message)
                            .setQuery(ref, ChatMessage.class)
                            .setLifecycleOwner(this)
                            .build();

            adapter = new FirebaseListAdapter<ChatMessage>(options) {

            @Override
            //populateView as alternative to getView
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

}
