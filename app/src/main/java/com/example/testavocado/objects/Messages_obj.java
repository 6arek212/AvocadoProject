package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Messages_obj implements Serializable {
    @SerializedName("Meesageid")
    private int meesageid;
    @SerializedName("Userid_sender")
    private int userid_sender;
    @SerializedName("Userid_recipient")
    private int userid_recipient;
    @SerializedName("Meesage_text")
    private String meesage_text;
    @SerializedName("Meesage_date")
    private String meesage_date;
    @SerializedName("Is_read")
    private int is_read;
    @SerializedName("User_id")
    private int  userid;
    @SerializedName("First_name")
    private String user_firstname;
    @SerializedName("Last_name")
    private String user_lastname;
    @SerializedName("Lastlogin")
    private String user_lastlogin;
    @SerializedName("Is_active")
    private boolean isactive;
    //CTOR--------------------------------------------------------------------------------------------------------------->


    public Messages_obj(int meesageid, int userid_sender, int userid_recipient, String meesage_text, String meesage_date, int is_read, int userid, String user_firstname, String user_lastname, String user_lastlogin, boolean isactive) {
        this.meesageid = meesageid;
        this.userid_sender = userid_sender;
        this.userid_recipient = userid_recipient;
        this.meesage_text = meesage_text;
        this.meesage_date = meesage_date;
        this.is_read = is_read;
        this.userid = userid;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_lastlogin = user_lastlogin;
        this.isactive = isactive;
    }

    public Messages_obj(int userid_sender, int userid_recipient, String meesage_text, String meesage_date, int is_read, String user_firstname, String user_lastname, String user_lastlogin, boolean isactive) {
        this.userid_sender = userid_sender;
        this.userid_recipient = userid_recipient;
        this.meesage_text = meesage_text;
        this.meesage_date = meesage_date;
        this.is_read = is_read;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_lastlogin = user_lastlogin;
        this.isactive = isactive;
    }

    public Messages_obj()
    {

    }
    //Get/set--------------------------------------------------------------------------------------------------------------->

    public int getMeesageid() {
        return meesageid;
    }

    public void setMeesageid(int meesageid) {
        this.meesageid = meesageid;
    }

    public int getUserid_sender() {
        return userid_sender;
    }

    public void setUserid_sender(int userid_sender) {
        this.userid_sender = userid_sender;
    }

    public int getUserid_recipient() {
        return userid_recipient;
    }

    public void setUserid_recipient(int userid_recipient) {
        this.userid_recipient = userid_recipient;
    }

    public String getMeesage_text() {
        return meesage_text;
    }

    public void setMeesage_text(String meesage_text) {
        this.meesage_text = meesage_text;
    }

    public String getMeesage_date() {
        return meesage_date;
    }

    public void setMeesage_date(String meesage_date) {
        this.meesage_date = meesage_date;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public String getUser_lastlogin() {
        return user_lastlogin;
    }

    public void setUser_lastlogin(String user_lastlogin) {
        this.user_lastlogin = user_lastlogin;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }


    //--------------------------------------------------------------------------------------------------------------->


    @Override
    public String toString() {
        return "chat_message{" +
                "meesageid=" + meesageid +
                ", userid_sender=" + userid_sender +
                ", userid_recipient=" + userid_recipient +
                ", meesage_text='" + meesage_text + '\'' +
                ", meesage_date='" + meesage_date + '\'' +
                ", is_read=" + is_read +
                ", userid=" + userid +
                ", user_firstname='" + user_firstname + '\'' +
                ", user_lastname='" + user_lastname + '\'' +
                ", user_lastlogin='" + user_lastlogin + '\'' +
                ", isactive=" + isactive +
                '}';
    }
}
