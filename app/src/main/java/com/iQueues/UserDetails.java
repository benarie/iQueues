package com.iQueues;


public class UserDetails {

    private String full_Name;
    private String phone_number;
    private String hat_number;
    private String company_name;

    public UserDetails(String full_Name, String phone_number, String hat_number, String companyName) {
        this.full_Name = full_Name;
        this.phone_number = phone_number;
        this.hat_number = hat_number;
        this.company_name = companyName;
    }

    public  UserDetails(){


    }

    public String getfull_Name() {
        return full_Name;
    }

    public void setName(String full_Name) {
        this.full_Name = full_Name;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String companyName) {
        this.company_name = companyName;
    }
}

