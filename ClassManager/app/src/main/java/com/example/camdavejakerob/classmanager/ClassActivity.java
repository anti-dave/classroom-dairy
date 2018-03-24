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

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ClassActivity extends AppCompatActivity {

    private FirebaseListAdapter<Class> mAdapter;
    private ListView mListView;
    private DatabaseReference mRef;

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
        classes.add(new Class("class1", today, "1pm","2pm","os101", 0));

        classes.add(new Class("class2", tomorrow, "11pm","2pm","os1021", 0));

        classes.add(new Class("class3", tomorrow, "12pm","2pm","os1031", 0));

        classes.add(new Class("class4", today, "13pm","2pm","os1041", 0));

        //adapter = new ClassAdapter(this, classes);


        // gets ref to firebase
        mRef = FirebaseDatabase.getInstance().getReference();// new Firebase("https://classmanager-38435.firebaseio.com/" + "u0/" + "classes");
        mListView = (ListView) findViewById(R.id.classes);
        mAdapter = new FirebaseListAdapter<Class>(this, Class.class, R.layout.activity_class, mRef) {
            @Override
            protected void populateView(View view, Class myObj, int position) {
                //Set the value for the views
                ((TextView)view.findViewById(R.id.class_name)).setText(myObj.getName());
                //...
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                // Create new intent to go to {@link EditorActivity}
//                Intent intent = new Intent(VisitedRestaurantActivity.this, VisitedRestaurantFormActivity.class);
//
//                // Form the content URI that represents the specific Restaurant that was clicked on,
//                // by appending the "id" (passed as input to this method) onto the
//                // {@link VisitedRestaurantEntry#CONTENT_URI}.
//                Uri currentRestaurantUri =
//                        ContentUris.withAppendedId(VisitedRestaurantEntry.CONTENT_URI, id);
//
//                // Set the URI on the data field of the intent
//                intent.setData(currentRestaurantUri);
//
//                // Launch the {@link EditorActivity} to display the data for the current Restaurant.
//                startActivity(intent);
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
