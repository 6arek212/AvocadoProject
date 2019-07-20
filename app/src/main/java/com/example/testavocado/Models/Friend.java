package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class Friend {


    @SerializedName("User_name")
    private String user_name;

    @SerializedName("User_profile_image")
    private String user_profile_image;

    @SerializedName("User_id")
    private int user_id;

    @SerializedName("Request_id")
    private int request_id;


    public Friend() {
    }

    public Friend(String user_name, String user_profile_image, int user_id, int request_id) {
        this.user_name = user_name;
        this.user_profile_image = user_profile_image;
        this.user_id = user_id;
        this.request_id = request_id;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "user_name='" + user_name + '\'' +
                ", user_profile_image='" + user_profile_image + '\'' +
                ", user_id=" + user_id +
                ", request_id=" + request_id +
                '}';
    }
}
