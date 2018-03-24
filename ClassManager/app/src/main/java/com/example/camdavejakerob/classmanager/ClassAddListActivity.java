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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davey on 3/20/2018.
 */

public class ClassAddListActivity  extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Class>> {

    private static final String LOG_TAG = ClassAddListActivity.class.getName();

    /** * Adapter for the list of Classes */
    private ClassAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** Content URI for the existing class (null if it's a new class) */
    private Uri mCurrentClassUri;

    /** Constant value for the Class loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders. */
    private static final int CLASS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_class_list);

        // Find a reference to the {@link ListView} in the layout
        final ListView itemListView = (ListView) findViewById(R.id.classes_list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        itemListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of classes as input
        mAdapter = new ClassAdapter(this, new ArrayList<Class>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        itemListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        getLoaderManager().restartLoader(CLASS_LOADER_ID, null, ClassAddListActivity.this );

        // Setup the item click listener to bring up popupmenu
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Toast.makeText(ClassAddListActivity.this,
                        "You have chosen: ",
                        Toast.LENGTH_LONG)
                        .show();
            }
        });

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CLASS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(com.example.camdavejakerob.classmanager.R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(com.example.camdavejakerob.classmanager.R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Class>> onCreateLoader(int i, Bundle bundle) {
        return new ClassLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Class>> loader, List<Class> classList) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(com.example.camdavejakerob.classmanager.R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No Classes found."
        mEmptyStateTextView.setText(com.example.camdavejakerob.classmanager.R.string.no_classes);

        // Clear the adapter of previous class data
        mAdapter.clear();

        // If there is a valid list of {@link Classes}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (classList != null && !classList.isEmpty()) {
            mAdapter.addAll(classList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Class>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}

