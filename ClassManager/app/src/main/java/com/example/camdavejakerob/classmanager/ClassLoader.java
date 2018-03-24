package com.example.camdavejakerob.classmanager;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davey on 3/20/2018.
 */

/** Loads a list of classes' by using an AsyncTask to fetch from firebase*/
public class ClassLoader extends AsyncTaskLoader<List<Class>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ClassLoader.class.getName();

    /** Constructs a new {@link ClassLoader}.
     * @param context of the activity */
    public ClassLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /** This is on a background thread. */
    @Override
    public List<Class> loadInBackground() {

        // retrieve classes from firebase.
        //List<Class> classList = QueryUtils.fetchData();
        //return classList;
        // Create a list of classes

        // some dummy data
        ArrayList<String> today = new ArrayList<String>();
        today.add("today");

        ArrayList<String> tomorrow = new ArrayList<String>();
        tomorrow.add("Tomorrow");

        final List<Class> classes = new ArrayList<Class>();

        // add classes here yo
        classes.add(new Class("class1", today, "1pm","2pm","os101"));

        classes.add(new Class("class2", tomorrow, "11pm","2pm","os1021"));

        classes.add(new Class("class3", tomorrow, "12pm","2pm","os1031"));

        classes.add(new Class("class4", today, "13pm","2pm","os1041"));

        List<Class> classList = classes ;
        return classList;
    }
}
