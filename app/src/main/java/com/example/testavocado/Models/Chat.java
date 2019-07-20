package com.example.testavocado.Models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("Chat_id")
    private int chat_id;

    @SerializedName("User_first_name")
    private String user_first_name;

    @SerializedName("User_last_name")
    private String user_last_name;

    @SerializedName("User_profile_photo")
    private String user_profile_photo;

    @SerializedName("Chat_sender_id")
    private int chat_sender_id;

    @SerializedName("Chat_receiver_id")
    private int chat_receiver_id;

    @SerializedName("Chat_datetime_created")
    private String chat_datetime_created;

    @SerializedName("Chat_messages_count")
    private int chat_messages_count;

    @SerializedName("Chat_last_message_user_id")
    private int chat_last_message_user_id;

    @SerializedName("Chat_last_message")
    private String chat_last_message;

    @SerializedName("Chat_last_message_datetime")
    private String chat_last_message_datetime;

    @SerializedName("Sender_not_read")
    private int sender_not_read;

    @SerializedName("Receiver_not_read")
    private int receiver_not_read;


    public Chat() {
    }


    public Chat(int chat_id, String user_first_name,
                String user_last_name, String user_profile_photo,
                int chat_sender_id, int chat_receiver_id, String chat_datetime_created,
                int chat_messages_count, int chat_last_message_user_id, String chat_last_message,
                String chat_last_message_datetime, int sender_not_read, int receiver_not_read) {
        this.chat_id = chat_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_profile_photo = user_profile_photo;
        this.chat_sender_id = chat_sender_id;
        this.chat_receiver_id = chat_receiver_id;
        this.chat_datetime_created = chat_datetime_created;
        this.chat_messages_count = chat_messages_count;
        this.chat_last_message_user_id = chat_last_message_user_id;
        this.chat_last_message = chat_last_message;
        this.chat_last_message_datetime = chat_last_message_datetime;
        this.sender_not_read = sender_not_read;
        this.receiver_not_read = receiver_not_read;
    }




    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
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

    public String getUser_profile_photo() {
        return user_profile_photo;
    }

    public void setUser_profile_photo(String user_profile_photo) {
        this.user_profile_photo = user_profile_photo;
    }

    public int getChat_sender_id() {
        return chat_sender_id;
    }

    public void setChat_sender_id(int chat_sender_id) {
        this.chat_sender_id = chat_sender_id;
    }

    public int getChat_receiver_id() {
        return chat_receiver_id;
    }

    public void setChat_receiver_id(int chat_receiver_id) {
        this.chat_receiver_id = chat_receiver_id;
    }

    public String getChat_datetime_created() {
        return chat_datetime_created;
    }

    public void setChat_datetime_created(String chat_datetime_created) {
        this.chat_datetime_created = chat_datetime_created;
    }

    public int getChat_messages_count() {
        return chat_messages_count;
    }

    public void setChat_messages_count(int chat_messages_count) {
        this.chat_messages_count = chat_messages_count;
    }

    public int getChat_last_message_user_id() {
        return chat_last_message_user_id;
    }

    public void setChat_last_message_user_id(int chat_last_message_user_id) {
        this.chat_last_message_user_id = chat_last_message_user_id;
    }

    public String getChat_last_message() {
        return chat_last_message;
    }

    public void setChat_last_message(String chat_last_message) {
        this.chat_last_message = chat_last_message;
    }

    public String getChat_last_message_datetime() {
        return chat_last_message_datetime;
    }

    public void setChat_last_message_datetime(String chat_last_message_datetime) {
        this.chat_last_message_datetime = chat_last_message_datetime;
    }

    public int getSender_not_read() {
        return sender_not_read;
    }

    public void setSender_not_read(int sender_not_read) {
        this.sender_not_read = sender_not_read;
    }

    public int getReceiver_not_read() {
        return receiver_not_read;
    }

    public void setReceiver_not_read(int receiver_not_read) {
        this.receiver_not_read = receiver_not_read;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chat_id=" + chat_id +
                ", user_first_name='" + user_first_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                ", user_profile_photo='" + user_profile_photo + '\'' +
                ", chat_sender_id=" + chat_sender_id +
                ", chat_receiver_id=" + chat_receiver_id +
                ", chat_datetime_created='" + chat_datetime_created + '\'' +
                ", chat_messages_count=" + chat_messages_count +
                ", chat_last_message_user_id=" + chat_last_message_user_id +
                ", chat_last_message='" + chat_last_message + '\'' +
                ", chat_last_message_datetime='" + chat_last_message_datetime + '\'' +
                ", sender_not_read=" + sender_not_read +
                ", receiver_not_read=" + receiver_not_read +
                '}';
    }
}
