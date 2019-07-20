package com.example.testavocado.methods.messages_methods;

public interface on_adding_contact_message_list_listner {
    public static final String TAG = "on_adding_contact_messa";

    public void onsuccess_adding_contact_message_list(String result);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
