package com.iQueues;


public class UserDetails {

    private String full_name;
    private String phone_number;
    private String hat_number;
    private String company_name;


    public  UserDetails(){}

    public UserDetails(String full_name, String phone_number, String hat_number, String company_name) {
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.hat_number = hat_number;
        this.company_name = company_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
}

