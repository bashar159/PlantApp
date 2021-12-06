package com.example.task14.Classes;

public class UserModel {

    private String fName, carType , carColor,carModel,phoneNumber , user , userId , location;

    public UserModel() {
    }

    public UserModel(String fName, String carType, String carColor, String carModel, String phoneNumber, String user , String userId , String location) {
        this.fName = fName;
        this.carType = carType;
        this.carColor = carColor;
        this.carModel = carModel;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.userId = userId;
        this.location = location;
    }

    public UserModel(String fName, String carType, String carColor, String carModel, String phoneNumber, String user , String userId) {
        this.fName = fName;
        this.carType = carType;
        this.carColor = carColor;
        this.carModel = carModel;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {

        return "UserModel{" +
                "fName='" + fName + '\'' +
                ", carType='" + carType + '\'' +
                ", carColor='" + carColor + '\'' +
                ", carModel='" + carModel + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", user='" + user + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
