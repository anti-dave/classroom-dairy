package com.example.camdavejakerob.classmanager;


/**
 * Created by Jake on 2/17/2018.
 */

public class Teacher implements User {
    public String FirstName;
    public String LastName;
    public String Email;

    public String GetName(){return FirstName + " " + LastName;}
    public String GetEmail(){return Email;}
}
