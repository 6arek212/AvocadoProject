package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class Setting {


    @SerializedName("User_location_switch")
    private boolean user_location_switch;

    @SerializedName("Account_is_private")
    private boolean account_is_private;

    @SerializedName("User_id")
    private int user_id;

    @SerializedName("ProfilePic")
    private String profilePic;

    @SerializedName("User_first_name")
    private String user_first_name;

    @SerializedName("User_last_name")
    private String user_last_name;

    private boolean fingerprint;




    public Setting() {
    }

    public void setUser_location_switch(boolean user_location_switch) {
        this.user_location_switch = user_location_switch;
    }

    public boolean isFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(boolean fingerprint) {
        this.fingerprint = fingerprint;
    }

    public boolean isUser_location_switch() {
        return user_location_switch;
    }



    public boolean isAccount_is_private() {
        return account_is_private;
    }

    public void setAccount_is_private(boolean account_is_private) {
        this.account_is_private = account_is_private;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }


    @Override
    public String toString() {
        return "Setting{" +
                "user_location_switch=" + user_location_switch +
                ", account_is_private=" + account_is_private +
                ", user_id=" + user_id +
                ", profilePic='" + profilePic + '\'' +
                ", user_first_name='" + user_first_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                '}';
    }
}
