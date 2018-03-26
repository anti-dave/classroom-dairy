package com.example.camdavejakerob.classmanager;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.camdavejakerob.classmanager.Assignments.Assignment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Rob on 3/13/2018.
 */

public class DatabaseHelper {

    //Strings used frequently for accessing data in Firebase
    private String CIDS = "cids", UIDS = "uids";
    private String ROSTER = "roster", DAYS = "daysOfClass", TIME_END = "endTime", TIME_START = "startTime", CLASS_NAME = "name", ROOM = "room";
    private String CLASSES = "classes", USER_NAME = "name", INSTRUCTOR = "instructor";
    private String ASSIGNMENTS = "assignments", DUE_DATE = "dueDate", GRADES = "grades";
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
     * writes a class to the database with its name and due date
     *
     * @param cid a string representation of the cass id. This method assumes its a valid class id
     * @param assignment an assignment class
     */
    public void writeAssignment(String cid, Assignment assignment){
        DatabaseReference classRef = mDatabase.getReference(CIDS).child(cid);
        classRef.child(ASSIGNMENTS).child(assignment.getName()).child(DUE_DATE).setValue(assignment.getDueDate());
    }

    /**
     * Adds a grade for a specified student in the given class on a given assignment
     *
     * @param cid String of the class id, assumed to be valid
     * @param uid String of the students id, assumed to be valid
     * @param grade String of the grade which the student received for the assignment
     * @param assignment String of the assignments name
     */
    public void writeAssignmentGrade(String cid, String uid, String grade, String assignment){
        DatabaseReference classRef = mDatabase.getReference(CIDS).child(cid);
        classRef.child(ASSIGNMENTS).child(assignment).child(GRADES).child(uid).setValue(grade);
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
     *
     * This method creates a new class object and then adds it to the Firebase database
     */
    public void writeNewClass( final String classId, final String name, final ArrayList<String> daysOfClass,
                               final String startTime, final String endTime, final String room ){

        Class newClass = new Class(name,daysOfClass,startTime,endTime,room);
        mDatabase.getReference(CIDS).child(classId).setValue(newClass);

    }

    /**
     * This method assumes all data was validated before its called
     *
     * @param name: is a string of the users first and last name
     * @param userId: is a string generated by FirebaseAuth
     *
     * This method creates a new user object and then adds it to the Firebase database with a new uid(user id)
     *   then increments the uid in the database for future users.
     */
    public void writeNewUser( final String name, final String userId ) {

        mDatabase.getReference(UIDS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(userId).getValue() == null) {
                    mDatabase.getReference(UIDS).child(userId).child(USER_NAME).setValue(name);

                    //a method after this one prompts user and updates the instructor value
                    mDatabase.getReference(UIDS).child(userId).child(INSTRUCTOR).setValue(false);

                    //Intent prompt = new Intent(context,InstructorPromptActivity.class);
                    //startActivity(prompt);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
            }
        });
    }

    public void updateUserToInstructor(String uid, boolean bool){
        mDatabase.getReference(UIDS).child(uid).child(INSTRUCTOR).setValue(bool);
    }
}
