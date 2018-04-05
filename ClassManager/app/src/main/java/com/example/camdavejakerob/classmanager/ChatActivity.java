package com.example.camdavejakerob.classmanager;

import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.security.MessageDigestSpi;

/**
 * Created by Davey on 2/27/2018.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    //private ListAdapter adapter;
    String Messages = "Messages";
    String AllUsers = "All Users";

    //mAuth = FirebaseAuth.getInstance().getCurrentUser;
    //FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.message_fab);

        displayChatMessages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        //.getReference()
                        .getReference(Messages).child(AllUsers)
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

    } //onCreate



    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        //begin insert
            Query ref = FirebaseDatabase.getInstance().getReference(Messages).child(AllUsers);

            FirebaseListOptions<ChatMessage> options =
                    new FirebaseListOptions.Builder<ChatMessage>()
                            .setLayout(R.layout.chat_message)
                            .setQuery(ref, ChatMessage.class)
                            .setLifecycleOwner(this)
                            .build();

            adapter = new FirebaseListAdapter<ChatMessage>(options) {
        //end insert

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

        Log.d(TAG, "Right Before Setting List Adapter");
        listOfMessages.setAdapter(adapter);
    }

}// ChatActivity
