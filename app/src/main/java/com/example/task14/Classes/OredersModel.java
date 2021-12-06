package com.example.task14.Classes;

public class OredersModel {

    String Name_text,tExp ;
    String rating;


    public OredersModel() {
    }

    public OredersModel(String name_text, String tExp, String rating) {
        Name_text = name_text;
        this.tExp = tExp;
        this.rating = rating;
    }

    public String getName_text() {
        return Name_text;
    }

    public void setName_text(String name_text) {
        Name_text = name_text;
    }

    public String gettExp() {
        return tExp;
    }

    public void settExp(String tExp) {
        this.tExp = tExp;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
