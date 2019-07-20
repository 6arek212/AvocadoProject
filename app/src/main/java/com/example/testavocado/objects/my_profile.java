package com.example.testavocado.objects;

import java.io.Serializable;

public class my_profile implements Serializable {
    private int profileid;
    private int userid;
    private String firstlogin;
    private String profile_picture_url;
    private int posts_counter;
    private int photos_counter;
    private int connection_count;
    private String city;
    private String country;
    private String phonenumber;
    private String profile_bio;
    //Ctor----------------------------------------------------------------------------------------->

    public my_profile(int profileid, int userid, String firstlogin, String profile_picture_url, int posts_counter,
                      int photos_counter, int connection_count, String city, String country, String phonenumber, String profile_bio) {
        this.profileid = profileid;
        this.userid = userid;
        this.firstlogin = firstlogin;
        this.profile_picture_url = profile_picture_url;
        this.posts_counter = posts_counter;
        this.photos_counter = photos_counter;
        this.connection_count = connection_count;
        this.city = city;
        this.country = country;
        this.phonenumber = phonenumber;
        this.profile_bio = profile_bio;
    }

    public my_profile()
    {}
    //Get Set--------------------------------------------------------------------------------------------->

    public int getProfileid() {
        return profileid;
    }

    public void setProfileid(int profileid) {
        this.profileid = profileid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getFirstlogin() {
        return firstlogin;
    }

    public void setFirstlogin(String firstlogin) {
        this.firstlogin = firstlogin;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public int getPosts_counter() {
        return posts_counter;
    }

    public void setPosts_counter(int posts_counter) {
        this.posts_counter = posts_counter;
    }

    public int getPhotos_counter() {
        return photos_counter;
    }

    public void setPhotos_counter(int photos_counter) {
        this.photos_counter = photos_counter;
    }

    public int getConnection_count() {
        return connection_count;
    }

    public void setConnection_count(int connection_count) {
        this.connection_count = connection_count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getProfile_bio() {
        return profile_bio;
    }

    public void setProfile_bio(String profile_bio) {
        this.profile_bio = profile_bio;
    }
    //to string--------------------------------------------------------------------------------------------->
    @Override
    public String toString() {
        return "my_profile{" +
                "profileid=" + profileid +
                ", userid=" + userid +
                ", firstlogin='" + firstlogin + '\'' +
                ", profile_picture_url='" + profile_picture_url + '\'' +
                ", posts_counter=" + posts_counter +
                ", photos_counter=" + photos_counter +
                ", connection_count=" + connection_count +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", profile_bio='" + profile_bio + '\'' +
                '}';
    }
}
