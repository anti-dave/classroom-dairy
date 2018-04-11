package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.camdavejakerob.classmanager.

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Davey on 3/20/2018.
 */

public class ClassAddListActivity  extends AppCompatActivity {

    private FirebaseListAdapter<Class> adapter;
    private static final String TAG = ClassAddListActivity.class.getName();
    String courseID = "cids", UIDS = "uids";
    private static final int CLASS_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    DatabaseHelper database = new DatabaseHelper();

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

       database.updateListViewListOfClasses(this, availableClasses);

        // Setup the item click listener to bring up popupmenu
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Toast.makeText(ClassAddListActivity.this,
                        "You have chosen: ",
                        Toast.LENGTH_LONG)
                        .show();

                TextView classId = (TextView)view.findViewById(R.id.class_id);

                if(FirebaseDatabase.getInstance().getReference(UIDS)
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("intructor")
                        .equals(true)) {
                    database.enrollUserToClass(
                            classId.getText().toString(),
                            FirebaseAuth.getInstance().getUid());
                } else {
                    database.enrollUserToClass(
                            classId.getText().toString(),
                            FirebaseAuth.getInstance().getUid());

                    //Robs thing
                    /*FirebaseDatabase
                            .getInstance()
                            .getReference(courseID)
                            .child(newClassId)
                            .child("roster")
                            .child(currentUserId)
                            .setValue(false);*/
                }
            }
        });
    }
}

