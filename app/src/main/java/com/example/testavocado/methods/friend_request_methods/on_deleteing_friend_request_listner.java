package com.example.testavocado.methods.friend_request_methods;

public interface on_deleteing_friend_request_listner {
    public static final String TAG = "on_deleteing_friend_request_listner";

    public void onsuccess_deleting_freind_request(String result);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
