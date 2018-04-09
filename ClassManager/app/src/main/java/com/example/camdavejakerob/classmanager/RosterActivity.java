package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Rob on 3/26/2018.
 */

public class RosterActivity extends AppCompatActivity{

    private String CLASS_ID = "CLASS_ID";
    private String classId;
    private ListView mListView;
    private DatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        classId = getIntent().getStringExtra(CLASS_ID);
        mListView = (ListView) findViewById(R.id.roster_students);
        mDatabase = new DatabaseHelper();
        mDatabase.getEnrolledStudents(this, mListView, classId);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Toast.makeText(RosterActivity.this,
                        "You have chosen: ",
                        Toast.LENGTH_LONG)
                        .show();

                TextView str = view.findViewById(R.id.roster_item_name);
                String userSelected = str.getText().toString();

                Log.d("hello", userSelected);
                // Create new intent to go to {@link ChatActivity}
                Intent intent = new Intent(RosterActivity.this, ChatActivity.class);

                intent.putExtra("recipientUid", userSelected);
                //intent.putExtra("chatId", rand);

                startActivity(intent);
            }
        });
    }
}
