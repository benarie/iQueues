package com.iqueues;

public class Queue {

    private String date;
    private String time;
    private String isQueue;


    public Queue(String date, String time, String isQueue) {
        this.date = date;
        this.time = time;
        this.isQueue = isQueue;
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

    public String isQueue() {
        return isQueue;
    }

    public void setQueue(String queue) {
        isQueue = queue;
    }
}
