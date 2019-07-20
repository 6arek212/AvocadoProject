package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class post_comment implements Serializable {
    @SerializedName("Commentid")
    private int commentid;
    @SerializedName("Postid")
    private int postid;
    @SerializedName("Userid")
    private int userid;
    @SerializedName("Comment_text")
    private String comment_text;
    @SerializedName("Comment_date")
    private String comment_date;
    @SerializedName("Comment_likes_counter")
    private int comment_likes_counter;
    @SerializedName("User_firstname")
    private String user_firstname;
    @SerializedName("User_lastname")
    private String user_lastname;
    @SerializedName("Isactive")
    private boolean isactive;

    //Ctor--------------------------------------------------------------------------------------------------------->

    public post_comment(int commentid, int postid, int userid, String comment_text, String comment_date,
                        int comment_likes_counter, String user_firstname, String user_lastname, boolean isactive) {
        this.commentid = commentid;
        this.postid = postid;
        this.userid = userid;
        this.comment_text = comment_text;
        this.comment_date = comment_date;
        this.comment_likes_counter = comment_likes_counter;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.isactive = isactive;
    }

    public post_comment()
    {}
    //Get/set--------------------------------------------------------------------------------------------------------->

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

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

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public int getComment_likes_counter() {
        return comment_likes_counter;
    }

    public void setComment_likes_counter(int comment_likes_counter) {
        this.comment_likes_counter = comment_likes_counter;
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
    //--------------------------------------------------------------------------------------------------------->

    @Override
    public String toString() {
        return "post_comment{" +
                "commentid=" + commentid +
                ", postid=" + postid +
                ", userid=" + userid +
                ", comment_text='" + comment_text + '\'' +
                ", comment_date='" + comment_date + '\'' +
                ", comment_likes_counter=" + comment_likes_counter +
                ", user_firstname='" + user_firstname + '\'' +
                ", user_lastname='" + user_lastname + '\'' +
                ", isactive=" + isactive +
                '}';
    }
}
