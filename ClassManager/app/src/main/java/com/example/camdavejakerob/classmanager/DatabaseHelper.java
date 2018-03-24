package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rob on 3/13/2018.
 */

public class DatabaseHelper {

    //Strings used frequently for accessing data in Firebase
    private String CIDS = "cids", UIDS = "uids", CUR_UID = "curUid", CUR_CID = "curCid";
    private String ROSTER = "roster", DAYS = "daysOfClass", TIME_END = "endTime", TIME_START = "startTime", CLASS_NAME = "name", ROOM = "room";
    private String CLASSES = "classes", EMAIL = "email", FIRST = "first", INSTRUCTOR = "instructor", LAST = "last";
    private String ASSIGNMENTS = "assignments", DUE_DATE = "dueDate";
    private String TAG = "DATABASE_HELPER";

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;


    DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }


    /*****************************************************************************************************************
     *
     *                                  Accessors
     *
     ****************************************************************************************************************/

    /**
     *
     * @param path a string of the exact path to the desired information in the database.
     *             example: to get the user u1's first name the string needs to be "uids/u1/first"
     * @param textView a TextView object you wish to update
     */
    public void updateTextView(String path, final TextView textView){
        mDatabase.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
            }
        });
    }

    /**
     * updates a list view with all available classes in the database
     *
     * @param context
     * @param listView
     */
    public void updateListViewListOfClasses(final Context context, final ListView listView){
        mDatabase.getReference(CIDS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ClassAdapter classAdapter;
                final ArrayList<Class> classes = new ArrayList<Class>();
                ArrayList<String> days = new ArrayList<String>();

                for(DataSnapshot classSnapshot: dataSnapshot.getChildren()){

                    String name,startTime,endTime,room;
                    name=classSnapshot.child(CLASS_NAME).getValue().toString();
                    startTime=classSnapshot.child(TIME_START).getValue().toString();
                    endTime=classSnapshot.child(TIME_END).getValue().toString();
                    room=classSnapshot.child(ROOM).getValue().toString();

                    classes.add(new Class(name,days,startTime,endTime,room));
                }
                classAdapter = new ClassAdapter(context,classes);
                listView.setAdapter(classAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
            }
        });
    }

    /**
     *
     * @param context
     * @param listView
     * @param uid
     */
    public void updateListViewUserClasses(final Context context, final ListView listView, final String uid){
        mDatabase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ClassAdapter classAdapter;
                final ArrayList<Class> classes = new ArrayList<Class>();
                ArrayList<String> days = new ArrayList<String>();

                for(DataSnapshot classSnapshot: dataSnapshot.child(UIDS).child(uid).child(CLASSES).getChildren()){
                    DataSnapshot classData = dataSnapshot.child(CIDS).child(classSnapshot.getKey().toString());

                    String name,startTime,endTime,room;
                    name=classData.child(CLASS_NAME).getValue().toString();
                    startTime=classData.child(TIME_START).getValue().toString();
                    endTime=classData.child(TIME_END).getValue().toString();
                    room=classData.child(ROOM).getValue().toString();
                    classes.add(new Class(name,days,startTime,endTime,room));
                }
                classAdapter = new ClassAdapter(context,classes);
                listView.setAdapter(classAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
            }
        });
    }

    /*****************************************************************************************************************
     *
     *                                  Mutaters
     *
     ****************************************************************************************************************/

    /**
     * not sure how I want to implement this yet with grades and all that
     *
     * @param cid
     * @param assignment
     */
    public void writeAssignment(String cid, Assignment assignment){
        DatabaseReference classRef = mDatabase.getReference(CIDS).child(cid);
        classRef.child(ASSIGNMENTS).child(assignment.Name).setValue(assignment.DueDate);
    }

    /**
     * @param cid a string of the class number
     * @param uid a string of the user id
     *
     *  This method adds the user id to the roster list and the class id to the classes list of the user
     *
     */
    public void enrollStudentToClass(final String cid, final String uid){
        DatabaseReference classRef = mDatabase.getReference(CIDS).child(cid);
        DatabaseReference userRef = mDatabase.getReference(UIDS).child(uid);
        classRef.child(ROSTER).child(uid).setValue(true);
        userRef.child(CLASSES).child(cid).setValue(true);
    }

    /**
     * This method assumes all data was validated before its called
     *
     * @param name: a string of the classes title
     * @param daysOfClass: an array containing a list of strings for each day this class takes place
     * @param startTime: a string of the class start time. 12 hour format
     * @param endTime: a string of the class end time. 12 hour format
     * @param room: a string of which room the class takes place
     * @param enrolled: an integer of how many students are enrolled in the class
     *
     * This method creates a new class object and then adds it to the Firebase database with a new cid(class id)
     *   then increments the cid in the database for future classes.
     */
    public void writeNewClass(final String name, final ArrayList<String> daysOfClass, final String startTime, final String endTime, final String room){
        DatabaseReference cidRef = mDatabase.getReference().child(CUR_CID);

        cidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String cid = dataSnapshot.getValue().toString();

                if(cid.isEmpty()){
                    //do something
                    Log.d("WRITE_NEW_CLASS", "mCid was empty, sad face");
                } else {
                    Class newClass = new Class(name,daysOfClass,startTime,endTime,room);
                    DatabaseReference classRef = mDatabase.getReference(CIDS);
                    classRef.child(cid).setValue(newClass);

                    cid = incrementId(cid);
                    cid = "c" + cid;
                    mDatabase.getReference(CUR_CID).setValue(cid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("WRITE_NEW_CLASS", "onCancelled");
            }
        });
    }

    /**
     * This method assumes all data was validated before its called
     *
     * @param first: is a string of the users first name
     * @param last: is a string of the users last name
     * @param email: is a string of the users email address
     * @param instructor: bool if true give instructor privileges
     *
     * This method creates a new user object and then adds it to the Firebase database with a new uid(user id)
     *   then increments the uid in the database for future users.
     */
    public void writeNewUser( final String first, final String last, final String email, final boolean instructor) {

        DatabaseReference uidRef = mDatabase.getReference().child(CUR_UID);

        uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue().toString();

                if(uid.isEmpty()){

                    //something has gone wrong if uid is empty we should probably handle this in some way... later though
                    Log.d("ADD USER ERROR", "ClassCreatorActivity: could not get uid");

                } else {

                    UserInfo user = new UserInfo(first,last,email,instructor);
                    DatabaseReference userRef = mDatabase.getReference(UIDS);
                    userRef.child(uid).setValue(user);

                    uid = incrementId(uid);
                    uid = "u" + uid;
                    mDatabase.getReference(CUR_UID).setValue(uid);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("WRITE_NEW_USER", "onCancelled");
            }
        });
    }

    /**
     * @param id is a string representing either curUid or curCid
     * @return an incremented string, adding zeros if necessary. does not add the 'u' or 'c'
     */
    private String incrementId(String id){

        String newId = id.substring(1);

        int intId = Integer.parseInt(newId);
        intId++;
        newId = Integer.toString(intId);

        return newId;
    }
}
