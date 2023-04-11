package com.example.fbchatapp;

public class MsgClass {

    String message;
    String senderId;
    String time;

    public MsgClass() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public MsgClass(String message, String senderId, String time) {
        this.message = message;
        this.senderId = senderId;
        this.time = time;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


}
