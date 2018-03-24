package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    private ClassAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // some dummy data
        String[] today = {"Today"};
        String[] tomorrow = {"Tomorrow"};

        // Create a list of classes
        final ArrayList<Class> classes = new ArrayList<Class>();

        // add classes here yo
        classes.add(new Class("class1", today, "1pm","2pm","os101"));

        classes.add(new Class("class2", tomorrow, "11pm","2pm","os1021"));

        classes.add(new Class("class3", tomorrow, "12pm","2pm","os1031"));

        classes.add(new Class("class4", today, "13pm","2pm","os1041"));

        adapter = new ClassAdapter(this, classes);
        listView = (ListView) findViewById(R.id.classes);
        listView.setAdapter(adapter);

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