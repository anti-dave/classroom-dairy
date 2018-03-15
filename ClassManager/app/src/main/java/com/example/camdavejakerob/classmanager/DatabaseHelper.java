package com.example.camdavejakerob.classmanager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rob on 3/13/2018.
 */

public class DatabaseHelper {

    private FirebaseDatabase mDatabase;
    private String mUid;
    private String mCid;

    DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mUid = "";
        mCid = "";
    }

    /**
     *
     * @param cid a string of the class number
     * @param uid a string of the user id
     *
     *  This method gets the DataSnapshot for the user id then calls writeStudentToClass
     */
    public void addStudentToClass(final String cid, final String uid){

        DatabaseReference uidRef = mDatabase.getReference("uids").child(uid);

        uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //DatabaseReference cidRef = mDatabase.getReference("cids").child(cid);

                //cidRef.child("roster").child(cid).child(uid).setValue()
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ADD_STUDENT_TO_CLASS", "onCancelled");
            }
        });
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
    public void writeNewClass(final String name, final String[] daysOfClass, final String startTime, final String endTime, final String room, final int enrolled){
        DatabaseReference cidRef = mDatabase.getReference().child("curCid");

        cidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCid = dataSnapshot.getValue().toString();

                if(mCid.isEmpty()){
                    //do something
                    Log.d("WRITE_NEW_CLASS", "mCid was empty, sad face");
                } else {
                    Class newClass = new Class(name,daysOfClass,startTime,endTime,room,enrolled);
                    DatabaseReference classRef = mDatabase.getReference("cids");
                    classRef.child(mCid).setValue(newClass);

                    mCid = incrementId(mCid);
                    mCid = "c" + mCid;
                    mDatabase.getReference("curCid").setValue(mCid);
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

        DatabaseReference uidRef = mDatabase.getReference().child("curUid");

        uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUid = dataSnapshot.getValue().toString();

                if(mUid.isEmpty()){

                    //something has gone wrong if uid is empty we should probably handle this in some way... later though
                    Log.d("ADD USER ERROR", "writeNewUser: could not get uid");

                } else {

                    UserInfo user = new UserInfo(first,last,email,instructor);
                    DatabaseReference userRef = mDatabase.getReference("uids");
                    userRef.child(mUid).setValue(user);

                    mUid = incrementId(mUid);
                    mUid = "u" + mUid;
                    mDatabase.getReference("curUid").setValue(mUid);

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
