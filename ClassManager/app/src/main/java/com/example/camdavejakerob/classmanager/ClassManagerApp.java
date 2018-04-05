package com.example.camdavejakerob.classmanager;

import android.app.Application;

/**
 * Created by Rob on 4/5/2018.
 */

public class ClassManagerApp extends Application {
    private User curUser;

    public User getCurUser(){return curUser;}

    public void setCurUser(User user){curUser = user;}
}
