package com.example.testavocado.methods.friend_request_methods;

public interface on_updating_friends_reques_listner {
    public static final String TAG = "on_updating_friends_req";

    public void onsuccess_updating_freind_request(String result);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
