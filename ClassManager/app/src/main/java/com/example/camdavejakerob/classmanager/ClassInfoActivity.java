package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class ClassInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        GridView gridview = (GridView) findViewById(R.id.main_menu);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ClassInfoActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button ChatButton = findViewById(R.id.chat_button);

        ChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ChatIntent = new Intent(ClassInfoActivity.this, ChatActivity.class);
                startActivity(ChatIntent);
            }
        });

    }
}
