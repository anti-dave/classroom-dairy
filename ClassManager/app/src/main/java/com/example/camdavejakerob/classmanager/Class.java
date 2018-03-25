package com.example.camdavejakerob.classmanager;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Jake on 2/11/2018.
 */

public class Class implements Parcelable{
    private String Name;
    private ArrayList<String> DaysOfClass;
    private String StartTime;
    private String EndTime;
    private String Room;
    //private String Instructor; i want to add this but not going to yet because to lazy to fix this

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

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

    // Parcelling part
    // for some help https://stackoverflow.com/questions/9406188/liststring-readstringarray-in-parcelable
    // http://www.vogella.com/tutorials/AndroidParcelable/article.html

    public Class(Parcel in){
        this.Name = in.readString();
        this.DaysOfClass = in.createStringArrayList();
        //in.readStringList(DaysOfClass);
        this.StartTime =  in.readString();
        this.EndTime =  in.readString();
        this.Room =  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeList(this.DaysOfClass);
        dest.writeString(this.StartTime);
        dest.writeString(this.EndTime);
        dest.writeString(this.Room);
    }

    @Override
    public String toString() {
        return "Class{" +
                "daysOfClass='" + Name + '\'' +
                ", name='" + DaysOfClass + '\'' +
                ", startTime='" + StartTime + '\'' +
                ", endTime='" + EndTime + '\'' +
                ", room='" + Room + '\'' +
                '}';
    }
}
