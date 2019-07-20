package com.example.testavocado.methods.messages_methods;
import com.example.testavocado.objects.Messages_obj;

import java.util.ArrayList;

public interface on_getting_chat_messages_listner {
    public static final String TAG = "on_getting_chat_message";

    public void onsuccess_on_getting_chat_messages(ArrayList<Messages_obj> chat_messages_list);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
