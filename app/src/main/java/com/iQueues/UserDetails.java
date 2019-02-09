package com.iQueues;


import java.util.ArrayList;

/**
 * The type User details.
 */
public class UserDetails {

    private String uid;
    private String phone_number;
    private String hat_number;
    private String company_name;
    private String attribute;
    private String name;

    private static UserDetails instance;

    private ArrayList<UserDetails> details = new ArrayList<>();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static UserDetails getInstance() {
        if (instance == null) {
            instance = new UserDetails();
        }
        return instance;
    }

    /**
     * Instantiates a new User details.
     *
     * @param uid          the uid
     * @param phone_number the phone number
     * @param hat_number   the hat number
     * @param company_name the company name
     * @param attribute    the attribute
     * @param name         the name
     * @param details      the details
     */
    public UserDetails(String uid, String phone_number, String hat_number, String company_name, String attribute, String name, ArrayList<UserDetails> details) {
        this.uid = uid;
        this.phone_number = phone_number;
        this.hat_number = hat_number;
        this.company_name = company_name;
        this.attribute = attribute;
        this.name = name;
        this.details = details;
    }

    private UserDetails() {
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets attribute.
     *
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Sets attribute.
     *
     * @param attribute the attribute
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Add user details.
     *
     * @param userDetails the user details
     * @return the user details
     */
    public UserDetails add(UserDetails userDetails) {
        this.details.add(userDetails);
        return this;
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
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * Sets phone number.
     *
     * @param phone_number the phone number
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * Gets hat number.
     *
     * @return the hat number
     */
    public String getHat_number() {
        return hat_number;
    }

    /**
     * Sets hat number.
     *
     * @param hat_number the hat number
     */
    public void setHat_number(String hat_number) {
        this.hat_number = hat_number;
    }

    /**
     * Gets company name.
     *
     * @return the company name
     */
    public String getCompany_name() {
        return company_name;
    }

    /**
     * Sets company name.
     *
     * @param company_name the company name
     */
    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    /**
     * Sets params.
     *
     * @param uid       the uid
     * @param uPhone    the u phone
     * @param uHat      the u hat
     * @param uCompany  the u company
     * @param uFullName the u full name
     * @param attribute the attribute
     */
    public void setParams(String uid, String uPhone, String uHat, String uCompany, String uFullName, String attribute) {
        this.setUid(uid);
        this.setPhone_number(uPhone);
        this.setHat_number(uHat);
        this.setCompany_name(uCompany);
        this.setName(uFullName);
        this.setAttribute(attribute);
    }

}

