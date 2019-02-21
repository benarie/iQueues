package com.iQueues;

import java.util.ArrayList;

import data.Globals;

/**
 * The type Orders queue.
 */
public class OrdersQueue {

    private static OrdersQueue instance;

    private ArrayList<Order> orders = new ArrayList<>();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static OrdersQueue getInstance() {
        if (instance == null) instance = new OrdersQueue();
        return instance;
    }

    private OrdersQueue() {
    }

    /**
     * Add orders queue.
     *
     * @param order the order
     * @return the orders queue
     */
    public OrdersQueue add(Order order) {
        this.orders.add(order);
        return this;
    }

    /**
     * Gets first.
     *
     * @return the first order in the orders list
     */
    public Order getFirst() {
        if (this.orders == null || this.orders.size() <= 0) return null;
        return this.orders.get(0);
    }

    /**
     * Gets last.
     *
     * @return the last order in the orders list
     */
    public Order getLast() {
        if (this.orders == null || this.orders.size() <= 0) return null;
        return this.orders.get(this.orders.size() - 1);
    }

    /**
     * Gets active.
     *
     * @return the active order in the orders list
     */
    public Order getActive() {
        for (Order order : orders)
            if (order.getStatus().equalsIgnoreCase(Globals.ACTIVE_ORDER_STATUS)) return order;
        return null;
    }

    /**
     * Gets in active.
     *
     * @return the in active order in the orders list
     */
    public Order getInActive() {
        for (Order order : orders)
            if (order.getStatus().equalsIgnoreCase(Globals.INACTIVE_ORDER_STATUS)) return order;
        return null;
    }

    /**
     * Gets date.
     *
     * @return the date of order in the orders list
     */
    public String getDate() {
        for (Order order : orders)
            if (order.getDate() != null) return order.getDate();
        return null;
    }

    /**
     * Gets time.
     *
     * @return the time of order in the orders list
     */
    public String getTime() {
        for (Order order : orders)
            if (order.getTime() != null) return order.getTime();
        return null;
    }

    /**
     * Gets status.
     *
     * @return the status of order in the orders list
     */
    public String getStatus() {
        for (Order order : orders)
            if (order.getStatus() != null) return order.getStatus();
        return null;
    }


    /**
     * Gets size.
     *
     * @return the size of order list
     */
    public int getSize() {
        return this.orders.size();
    }

    /**
     * Update order.
     *
     * @param orderId the order id
     * @param date    the date
     * @param time    the time
     */
    public void updateOrder(String orderId, String date, String time) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setDate(date);
                order.setTime(time);
                break;
            }
        }
    }

    /**
     * Remove order.
     *
     * @param orderId the order id
     */
    public void removeOrder(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                orders.remove(order);
                break;
            }
        }
    }

    /**
     * Is there active boolean.
     *
     * @return the boolean
     */
    public boolean isThereActive() {
        return this.getActive() != null;
    }

    /**
     * Clear queue.
     */
    public void clearQueue() {
        this.orders = new ArrayList<>();
    }
}
