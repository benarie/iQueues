package com.iQueues;

public class Queue {

    private String date;
    private String time;
    private boolean isQueue;
    private String noQueue;


    public Queue() {}

    public Queue(String noQueue) {
        this.noQueue = noQueue;
    }

    public Queue(String date, String time, boolean isQueue) {
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

    public boolean isQueue() {
        return isQueue;
    }

    public void setQueue(boolean queue) {
        isQueue = queue;
    }

    public String getNoQueue() {
        return noQueue;
    }

    public void setNoQueue(String noQueue) {
        this.noQueue = noQueue;
    }
}
