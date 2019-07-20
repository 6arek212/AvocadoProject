package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class messages_contacts implements Serializable {
    @SerializedName("Contact_userid")
    private int contact_userid;
    @SerializedName("Contact_date")
    private String contact_date;
    @SerializedName("First_name")
    private String firstname;
    @SerializedName("Last_name")
    private String lastname;
    @SerializedName("Gender")
    private boolean gender;
    @SerializedName("Lastlogin")
    private String lastactive;
    @SerializedName("Is_active")
    private  boolean is_active;
//--------------------------------------------------------------------------------------------------------------->
    public messages_contacts(int contact_userid, String contact_date, String firstname, String lastname, boolean gender, String lastactive, boolean is_active) {
        this.contact_userid = contact_userid;
        this.contact_date = contact_date;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.lastactive = lastactive;
        this.is_active = is_active;
    }

    public messages_contacts()
    {}
//--------------------------------------------------------------------------------------------------------------->

    public int getContact_userid() {
        return contact_userid;
    }

    public void setContact_userid(int contact_userid) {
        this.contact_userid = contact_userid;
    }

    public String getContact_date() {
        return contact_date;
    }

    public void setContact_date(String contact_date) {
        this.contact_date = contact_date;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getLastactive() {
        return lastactive;
    }

    public void setLastactive(String lastactive) {
        this.lastactive = lastactive;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
    //--------------------------------------------------------------------------------------------------------------->

    @Override
    public String toString() {
        return "messages_contacts{" +
                "contact_userid=" + contact_userid +
                ", contact_date='" + contact_date + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender=" + gender +
                ", lastactive='" + lastactive + '\'' +
                ", is_active=" + is_active +
                '}';
    }
}
