package com.example.camdavejakerob.classmanager;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Rob on 3/13/2018.
 */


@IgnoreExtraProperties
public class User {

    private String mName;
    private String mUserId;
    private boolean mInstructor;
    private String mChatId;
    private String mLastMessageText;
    private String mLastMessageTime;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        mName = "";
        mUserId = "";
        mInstructor = false;
        mChatId = "";
        mLastMessageText = "";
        mLastMessageTime = "";
    }

    public User(String recipientUserId, String recipientName, String chatId, String lastMessage, String lastMessageTime) {
        mName = recipientName;
        mUserId = recipientUserId;
        mChatId = chatId;
        mLastMessageText = lastMessage;
        mLastMessageTime = lastMessageTime;
    }

    public User(String userId, String name, Boolean instructor) {
        mUserId = userId;
        mName = name;
        mInstructor = instructor;
    }

    public User(String userId, String name, Boolean instructor,  String chatId, String lastMessage, String lastMessageTime) {
        mUserId = userId;
        mName = name;
        mInstructor = instructor;
        mChatId = chatId;
        mLastMessageText = lastMessage;
        mLastMessageTime = lastMessageTime;
    }

    public String getName(){return mName;}
    public String getUserId(){return mUserId;}
    public String getChatId(){return mChatId;}
    public String getLastMessageText(){return mLastMessageText;}
    public String getLastMessageTime(){return mLastMessageTime;}
    public Boolean isInstructor(){return mInstructor;}
    public void setName(String name){mName = name;}
    public void setUserId(String uid){mUserId = uid;}
    public void setInstructor(Boolean bool){mInstructor = bool;}

}
