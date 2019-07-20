package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("Notification_id")
    private int notification_id;

    @SerializedName("User_id_sent_notification")
    private int user_id_sent_notification;

    @SerializedName("User_sent_profile_image")
    private String user_sent_profile_image;


    @SerializedName("User_sent_name")
    private String user_sent_name;

    @SerializedName("Notification_type")
    private int notification_type;

    @SerializedName("Notification_datetime")
    private String notification_datetime;

    @SerializedName("Post_id")
    private int post_id;

    @SerializedName("Type_txt")
    private String type_txt;


    public Notification() {
    }


    public Notification(int notification_id, int user_id_sent_notification,
                        String user_sent_profile_image, String user_sent_name,
                        int notification_type, String notification_datetime, int post_id, String type_txt) {
        this.notification_id = notification_id;
        this.user_id_sent_notification = user_id_sent_notification;
        this.user_sent_profile_image = user_sent_profile_image;
        this.user_sent_name = user_sent_name;
        this.notification_type = notification_type;
        this.notification_datetime = notification_datetime;
        this.post_id = post_id;
        this.type_txt = type_txt;
    }

    public String getType_txt() {
        return type_txt;
    }

    public void setType_txt(String type_txt) {
        this.type_txt = type_txt;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getUser_id_sent_notification() {
        return user_id_sent_notification;
    }

    public void setUser_id_sent_notification(int user_id_sent_notification) {
        this.user_id_sent_notification = user_id_sent_notification;
    }

    public String getUser_sent_profile_image() {
        return user_sent_profile_image;
    }

    public void setUser_sent_profile_image(String user_sent_profile_image) {
        this.user_sent_profile_image = user_sent_profile_image;
    }

    public String getUser_sent_name() {
        return user_sent_name;
    }

    public void setUser_sent_name(String user_sent_name) {
        this.user_sent_name = user_sent_name;
    }

    public int getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }

    public String getNotification_datetime() {
        return notification_datetime;
    }

    public void setNotification_datetime(String notification_datetime) {
        this.notification_datetime = notification_datetime;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notification_id=" + notification_id +
                ", user_id_sent_notification=" + user_id_sent_notification +
                ", user_sent_profile_image='" + user_sent_profile_image + '\'' +
                ", user_sent_name='" + user_sent_name + '\'' +
                ", notification_type=" + notification_type +
                ", notification_datetime='" + notification_datetime + '\'' +
                ", post_id=" + post_id +
                ", type_txt='" + type_txt + '\'' +
                '}';
    }
}
