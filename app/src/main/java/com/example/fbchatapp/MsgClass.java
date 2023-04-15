package com.example.fbchatapp;

public class MsgClass {

    String message;
    String senderId;
    String time;
    String receiver;
    String key;
    boolean seen;

    public MsgClass(String message, String senderId, String time, String receiver, String key, boolean seen) {
        this.message = message;
        this.senderId = senderId;
        this.time = time;
        this.receiver = receiver;
        this.key = key;
        this.seen = seen;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

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


    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


}
