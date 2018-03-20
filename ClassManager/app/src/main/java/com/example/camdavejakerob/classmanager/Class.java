package com.example.camdavejakerob.classmanager;

/**
 * Created by Jake on 2/11/2018.
 */

public class Class {
    private String mName;
    private String[] mDaysOfClass; // maybe change this to an array
    private String mStartTime;
    private String mEndTime;
    private String mRoom;
    private int mEnrolled;

    public Class(String name,String[] daysOfClass, String startTime, String endTime, String room, int enrolled){
        mName = name;
        mDaysOfClass = daysOfClass;
        mStartTime = startTime;
        mEndTime = endTime;
        mRoom = room;
        mEnrolled = enrolled;
    }

    public String getName(){return mName;}
    public String getStartTime(){return mStartTime;}
    public String getEndTime(){return mEndTime;}
    public String getRoom(){return mRoom;}
    public int getEnrolled(){return mEnrolled;}
    public String GetSchiheduledTime(){return getDaysOfClass() + " " + getStartTime() + " - " + getEndTime();}
    public String getDaysOfClass(){
        String days = "";

        for(int i = 0; i < mDaysOfClass.length; i++){
            days += mDaysOfClass[i] + " ";
        }

        return days;
    }
}
