package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class SavedPost {


    @SerializedName("Saved_post_id")
    private int saved_post_id;

    @SerializedName("Post_id")
    private int post_id;

    @SerializedName("Saved_datetime")
    private String saved_datetime;

    @SerializedName("Description")
    private String description;


    public int getSaved_post_id() {
        return saved_post_id;
    }

    public void setSaved_post_id(int saved_post_id) {
        this.saved_post_id = saved_post_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getSaved_datetime() {
        return saved_datetime;
    }

    public void setSaved_datetime(String saved_datetime) {
        this.saved_datetime = saved_datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SavedPost{" +
                "saved_post_id=" + saved_post_id +
                ", post_id=" + post_id +
                ", saved_datetime='" + saved_datetime + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
