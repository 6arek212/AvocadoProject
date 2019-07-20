package com.example.testavocado.methods.friend_request_methods;

public interface on_sending_friendsrequest_listner {
    public static final String TAG = "on_sending_friendsrequest_listner";

    public void onsuccess_sending_freind_request(String result);

    public void onserverException(String exception);

    public void onFailure(String exception);
}
