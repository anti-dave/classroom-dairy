package com.example.camdavejakerob.classmanager;

import java.util.ArrayList;

/**
 * Created by Jake on 2/11/2018.
 */

public class Class {
    private String Name;
    private ArrayList<String> DaysOfClass;
    private String StartTime;
    private String EndTime;
    private String Room;
    //private String Instructor; i want to add this but not going to yet because to lazy to fix this

    public Class(String name,ArrayList<String> daysOfClass, String startTime, String endTime, String room){
        Name = name;
        DaysOfClass = daysOfClass;
        StartTime = startTime;
        EndTime = endTime;
        Room = room;
    }

    public String getName(){return Name;}
    public String getStartTime(){return StartTime;}
    public String getEndTime(){return EndTime;}
    public String getRoom(){return Room;}
    public String GetSchiheduledTime(){return getDaysOfClass() + " " + getStartTime() + " - " + getEndTime();}
    public String getDaysOfClass(){
        String days = "";

        for(int i = 0; i < DaysOfClass.size(); i++){
            days += DaysOfClass.get(i) + " ";
        }

        return days;
    }
}
