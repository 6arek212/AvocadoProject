package com.example.testavocado.methods;


import com.example.testavocado.objects.user;

public interface on_logging_listner {

    public static final String TAG = "on_logging_listner";
    public void onsuccess_getting_loginuser(user myuser1);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
