package com.example.p.jbossapp;

public class ListDetails {
    private String name = "Name";
    private String description = "this is the description";
    private String url = "";

    // Getters
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getUrl(){
        return this.url;
    }

    //Setters
    public void setName(String str) {
        this.name = str;
    }
    public void setDescription(String str) {
        this.description = str;
    }
    public void setUrl(String str) {
        this.url = str;
    }
}
