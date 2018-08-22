package com.iQueues;




import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class UserDetails {

    private String uid;
    private String phone_number;
    private String hat_number;
    private String company_name;
    private ArrayList <String> idQueue;

    @Exclude private transient ArrayList <Order> orders;



    public  UserDetails(){}

    public UserDetails(String uid, String phone_number, String hat_number, String company_name, ArrayList<String> idQueue) {
        this.uid = uid;
        this.phone_number = phone_number;
        this.hat_number = hat_number;
        this.company_name = company_name;
        this.idQueue = idQueue;
        this.orders = orders;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHat_number() {
        return hat_number;
    }

    public void setHat_number(String hat_number) {
        this.hat_number = hat_number;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public ArrayList<String> getIdQueue() {
        return idQueue;
    }

    public void setIdQueue(ArrayList<String> idQueue) {
        this.idQueue = idQueue;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}

