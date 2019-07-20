package com.example.testavocado.methods.add_newuser;

public interface on_add_new_user {
    public static final String TAG = "on_add_new_user";

    public void onsuccess_adding_new_user();
    public void onserverException(String exception);
    public void onFailure(String exception);
}
