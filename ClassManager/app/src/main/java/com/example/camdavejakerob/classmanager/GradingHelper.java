package com.example.camdavejakerob.classmanager;

/**
 * Created by Rob on 4/7/2018.
 */

public class GradingHelper {
    private String mName;
    private String mUid;
    private String mFilePath;
    private String mGrade;
    private Boolean mWorkSubmitted;

    public GradingHelper(){
        mName = "";
        mUid = "";
        mFilePath = "";
        mGrade = "";
        mWorkSubmitted = false;
    }

    public GradingHelper(String name, String uid, String path, String grade, Boolean submitted){
        mName = name;
        mUid = uid;
        mFilePath = path;
        mGrade = grade;
        mWorkSubmitted = submitted;
    }

    public String getStudentName(){return mName;}
    public String getStudentId(){return mUid;}
    public String getFilePath(){return mFilePath;}
    public String getGrade(){return mGrade;}
    public Boolean submittedWork(){return mWorkSubmitted;}
}
