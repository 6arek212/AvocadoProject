package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class Status {
    public static  String STATE_NOTFOUND="0";
    public static  String STATE_FOUND="1";
    public static  String STATE_EXCEPTION="-1";


    @SerializedName("State")
    private int state;

    @SerializedName("Exception")
    private String exception;

    @SerializedName("Json_data")
    private String json_data;


    public Status() {
    }

    public Status(int state, String exception, String json_data) {
        this.state = state;
        this.exception = exception;
        this.json_data = json_data;
    }


    public String getJson_data() {
        return json_data;
    }

    public void setJson_data(String json_data) {
        this.json_data = json_data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }


    @Override
    public String toString() {
        return "Status{" +
                "state='" + state + '\'' +
                ", exception='" + exception + '\'' +
                ", json_data='" + json_data + '\'' +
                '}';
    }
}
