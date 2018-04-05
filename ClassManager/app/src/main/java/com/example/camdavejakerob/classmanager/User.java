package com.example.camdavejakerob.classmanager;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Rob on 3/13/2018.
 */


@IgnoreExtraProperties
public class User {

    private String mName;
    private String mUserId;
    private boolean mInstructor;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        mName = "";
        mUserId = "";
        mInstructor = false;
    }

    public User(String userId, String name, Boolean instructor) {
        mUserId = userId;
        mName = name;
        mInstructor = instructor;
    }

    public String getName(){return mName;}
    public String getUserId(){return mUserId;}
    public Boolean isInstructor(){return mInstructor;}
    public void setName(String name){mName = name;}
    public void setUserId(String uid){mUserId = uid;}
    public void setInstructor(Boolean bool){mInstructor = bool;}

}
