package com.example.testavocado.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class friends_requests implements Serializable {
    @SerializedName("Requestid")
    private int requestid;
    @SerializedName("Userid_sender")
    private  int userid_sender;
    @SerializedName("Userid_recipient")
    private int userid_recipient;
    @SerializedName("Request_date")
    private String request_date;
    @SerializedName("Freinds_type")
    private int freinds_type;
    @SerializedName("Approvaldate")
    private String approvaldate;
    @SerializedName("First_name")
    private String first_name;
    @SerializedName("Last_name")
    private String last_name;
    @SerializedName("Lastlogin")
    private String lastlogin;
    @SerializedName("Is_active")
    private boolean is_active;

    //Ctor------------------------------------------------------------------------------------------------->
    public friends_requests()
    {}

    public friends_requests(int requestid, int userid_sender, int userid_recipient, String request_date, int freinds_type,
                            String approvaldate, String first_name, String last_name, String lastlogin, boolean is_active) {
        this.requestid = requestid;
        this.userid_sender = userid_sender;
        this.userid_recipient = userid_recipient;
        this.request_date = request_date;
        this.freinds_type = freinds_type;
        this.approvaldate = approvaldate;
        this.first_name = first_name;
        this.last_name = last_name;
        this.lastlogin = lastlogin;
        this.is_active = is_active;
    }

    //Get/Set------------------------------------------------------------------------------------------------->

    public int getRequestid() {
        return requestid;
    }

    public void setRequestid(int requestid) {
        this.requestid = requestid;
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

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public int getFreinds_type() {
        return freinds_type;
    }

    public void setFreinds_type(int freinds_type) {
        this.freinds_type = freinds_type;
    }

    public String getApprovaldate() {
        return approvaldate;
    }

    public void setApprovaldate(String approvaldate) {
        this.approvaldate = approvaldate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    //tostring------------------------------------------------------------------------------------------------->
    @Override
    public String toString() {
        return "friends_requests{" +
                "requestid=" + requestid +
                ", userid_sender=" + userid_sender +
                ", userid_recipient=" + userid_recipient +
                ", request_date='" + request_date + '\'' +
                ", freinds_type=" + freinds_type +
                ", approvaldate='" + approvaldate + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", lastlogin='" + lastlogin + '\'' +
                ", is_active=" + is_active +
                '}';
    }
}
