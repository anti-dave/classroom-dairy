package com.example.camdavejakerob.classmanager;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
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

import java.io.Serializable;
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

        database.updateListViewUserClasses(this,mListView, FirebaseAuth.getInstance().getUid());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
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
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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
