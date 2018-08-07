package com.iQueues;

public class Queue {

    private String date;
    private String time;
    private String status = "false";

    public Queue() {
        //default cons.
    }

    public Queue(String date, String time, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
