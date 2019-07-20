package com.example.testavocado.Models;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("Message_id")
    private int message_id;

    @SerializedName("Message_sender_id")
    private int message_sender_id;

    @SerializedName("Message_text")
    private String message_text;

    @SerializedName("Message_datetime")
    private String message_datetime;


    public Message() {
    }

    public Message(int message_id, int message_sender_id, String message_text, String message_datetime) {
        this.message_id = message_id;
        this.message_sender_id = message_sender_id;
        this.message_text = message_text;
        this.message_datetime = message_datetime;
    }


    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", message_sender_id=" + message_sender_id +
                ", message_text='" + message_text + '\'' +
                ", message_datetime='" + message_datetime + '\'' +
                '}';
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getMessage_sender_id() {
        return message_sender_id;
    }

    public void setMessage_sender_id(int message_sender_id) {
        this.message_sender_id = message_sender_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_datetime() {
        return message_datetime;
    }

    public void setMessage_datetime(String message_datetime) {
        this.message_datetime = message_datetime;
    }
}
