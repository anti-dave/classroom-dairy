package com.example.camdavejakerob.classmanager;

import android.content.Intent;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseListAdapter;

import java.util.ArrayList;


public class ClassActivity extends AppCompatActivity {

    private ClassAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // some dummy data
        String[] today = {"today"};
        String[] tomorrow = {"tomorrow"};

        // Create a list of classes
        final ArrayList<Class> classes = new ArrayList<Class>();

        // add classes here yo
        classes.add(new Class("class1", today, "1pm","2pm","os101", 0));

        classes.add(new Class("class2", tomorrow, "11pm","2pm","os1021", 0));

        classes.add(new Class("class3", tomorrow, "12pm","2pm","os1031", 0));

        classes.add(new Class("class4", today, "13pm","2pm","os1041", 0));

        //adapter = new ClassAdapter(this, classes);


        // gets ref to firebase
        //mRef = firebase.database().ref("u0/" + "classes");//= new Firebase("https://classmanager-38435.firebaseio.com/" + "u0/" + "classes");
        mListView = (ListView) findViewById(R.id.classes);
//        mAdapter = new FirebaseListAdapter<Class>(this, Class.class, R.layout.activity_class, mRef) {
//            @Override
//            protected void populateView(View view, Class myObj, int position) {
//                //Set the value for the views
//                ((TextView)view.findViewById(R.id.class_name)).setText(myObj.getName());
//                //...
//            }
//        };
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
}
