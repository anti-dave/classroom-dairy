package com.example.camdavejakerob.classmanager;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Jake on 2/11/2018.
 */

public class Class implements Parcelable{
    private String Name;
    private String DaysOfClass;
    private String StartTime;
    private String EndTime;
    private String Room;
    private String CourseID;
    private String Instructor;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

    public Class(){

    }

    public Class(String name,String daysOfClass, String startTime, String endTime, String room, String id){
        Name = name;
        DaysOfClass = daysOfClass;
        StartTime = startTime;
        EndTime = endTime;
        Room = room;
        CourseID = id;
        Instructor = "TEMP";
    }

    public String getName(){return Name;}
    public String getStartTime(){return StartTime;}
    public String getEndTime(){return EndTime;}
    public String getClassTime(){return getStartTime() + " - " + getEndTime();}
    public String getRoom(){return Room;}
    public String getSchiheduledTime(){return getDaysOfClass() + " " + getStartTime() + " - " + getEndTime();}
    public String getCourseID(){return CourseID;}
    public String getInstructor(){return Instructor;}
    public String getDaysOfClass(){return DaysOfClass;}

    // Parcelling part
    // for some help https://stackoverflow.com/questions/9406188/liststring-readstringarray-in-parcelable
    // http://www.vogella.com/tutorials/AndroidParcelable/article.html

    public Class(Parcel in){
        this.Name = in.readString();
        this.DaysOfClass = in.readString();
        //in.readStringList(DaysOfClass);
        this.StartTime =  in.readString();
        this.EndTime =  in.readString();
        this.Room =  in.readString();
        this.CourseID =in.readString();
        this.Instructor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.DaysOfClass);
        dest.writeString(this.StartTime);
        dest.writeString(this.EndTime);
        dest.writeString(this.Room);
        dest.writeString(this.CourseID);
        dest.writeString(this.Instructor);
    }

    @Override
    public String toString() {
        return "Class{" +
                "daysOfClass='" + Name + '\'' +
                ", name='" + DaysOfClass + '\'' +
                ", startTime='" + StartTime + '\'' +
                ", endTime='" + EndTime + '\'' +
                ", room='" + Room + '\'' +
                ", id='" + CourseID + '\'' +
                '}';
    }
}
