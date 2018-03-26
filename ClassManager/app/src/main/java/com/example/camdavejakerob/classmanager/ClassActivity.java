package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.os.Parcelable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;


public class ClassActivity extends AppCompatActivity {

    private ListView mListView;
    private FirebaseUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        curUser = FirebaseAuth.getInstance().getCurrentUser();
        //This generates the list view of all classes the user is currently enrolled in.
        //for right now it is displaying classes that user u0 is enrolled in
        // in the future we will use FirebaseAuth to get current users id and then call method using that
        mListView = (ListView) findViewById(R.id.classes);
        DatabaseHelper database = new DatabaseHelper();
        database.updateListViewUserClasses(this, mListView, curUser.getUid());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Create new intent to go to {@link ClassInfoActivity}
                Intent classIntent = new Intent(ClassActivity.this, ClassInfoActivity.class);

                // using the position to get the name of the class clicked on
                Class currentClass = (Class)adapterView.getItemAtPosition(position);
                // then pass the class name to the class activity
                classIntent.putExtra("CURRENT_CLASS",(Parcelable) currentClass);

                // Launch the {@link ClassInfoActivity} to display the data for the current class.
                startActivity(classIntent);
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
        //if student do student
        getMenuInflater().inflate(R.menu.teacher_class_dropdown, menu);
        //if teacher do teacher
        getMenuInflater().inflate(R.menu.student_class_dropdown, menu);
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
            Intent addClassIntent = new Intent(ClassActivity.this, ClassAddListActivity.class);
            startActivity(addClassIntent); // probaly should pass the class to so we can change the title but this is just a dummy
        }
        if(item.getItemId() == R.id.action_create_class) {
            //create class action
            Toast.makeText(ClassActivity.this,
                    "Create Class",
                    Toast.LENGTH_LONG)
                    .show();
            Intent createClassIntent = new Intent(ClassActivity.this, ClassCreatorActivity.class);
            startActivity(createClassIntent); // probaly should pass the class to so we can change the title but this is just a dummy
        }
        if(item.getItemId() == R.id.action_remove_class) {
            //remove class action
            Toast.makeText(ClassActivity.this,
                    "Remove Class",
                    Toast.LENGTH_LONG)
                    .show();
            Intent removeClassIntent = new Intent(ClassActivity.this, ClassCreatorActivity.class);
            startActivity(removeClassIntent); // probaly should pass the class to so we can change the title but this is just a dummy
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
