package com.example.testavocado.Models;

import android.content.Context;

import com.example.testavocado.R;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;

public class User {
    @SerializedName("User_first_name")
    private String user_first_name;

    @SerializedName("User_last_name")
    private String user_last_name;

    @SerializedName("User_birth_date")
    private String user_birth_date;

    @SerializedName("User_profile_photo")
    private String user_profile_photo;

    @SerializedName("User_posts_count")
    private int user_posts_count;

    @SerializedName("User_photos_count")
    private int user_photos_count;

    @SerializedName("User_connection_count")
    private int user_connection_count;

    @SerializedName("User_reputation")
    private int user_reputation;

    @SerializedName("User_city")
    private String user_city;

    @SerializedName("User_country")
    private String user_country;

    @SerializedName("User_job")
    private String user_job;

    @SerializedName("User_is_private")
    private boolean user_is_private;

    @SerializedName("User_is_online")
    private boolean user_is_online;

    @SerializedName("Friend_request_id")
    private int friend_request_id;

    @SerializedName("Sender_id")
    private int sender_id;

    @SerializedName("Is_accepted")
    private boolean is_accepted;


    @SerializedName("User_bio")
    private String user_bio;

    public User() {
    }

    public String getUser_bio() {
        return user_bio;
    }

    public void setUser_bio(String user_bio) {
        this.user_bio = user_bio;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
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

    public String getUser_birth_date() {
        return user_birth_date;
    }

    public void setUser_birth_date(String user_birth_date) {
        this.user_birth_date = user_birth_date;
    }

    public String getUser_profile_photo() {
        return user_profile_photo;
    }

    public void setUser_profile_photo(String user_profile_photo) {
        this.user_profile_photo = user_profile_photo;
    }

    public int getUser_posts_count() {
        return user_posts_count;
    }

    public void setUser_posts_count(int user_posts_count) {
        this.user_posts_count = user_posts_count;
    }

    public int getUser_photos_count() {
        return user_photos_count;
    }

    public void setUser_photos_count(int user_photos_count) {
        this.user_photos_count = user_photos_count;
    }

    public int getUser_connection_count() {
        return user_connection_count;
    }

    public void setUser_connection_count(int user_connection_count) {
        this.user_connection_count = user_connection_count;
    }

    public int getUser_reputation() {
        return user_reputation;
    }

    public void setUser_reputation(int user_reputation) {
        this.user_reputation = user_reputation;
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

    public String getUser_job() {
        return user_job;
    }

    public void setUser_job(String user_job) {
        this.user_job = user_job;
    }

    public boolean isUser_is_private() {
        return user_is_private;
    }

    public void setUser_is_private(boolean user_is_private) {
        this.user_is_private = user_is_private;
    }

    public boolean isUser_is_online() {
        return user_is_online;
    }

    public void setUser_is_online(boolean user_is_online) {
        this.user_is_online = user_is_online;
    }

    public int getFriend_request_id() {
        return friend_request_id;
    }

    public void setFriend_request_id(int friend_request_id) {
        this.friend_request_id = friend_request_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_first_name='" + user_first_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                ", user_birth_date='" + user_birth_date + '\'' +
                ", user_profile_photo='" + user_profile_photo + '\'' +
                ", user_posts_count=" + user_posts_count +
                ", user_photos_count=" + user_photos_count +
                ", user_connection_count=" + user_connection_count +
                ", user_reputation=" + user_reputation +
                ", user_city='" + user_city + '\'' +
                ", user_country='" + user_country + '\'' +
                ", user_job='" + user_job + '\'' +
                ", user_is_private=" + user_is_private +
                ", user_is_online=" + user_is_online +
                ", friend_request_id=" + friend_request_id +
                ", sender_id=" + sender_id +
                ", is_accepted=" + is_accepted +
                '}';
    }
}

