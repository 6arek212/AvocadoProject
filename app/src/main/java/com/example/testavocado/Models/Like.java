package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class Like {
    @SerializedName("User_id")
    private int user_id;

    @SerializedName("User_name")
    private String user_name;

    @SerializedName("Time")
    private String time;

    @SerializedName("Profile_image")
    private String profile_image;


    public Like() {
    }

    public Like(int user_id, String user_name, String time, String profile_image) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.time = time;
        this.profile_image = profile_image;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
