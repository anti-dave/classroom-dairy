package com.example.camdavejakerob.classmanager;

import java.util.Date;

/**
 * Created by Davey on 4/9/2018.
 */

public class MessageHeading {

    private String chatName;

    public MessageHeading(){

    }

    public MessageHeading(String chatName) {
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

}
