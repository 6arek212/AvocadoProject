package com.example.testavocado.Models;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    private static final String TAG = "Comment";

    @SerializedName("Comment_id")
    private int comment_id;
    @SerializedName("Comment_user_id")
    private int comment_user_id;
    @SerializedName("Comment_text")
    private String comment_text;
    @SerializedName("Comment_date_time")
    private String comment_date_time;
    @SerializedName("Comment_user_name")
    private String comment_user_name;
    @SerializedName("Comment_user_profile_image_path")
    private String comment_user_profile_image_path;


    public Comment() {
    }


    public Comment(int comment_id, int comment_user_id, String comment_text,
                   String comment_date_time, String comment_user_name, String comment_user_profile_image_path) {
        this.comment_id = comment_id;
        this.comment_user_id = comment_user_id;
        this.comment_text = comment_text;
        this.comment_date_time = comment_date_time;
        this.comment_user_name = comment_user_name;
        this.comment_user_profile_image_path = comment_user_profile_image_path;
    }


    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(int comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_date_time() {
        return comment_date_time;
    }

    public void setComment_date_time(String comment_date_time) {
        this.comment_date_time = comment_date_time;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public void setComment_user_name(String comment_user_name) {
        this.comment_user_name = comment_user_name;
    }

    public String getComment_user_profile_image_path() {
        return comment_user_profile_image_path;
    }

    public void setComment_user_profile_image_path(String comment_user_profile_image_path) {
        this.comment_user_profile_image_path = comment_user_profile_image_path;
    }




}


