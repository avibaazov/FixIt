package com.example.appproject;

public class ServiceProvider {
    String firstname;
    String lastname;
    String emailID;
    String phoneNum;
    String city;
    String category;
    Float yearsOfExperience;
    String qualifications;
    Double latitude;
    Double longitude;
    //String profilePicUrl;
    String idProofUrl;
    Boolean pending;
    String id;

    int totalRate;
    int numRate;
    float avRate;
    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public ServiceProvider(String firstname, String lastname, String emailID, String phoneNum, String city, String category) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailID = emailID;
        this.phoneNum = phoneNum;
        this.city = city;
        this.category = category;
//        this.numRate =0;
//        this.totalRate=0;
    }
    public ServiceProvider(){
        //empty constructor needed for firebase database
    }

    /*public ServiceProvider(String firstname, String lastname, String emailID, String phoneNum, String city, String category,String profilePicUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailID = emailID;
        this.phoneNum = phoneNum;
        this.city = city;
        this.category = category;
        this.profilePicUrl = profilePicUrl;
        this.numRate = 0;
        this.totalRate =0;
    }*/



    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailid() {
        return emailID;
    }

    public String getPhonenum() {
        return phoneNum;
    }

    public String getCity() {
        return city;
    }
    public int getTotalrate() {
        return totalRate;
    }

    public void setTotalrate(int totalrate) {
        this.totalRate = totalrate;
    }

    public int getNumrate() {
        return numRate;
    }

    public void setNumrate(int numrate) {
        this.numRate = numrate;
    }

    public float getAvrate() {
        return avRate;
    }

    public void setAvrate(float avrate) {
        this.avRate = avrate;
    }

    public String getCategory() {
        return category;
    }

    public float getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(float yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }



//    public String getProfilePicUrl() {
//        return profilePicUrl;
//    }
//
//    public void setProfilePicUrl(String profilePicUrl) {
//        this.profilePicUrl = profilePicUrl;
//    }

    public String getIdProofUrl() {
        return idProofUrl;
    }

    public void setIdProofUrl(String idProofUrl) {
        this.idProofUrl = idProofUrl;
    }
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
}
