package com.example.testavocado.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChatUser   {


    @SerializedName("User_id")
    private int user_id;

    @SerializedName("User_first_name")
    private String user_first_name;

    @SerializedName("User_last_name")
    private String user_last_name;

    @SerializedName("Chat_id")
    private int chat_id;


    @SerializedName("Profile_photo")
    private String profile_photo;

    public ChatUser() {
    }


    public ChatUser(int user_id, String user_first_name,
                    String user_last_name, int chat_id, String profile_photo) {
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.chat_id = chat_id;
        this.profile_photo = profile_photo;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
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

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }


    @Override
    public String toString() {
        return "ChatUser{" +
                "user_id=" + user_id +
                ", user_first_name='" + user_first_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                ", chat_id=" + chat_id +
                ", profile_photo=" + profile_photo +
                '}';
    }
}
