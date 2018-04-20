package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Davey on 4/9/2018.
 */

public class DiscussionBoardActivity extends AppCompatActivity {

    private static final String TAG = "DiscussionBoardActivity";
    private int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    String Messages = "Messages";
    String DiscussionBoards = "Discussion Boards";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // See which chat this is & get End User from intent
        Intent intent = getIntent();
        final String classId = intent.getStringExtra("cid");

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.message_fab);

        displayChatMessages(classId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                String inputText = input.getText().toString();
                String userName = FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getDisplayName();

                final User user = ((ClassManagerApp) DiscussionBoardActivity.this.getApplication()).getCurUser();

                //if instructor do instructor
                if( user.isInstructor() ) {
                    userName += " (Instructor)";
                }

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference(DiscussionBoards)
                        .child(classId)
                        .push()
                        .setValue(new ChatMessage(inputText, userName)
                        );

                // Clear the input
                input.setText("");
            }
        });

    } //onCreate


    private void displayChatMessages(String classId) {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        Query ref = FirebaseDatabase.getInstance().getReference(DiscussionBoards).child(classId);

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

        Log.d(TAG, "Right Before Setting List Adapter");
        listOfMessages.setAdapter(adapter);
    }

}// ChatActivity