package com.example.task14.Classes;

public class Model {
    private String name , tExperience , lExperience, helperLocation, phoneNumber , user , userId;

    public Model(String name, String tExperience, String lExperience, String helperLocation, String phoneNumber, String user, String helperId) {
        this.name = name;
        this.tExperience = tExperience;
        this.lExperience = lExperience;
        this.helperLocation = helperLocation;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.userId = helperId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Model() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String gettExperience() {
        return tExperience;
    }

    public void settExperience(String tExperience) {
        this.tExperience = tExperience;
    }

    public String getlExperience() {
        return lExperience;
    }

    public void setlExperience(String lExperience) {
        this.lExperience = lExperience;
    }

    public String getHelperLocation() {
        return helperLocation;
    }

    public void setHelperLocation(String helperLocation) {
        this.helperLocation = helperLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", tExperience='" + tExperience + '\'' +
                ", lExperience='" + lExperience + '\'' +
                ", location='" + helperLocation + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", user='" + user + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public Model(String name, String tExperience, String lExperience, String phoneNumber) {
        this.name = name;
        this.tExperience = tExperience;
        this.lExperience = lExperience;
        this.phoneNumber = phoneNumber;
    }

    public Model(String name, String tExperience, String lExperience, String phoneNumber, String userId) {
        this.name = name;
        this.tExperience = tExperience;
        this.lExperience = lExperience;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }
}
