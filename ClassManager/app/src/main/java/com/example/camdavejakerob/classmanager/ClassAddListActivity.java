package com.example.camdavejakerob.classmanager;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.camdavejakerob.classmanager.

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davey on 3/20/2018.
 */

public class ClassAddListActivity  extends AppCompatActivity {

    private FirebaseListAdapter<Class> adapter;
    private static final String TAG = ClassAddListActivity.class.getName();
    String courseID = "cids";
    private static final int CLASS_LOADER_ID = 1;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    DatabaseHelper database = new DatabaseHelper();
    //Query ref = FirebaseDatabase.getInstance().getReference(Messages).child(AllUsers);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_class_list);

        ListView availableClasses = (ListView)findViewById(R.id.classes_list);
        Query ref = FirebaseDatabase.getInstance().getReference(courseID);
        // Find a reference to the {@link ListView} in the layout
        final ListView itemListView = (ListView) findViewById(R.id.classes_list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        itemListView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(com.example.camdavejakerob.classmanager.R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(com.example.camdavejakerob.classmanager.R.string.no_internet_connection);
        }

        /********************************/
        /*FirebaseListOptions<Class> options =
                new FirebaseListOptions.Builder<Class>()
                        .setLayout(R.layout.class_item)
                        .setQuery(ref, Class.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseListAdapter<Class>(options) {
            @Override
            //populateView as alternative to getView
            protected void populateView(View v, Class model, int position) {
                // Get references to the views of message.xml
                Log.d(TAG, "here 4");
                TextView className = (TextView)v.findViewById(R.id.class_name);
                TextView classRoom = (TextView)v.findViewById(R.id.class_room);
                TextView classTime = (TextView)v.findViewById(R.id.class_time);
                TextView classId = (TextView)v.findViewById(R.id.class_id);
                Log.d(TAG, "here 3");

                // Set their text
                classId.setText(model.getCourseID());
                className.setText(model.getName());
                classRoom.setText(model.getRoom());
                classTime.setText(model.getClassTime());
            }
        };
*/
       database.updateListViewListOfClasses(this, availableClasses);

            Log.d(TAG, "here 1");
        //availableClasses.setAdapter(adapter);
        /***********************/

        // Setup the item click listener to bring up popupmenu
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Toast.makeText(ClassAddListActivity.this,
                        "You have chosen: ",
                        Toast.LENGTH_LONG)
                        .show();
                Log.d(TAG, "here 2");

                TextView classId = (TextView)view.findViewById(R.id.class_id);
                //get uid
                //under uids / child name (key) / child classes
                database.enrollStudentToClass(
                        FirebaseAuth.getInstance().getUid(),
                        classId.toString());
            }
        });
    }
}

