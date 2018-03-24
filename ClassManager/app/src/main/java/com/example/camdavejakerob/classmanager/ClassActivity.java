package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ClassActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        //This generates the list view of all classes the user is currently enrolled in.
        //for right now it is displaying classes that user u0 is enrolled in
        // in the future we will use FirebaseAuth to get current users id and then call method using that
        mListView = (ListView) findViewById(R.id.classes);
        DatabaseHelper database = new DatabaseHelper();
        database.updateListViewUserClasses(this,mListView,"u0");

        final TextView dummyButton = findViewById(R.id.dummy);
        dummyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent dummyIntent = new Intent(ClassActivity.this, ClassInfoActivity.class);
                startActivity(dummyIntent); // probaly should pass the class to so we can change the title but this is just a dummy

            }
        });

    }

    /**
     * Creates the menu with oncreate.
     *
     * @param menu
     * @return bool on whether it was successful.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_class_activity_dropdown, menu);
        return true;
    }//onCreateOptionsMenu

    /**
     * Logic for when menu is pressed.
     *
     * @param item
     * @return bool on whether it was successful.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_class) {
            //add class action
            Toast.makeText(ClassActivity.this,
                    "Add Class",
                    Toast.LENGTH_LONG)
                    .show();
            Intent addClassIntent = new Intent(ClassActivity.this, ClassCreatorActivity.class);
            startActivity(addClassIntent); // probaly should pass the class to so we can change the title but this is just a dummy

        }
        if(item.getItemId() == R.id.action_create_class) {
            //add class action
            Toast.makeText(ClassActivity.this,
                    "Created Class",
                    Toast.LENGTH_LONG)
                    .show();
            Intent createClassIntent = new Intent(ClassActivity.this, ClassCreatorActivity.class);
            startActivity(createClassIntent); // probaly should pass the class to so we can change the title but this is just a dummy

        }
        return true;
    }

}

/*
addClass(){
    if student(){
        addClassCourse()
    } //else, is teacher
    else {
        createClass();
    }
}

student(){
    return;
}

addClassCourse() {

}

createClass(){

}
*/
