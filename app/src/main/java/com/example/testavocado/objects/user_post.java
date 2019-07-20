package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class user_post implements Serializable  {
    @SerializedName("Postid")
    private int postid;
    @SerializedName("Userid")
    private int userid;
    @SerializedName("Post_text")
    private String post_text;
    @SerializedName("Post_imageurl")
    private String post_imageurl;
    @SerializedName("Post_date")
    private String post_date;
    @SerializedName("Postlastchange_date")
    private String postlastchange_date;
    @SerializedName("How_can_see")
    private int how_can_see;
    @SerializedName("Likes_counter")
    private int likes_counter;
    @SerializedName("Comments_counter")
    private int comments_counter;
    @SerializedName("Dis_likes_counter")
    private int dis_likes_counter;
    @SerializedName("First_name")
    private String user_firstname;
    @SerializedName("Last_name")
    private String user_lastname;
    @SerializedName("Is_active")
    private boolean isactive;
    //---------------------------------------------------------------------->

    public user_post(int postid, int userid, String post_text, String post_imageurl, String post_date, String postlastchange_date, int how_can_see, int likes_counter, int comments_counter, int dis_likes_counter) {
        this.postid = postid;
        this.userid = userid;
        this.post_text = post_text;
        this.post_imageurl = post_imageurl;
        this.post_date = post_date;
        this.postlastchange_date = postlastchange_date;
        this.how_can_see = how_can_see;
        this.likes_counter = likes_counter;
        this.comments_counter = comments_counter;
        this.dis_likes_counter = dis_likes_counter;
    }

    public user_post(String post_text, String post_date) {
        this.post_text = post_text;
        this.post_date = post_date;
    }

    public user_post()
    {}
    //------------------------------------------------------------------------>

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_imageurl() {
        return post_imageurl;
    }

    public void setPost_imageurl(String post_imageurl) {
        this.post_imageurl = post_imageurl;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPostlastchange_date() {
        return postlastchange_date;
    }

    public void setPostlastchange_date(String postlastchange_date) {
        this.postlastchange_date = postlastchange_date;
    }

    public int getHow_can_see() {
        return how_can_see;
    }

    public void setHow_can_see(int how_can_see) {
        this.how_can_see = how_can_see;
    }

    public int getLikes_counter() {
        return likes_counter;
    }

    public void setLikes_counter(int likes_counter) {
        this.likes_counter = likes_counter;
    }

    public int getComments_counter() {
        return comments_counter;
    }

    public void setComments_counter(int comments_counter) {
        this.comments_counter = comments_counter;
    }

    public int getDis_likes_counter() {
        return dis_likes_counter;
    }

    public void setDis_likes_counter(int dis_likes_counter) {
        this.dis_likes_counter = dis_likes_counter;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
    //----------------------------------------------------------------------->

    @Override
    public String toString() {
        return "user_post{" +
                "postid=" + postid +
                ", userid=" + userid +
                ", post_text='" + post_text + '\'' +
                ", post_imageurl='" + post_imageurl + '\'' +
                ", post_date=" + post_date +
                ", postlastchange_date=" + postlastchange_date +
                ", how_can_see=" + how_can_see +
                ", likes_counter=" + likes_counter +
                ", comments_counter=" + comments_counter +
                ", dis_likes_counter=" + dis_likes_counter +
                '}';
    }
}
