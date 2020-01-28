package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class user_information implements Serializable {

    @SerializedName("user_email")
    private String user_email;
    @SerializedName("user_birth_date")
    private String user_birth_date ;
    @SerializedName("user_gender")
    private int user_gender;
    @SerializedName("user_phonenumber")
    private String user_phonenumber;
//CTOR----------------------------------------------------------------------------------------------------------->


    public user_information(String user_email, String user_birth_date, int user_gender, String user_phonenumber) {
        this.user_email = user_email;
        this.user_birth_date = user_birth_date;
        this.user_gender = user_gender;
        this.user_phonenumber = user_phonenumber;
    }

    public user_information()
    {}
//Get/Set------------------------------------------------------------------------------------------------------------->


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_birth_date() {
        return user_birth_date;
    }

    public void setUser_birth_date(String user_birth_date) {
        this.user_birth_date = user_birth_date;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_phonenumber() {
        return user_phonenumber;
    }

    public void setUser_phonenumber(String user_phonenumber) {
        this.user_phonenumber = user_phonenumber;
    }

    //--------------------------------------------------------------------->


    @Override
    public String toString() {
        return "user_information{" +
                "user_email='" + user_email + '\'' +
                ", user_birth_date='" + user_birth_date + '\'' +
                ", user_gender=" + user_gender +
                ", user_phonenumber='" + user_phonenumber + '\'' +
                '}';
    }
}
