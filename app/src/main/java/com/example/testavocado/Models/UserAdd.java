package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class UserAdd {

    @SerializedName("User_id")
    private int user_id;

    @SerializedName("User_first_name")
    private String user_first_name;

    @SerializedName("User_last_name")
    private String user_last_name;

    @SerializedName("User_profile_photo")
    private String user_profile_photo;

    @SerializedName("User_city")
    private String user_city;

    @SerializedName("User_country")
    private String user_country;

    @SerializedName("Is_accepted")
    private boolean is_accepted;

    @SerializedName("Request_id")
    private int request_id;

    @SerializedName("Sender_id")
    private int sender_id;

    @SerializedName("Distance")
    private double distance;


    public UserAdd() {
    }


    public UserAdd(int user_id, String user_first_name, String user_last_name,
                   String user_profile_photo, String user_city, String user_country,
                   boolean is_accepted, int request_id, int sender_id, double distance) {
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_profile_photo = user_profile_photo;
        this.user_city = user_city;
        this.user_country = user_country;
        this.is_accepted = is_accepted;
        this.request_id = request_id;
        this.sender_id = sender_id;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getUser_profile_photo() {
        return user_profile_photo;
    }

    public void setUser_profile_photo(String user_profile_photo) {
        this.user_profile_photo = user_profile_photo;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }


    @Override
    public String toString() {
        return "UserAdd{" +
                "user_first_name='" + user_first_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                ", user_profile_photo='" + user_profile_photo + '\'' +
                ", user_city='" + user_city + '\'' +
                ", user_country='" + user_country + '\'' +
                ", is_accepted=" + is_accepted +
                ", request_id=" + request_id +
                ", sender_id=" + sender_id +
                '}';
    }
}


