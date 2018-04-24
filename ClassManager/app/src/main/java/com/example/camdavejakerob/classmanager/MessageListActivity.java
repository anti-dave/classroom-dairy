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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ListView mListView = (ListView) findViewById(R.id.list_of_messages);
        DatabaseHelper mDatabase = new DatabaseHelper();
        mDatabase.getAllChats(this, mListView, FirebaseAuth.getInstance().getUid().toString() );

        // Hide loading indicator once chats recieved
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {

            TextView uid = view.findViewById(R.id.uid);
            String userSelectedId = uid.getText().toString();

            TextView name = view.findViewById(R.id.chatName);
            String userSelectedName = name.getText().toString();

            TextView chatIdKey = view.findViewById(R.id.chatId);
            String chatId = chatIdKey.getText().toString();

            // Create new intent to go to {@link ChatActivity}
            Intent intent = new Intent(MessageListActivity.this, ChatActivity.class);
            intent.putExtra("chatId", chatId);
            intent.putExtra("recipientUid", userSelectedId);
            intent.putExtra("recipientName", userSelectedName);
            startActivity(intent);
        }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        ListView mListView = (ListView) findViewById(R.id.list_of_messages);
        DatabaseHelper mDatabase = new DatabaseHelper();
        mDatabase.getAllChats(this, mListView, FirebaseAuth.getInstance().getUid().toString() );

        // Hide loading indicator once chats recieved
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

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

                TextView chatIdKey = view.findViewById(R.id.chatId);
                String chatId = chatIdKey.getText().toString();

                // Create new intent to go to {@link ChatActivity}
                Intent intent = new Intent(MessageListActivity.this, ChatActivity.class);
                intent.putExtra("chatId", chatId);
                intent.putExtra("recipientUid", userSelectedId);
                intent.putExtra("recipientName", userSelectedName);
                startActivity(intent);
            }
        });
    }
}