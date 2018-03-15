package com.example.camdavejakerob.classmanager;

/**
 * Created by Jake on 2/11/2018.
 */

public class Class {
    private String Name;
    private String[] DaysOfClass;
    private String StartTime;
    private String EndTime;
    private String Room;
    //private String Instructor; i want to add this but not going to yet because to lazy to fix this
    private int Enrolled;

    public Class(String name,String[] daysOfClass, String startTime, String endTime, String room, int enrolled){
        Name = name;
        DaysOfClass = daysOfClass;
        StartTime = startTime;
        EndTime = endTime;
        Room = room;
        Enrolled = enrolled;
    }

    public String getName(){return Name;}
    public String getStartTime(){return StartTime;}
    public String getEndTime(){return EndTime;}
    public String getRoom(){return Room;}
    public int getEnrolled(){return Enrolled;}
    public String GetSchiheduledTime(){return getDaysOfClass() + " " + getStartTime() + " - " + getEndTime();}
    public String getDaysOfClass(){
        String days = "";

        for(int i = 0; i < DaysOfClass.length; i++){
            days += DaysOfClass[i] + " ";
        }

        return days;
    }
}
