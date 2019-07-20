package com.example.testavocado.user_login_register;

public class register_object {

    private String firstname;
    private String lastname;
    private String emil;
    private String password;

    //Ctor ----------------------------------------------------------------------------------------------------->
    public register_object(String firstname, String lastname, String emil, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emil = emil;
        this.password = password;
    }

    public register_object() {

    }

    //Get Set----------------------------------------------------------------------------------------------------->
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

    public String getEmil() {
        return emil;
    }

    public void setEmil(String emil) {
        this.emil = emil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //------------------------------------------------------------------------------------------------->

    @Override
    public String toString() {
        return "register_object{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emil='" + emil + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
