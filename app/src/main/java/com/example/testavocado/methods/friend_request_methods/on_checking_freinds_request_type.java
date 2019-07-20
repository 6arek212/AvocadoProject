package com.example.testavocado.methods.friend_request_methods;

import java.util.ArrayList;

public interface on_checking_freinds_request_type {
    public static final String TAG = "on_checking_freinds_req";
    public void onsuccess_sending_freind_request(ArrayList resultlist);

    public void onserverException(String exception);

    public void onFailure(String exception);
}
