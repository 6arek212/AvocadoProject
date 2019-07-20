package com.example.testavocado.methods.messages_methods;

import com.example.testavocado.objects.messages_contacts;

import java.util.ArrayList;

public interface on_getting_list_contacts_messages_listner {

    public static final String TAG = "on_getting_list_contact";

    public void onsuccess_getting_contacts_list_messages(ArrayList<messages_contacts> contacts_list);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
