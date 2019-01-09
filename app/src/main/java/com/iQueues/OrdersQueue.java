package com.iQueues;

import java.util.ArrayList;

import data.Globals;

public class OrdersQueue {

    private static OrdersQueue instance;

    private ArrayList<Order> orders = new ArrayList<>();

    public static OrdersQueue getInstance() {
        if (instance == null) instance = new OrdersQueue();
        return instance;
    }

    private OrdersQueue() {
    }

    public OrdersQueue add(Order order) {
        this.orders.add(order);
        return this;
    }

    public Order getFirst() {
        if (this.orders == null || this.orders.size() <= 0) return null;
        return this.orders.get(0);
    }

    public Order getLast() {
        if (this.orders == null || this.orders.size() <= 0) return null;
        return this.orders.get(this.orders.size() - 1);
    }

    public Order getActive() {
        for (Order order : orders)
            if (order.getStatus().equalsIgnoreCase(Globals.ACTIVE_ORDER_STATUS)) return order;
        return null;
    }

    public Order getInActive() {
        for (Order order : orders)
            if (order.getStatus().equalsIgnoreCase(Globals.INACTIVE_ORDER_STATUS)) return order;
        return null;
    }

    public String getDate() {
        for (Order order : orders)
            if (order.getDate() != null) return order.getDate();
        return null;
    }

    public String getTime() {
        for (Order order : orders)
            if (order.getTime() != null) return order.getTime();
        return null;
    }

    public String getStatus() {
        for (Order order : orders)
            if (order.getStatus() != null) return order.getStatus();
        return null;
    }

    public int getSize() {
        return this.orders.size();
    }

    public void updateOrder(String orderId, String date, String time) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setDate(date);
                order.setTime(time);
                break;
            }
        }
    }

    public void removeOrder(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                orders.remove(order);
                break;
            }
        }
    }
}
