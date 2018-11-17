package com.iQueues;


import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class UserDetails {

    private String uid;
    private String phone_number;
    private String hat_number;
    private String company_name;

    private static UserDetails instance;

    private ArrayList<UserDetails> details = new ArrayList<>();

    public static UserDetails getInstance() {
        if (instance == null) {
            instance = new UserDetails();
        }
        return instance;
    }

    private UserDetails() {
    }

    public UserDetails add(UserDetails userDetails) {
        this.details.add(userDetails);
        return this;
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

    public void setParams(String uid, String uPhone, String uHat, String uCompany) {
        this.setUid(uid);
        this.setPhone_number(uPhone);
        this.setHat_number(uHat);
        this.setCompany_name(uCompany);
    }

}

