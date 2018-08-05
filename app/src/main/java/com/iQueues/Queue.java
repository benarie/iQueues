package com.iQueues;

public class Queue {

    private String date;
    private String time;
    private boolean status = false;



    public Queue() {}


    public Queue(String date, String time, boolean status) {
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
