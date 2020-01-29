package com.example.testavocado.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class Post  implements Parcelable  {



    @SerializedName("Post_id")
    private int post_id;

    @SerializedName("User_id")
    private int user_id;

    @SerializedName("User_first_name")
    private String user_name;

    @SerializedName("User_last_name")
    private String user_last_name;

    @SerializedName("User_profile_photo")
    private String user_profile_photo;

    @SerializedName("Post_text")
    private String post_text;

    @SerializedName("Post_images_url")
    private List<String> post_images_url;

    @SerializedName("Post_date_time")
    private String post_date_time;

    @SerializedName("Post_type")
    private int post_type;

    @SerializedName("Post_location")
    private String post_location;

    @SerializedName("Post_comments_count")
    private int post_comments_count;

    @SerializedName("Post_likes_count")
    private int post_likes_count;

    @SerializedName("Post_dislike_count")
    private int post_dislike_count;

    @SerializedName("Post_reports_count")
    private int post_reports_count;

    @SerializedName("Post_share_count")
    private int post_share_count;

    @SerializedName("Post_is_shared")
    private boolean post_is_shared;

    @SerializedName("Original_post_id")
    private int original_post_id;

    @SerializedName("Like_id")
    private int like_id;

    @SerializedName("Dis_like_id")
    private int dis_like_id;


    @SerializedName("saved_post_id")
    private int saved_post_id;


    public Post() {
    }


    public int getSaved_post_id() {
        return saved_post_id;
    }

    public void setSaved_post_id(int saved_post_id) {
        this.saved_post_id = saved_post_id;
    }

    public String getUser_profile_photo() {
        return user_profile_photo;
    }

    public void setUser_profile_photo(String user_profile_photo) {
        this.user_profile_photo = user_profile_photo;
    }

    public int getDis_like_id() {
        return dis_like_id;
    }

    public void setDis_like_id(int dis_like_id) {
        this.dis_like_id = dis_like_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
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

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public List<String> getPost_images_url() {
        return post_images_url;
    }

    public void setPost_images_url(List<String> post_images_url) {
        this.post_images_url = post_images_url;
    }

    public String getPost_date_time() {
        return post_date_time;
    }

    public void setPost_date_time(String post_date_time) {
        this.post_date_time = post_date_time;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public String getPost_location() {
        return post_location;
    }

    public void setPost_location(String post_location) {
        this.post_location = post_location;
    }

    public int getPost_comments_count() {
        return post_comments_count;
    }

    public void setPost_comments_count(int post_comments_count) {
        this.post_comments_count = post_comments_count;
    }

    public int getPost_likes_count() {
        return post_likes_count;
    }

    public void setPost_likes_count(int post_likes_count) {
        this.post_likes_count = post_likes_count;
    }

    public int getPost_dislike_count() {
        return post_dislike_count;
    }

    public void setPost_dislike_count(int post_dislike_count) {
        this.post_dislike_count = post_dislike_count;
    }

    public int getPost_reports_count() {
        return post_reports_count;
    }

    public void setPost_reports_count(int post_reports_count) {
        this.post_reports_count = post_reports_count;
    }

    public int getPost_share_count() {
        return post_share_count;
    }

    public void setPost_share_count(int post_share_count) {
        this.post_share_count = post_share_count;
    }

    public boolean isPost_is_shared() {
        return post_is_shared;
    }

    public void setPost_is_shared(boolean post_is_shared) {
        this.post_is_shared = post_is_shared;
    }

    public int getOriginal_post_id() {
        return original_post_id;
    }

    public void setOriginal_post_id(int original_post_id) {
        this.original_post_id = original_post_id;
    }

    public int getLike_id() {
        return like_id;
    }

    public void setLike_id(int like_id) {
        this.like_id = like_id;
    }


    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                ", user_profile_photo='" + user_profile_photo + '\'' +
                ", post_text='" + post_text + '\'' +
                ", post_images_url=" + post_images_url +
                ", post_date_time='" + post_date_time + '\'' +
                ", post_type=" + post_type +
                ", post_location='" + post_location + '\'' +
                ", post_comments_count=" + post_comments_count +
                ", post_likes_count=" + post_likes_count +
                ", post_dislike_count=" + post_dislike_count +
                ", post_reports_count=" + post_reports_count +
                ", post_share_count=" + post_share_count +
                ", post_is_shared=" + post_is_shared +
                ", original_post_id=" + original_post_id +
                ", like_id=" + like_id +
                ", dis_like_id=" + dis_like_id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
