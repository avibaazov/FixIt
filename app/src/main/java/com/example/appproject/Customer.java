package com.example.appproject;

public class Customer {
    String firstname;
    String lastname;
    String emailID;
    String phoneNum;
    String city;
    Double latitude;
    Double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }




public Customer() {
    }

    public Customer(String firstname, String lastname, String emailid, String phonenum, String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailID = emailid;
        this.phoneNum = phonenum;
        this.city = city;
    }
    public Customer(String firstname, String lastname, String emailid, String phonenum, String city,double latitude,double longitude) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailID = emailid;
        this.phoneNum = phonenum;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getCity() {
        return city;
    }


}