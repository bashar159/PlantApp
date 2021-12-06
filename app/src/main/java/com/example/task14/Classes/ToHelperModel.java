package com.example.task14.Classes;

public class ToHelperModel {
    private String name , distance ,carType ,carColor , phone , location;

    public ToHelperModel() {

    }

    public ToHelperModel(String name,String distance, String carType, String carColor , String phone , String location) {
        this.name = name;
        this.carType = carType;
        this.carColor = carColor;
        this.distance = distance;
        this.phone = phone;
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public ToHelperModel(String name, String distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
