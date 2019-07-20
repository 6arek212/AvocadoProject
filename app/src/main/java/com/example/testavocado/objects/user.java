package com.example.testavocado.objects;

import java.io.Serializable;

public class user implements Serializable {
  private int  userid;
  private String user_firstname;
  private String user_lastname;
  private String user_emil;
  private String user_birthday ;
  private int user_gender;
  private String user_lastlogin;
  private String user_password;
  private boolean isactive;

  //------------------------------------------------------------------------------------------------>

    public user(int userid, String user_firstname, String user_lastname, String user_emil, String user_birthday, int user_gender,
                String user_lastlogin, String user_password, boolean isactive) {
        this.userid = userid;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_emil = user_emil;
        this.user_birthday = user_birthday;
        this.user_gender = user_gender;
        this.user_lastlogin = user_lastlogin;
        this.user_password = user_password;
        this.isactive = isactive;
    }
    public user()
    {}
    //------------------------------------------------------------------------------------------------>

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

    public String getUser_emil() {
        return user_emil;
    }

    public void setUser_emil(String user_emil) {
        this.user_emil = user_emil;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_lastlogin() {
        return user_lastlogin;
    }

    public void setUser_lastlogin(String user_lastlogin) {
        this.user_lastlogin = user_lastlogin;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
    //------------------------------------------------------------------------------------------------>

    @Override
    public String toString() {
        return "user{" +
                "userid=" + userid +
                ", user_firstname='" + user_firstname + '\'' +
                ", user_lastname='" + user_lastname + '\'' +
                ", user_emil='" + user_emil + '\'' +
                ", user_birthday=" + user_birthday +
                ", user_gender=" + user_gender +
                ", user_lastlogin=" + user_lastlogin +
                ", user_password='" + user_password + '\'' +
                ", isactive=" + isactive +
                '}';
    }
}
