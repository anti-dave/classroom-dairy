package com.example.camdavejakerob.classmanager;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Rob on 3/13/2018.
 */


@IgnoreExtraProperties
public class UserInfo {

    public String first;
    public String last;
    public String email;
    boolean instructor;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserInfo(String first, String last, String email, boolean instructor) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.instructor = instructor;
    }

}
