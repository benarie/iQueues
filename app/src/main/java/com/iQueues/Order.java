package com.iQueues;

public class Order {

    private String date;
    private String time;
    private String orderId;
    private String uid;


    public Order() {
        //default cons.
    }

    public Order(String date, String time, String orderId, String uid) {
        this.date = date;
        this.time = time;
        this.orderId = orderId;
        this.uid = uid;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
