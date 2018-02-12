package com.example.camdavejakerob.classmanager;

/**
 * Created by Jake on 2/11/2018.
 */

public class Class {
    private String Name;
    private String DaysOfClass; // maybe change this to an array
    private String StartTime;
    private double Length;
    private String Room;

    public Class(String name,String daysOfClass, String startTime, double length, String room){
        Name = name;
        DaysOfClass = daysOfClass;
        StartTime = startTime;
        Length = length;
        Room = room;
    }

    public String getName(){return Name;}
    public String getDaysOfClass(){return DaysOfClass;}
    public String getStartTime(){return StartTime;}
    public double getLength(){return Length;}
    public String getRoom(){return Room;}

}
