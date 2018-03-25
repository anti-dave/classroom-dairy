package com.example.camdavejakerob.classmanager;

/**
 * Created by Rob on 3/15/2018.
 */

public class Assignment {
    public String DueDate;
    public String Grade;
    public String Name;

    public Assignment(){
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Assignment(String dueDate, String grade, String name){
        DueDate = dueDate;
        Grade = grade;
        Name = name;
    }
}
