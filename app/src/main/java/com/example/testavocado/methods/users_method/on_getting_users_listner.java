package com.example.testavocado.methods.users_method;


import com.example.testavocado.objects.user;

public interface on_getting_users_listner {
    public static final String TAG = "on_getting_users_listne";

    public void onSuccess_getting_user_byid(user user1);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
