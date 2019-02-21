package com.iQueues;

/**
 * The type Order.
 */
public class Order {

    private String orderId;
    private String date;
    private String time;
    private String uid;
    private String status;

    /**
     * Instantiates a new Order.
     */
    public Order() {
        //default cons.
   }

    /**
     * Instantiates a new Order.
     *
     * @param orderId the order id
     * @param date    the date
     * @param time    the time
     * @param uid     the uid
     * @param status  the status
     */
    public Order(String orderId, String date, String time, String uid, String status) {
        this.date = date;
        this.time = time;
        this.orderId = orderId;
        this.uid = uid;
        this.status = status;
    }

    /**
     * Gets date.
     *
     * @return the date of order
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date of order
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets time.
     *
     * @return the time of order
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time of order
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets order id.
     *
     * @return the order id
     */
    public String getOrderId() { return orderId; }

    /**
     * Sets order id.
     *
     * @param orderId the order id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets status.
     *
     * @return the status of order
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status of order
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
