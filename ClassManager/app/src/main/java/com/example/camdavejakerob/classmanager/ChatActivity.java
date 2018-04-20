package com.example.camdavejakerob.classmanager;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;


/**
 * Created by Davey on 2/27/2018.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private FirebaseListAdapter<ChatMessage> adapter;
    private String ChatKeys = "Chat Keys";
    private String MessageRoom = "MessageRooms";
    private String Messages = "Messages";
    private String CIDS = "cids", UIDS = "uids";
    private String key = "";
    private Boolean hasChatted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        final String chatId = intent.getStringExtra("chatId");
        final String endUserId = intent.getStringExtra("recipientUid");
        final String endUserName = intent.getStringExtra("recipientName");

        DatabaseReference db = FirebaseDatabase
                .getInstance()
                .getReference();

        //Database references for user and recipient
        DatabaseReference user =
                db
                        .child(UIDS)
                        .child(FirebaseAuth.getInstance().getUid());

        DatabaseReference recipient =
                db
                        .child(UIDS)
                        .child(endUserId);

        //Check if the recipient has been messaged prior
        if( chatId == null || chatId.isEmpty() ) {

            Integer rand = new Random().nextInt();
            key = rand.toString();

            //Push key into message queue of recipients chat
            user
                    .child(Messages)
                    .child( endUserId )
                    .setValue(key);

            //Push key into message queue of users chat in recipients chat folder
            recipient
                    .child(Messages)
                    .child(
                            FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid())
                    .setValue(key);

            displayChatMessages(key);
        } else {
            key = chatId;
            //Retrieve key from message queue with recipients name on it
            displayChatMessages(key);
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
                        .child(key)
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

        /*Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(this, upIntent);
        }*/
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
