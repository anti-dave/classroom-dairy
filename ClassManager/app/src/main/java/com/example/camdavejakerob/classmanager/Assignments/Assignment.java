package com.example.camdavejakerob.classmanager.Assignments;

/**
 * Created by Rob on 3/15/2018.
 */

public class Assignment {
    private String DueDate;
    private String Grade;
    private String Name;

    public Assignment(){
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        DueDate = "";
        Grade = "";
        Name = "";
    }

    public Assignment(String dueDate, String grade, String name){
        DueDate = dueDate;
        Grade = grade;
        Name = name;
    }

    public String getDueDate(){return DueDate;}
    public String getGrade() {return Grade;}
    public String getName(){return Name;}


}
