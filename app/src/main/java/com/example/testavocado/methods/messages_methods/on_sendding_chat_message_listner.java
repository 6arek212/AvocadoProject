package com.example.testavocado.methods.messages_methods;

public interface on_sendding_chat_message_listner {
    public static final String TAG = "on_sendding_chat_messag";

    public void onsuccess_sendding_chat_message(String result);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
