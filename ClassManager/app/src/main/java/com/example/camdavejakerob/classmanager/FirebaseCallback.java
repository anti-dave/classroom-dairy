package com.example.camdavejakerob.classmanager;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Rob on 3/15/2018.
 */

public interface FirebaseCallback {
    void onCallback(DataSnapshot dataSnapshot);
}
