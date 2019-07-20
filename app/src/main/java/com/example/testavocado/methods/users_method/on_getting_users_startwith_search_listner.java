package com.example.testavocado.methods.users_method;

import com.example.testavocado.objects.user;

import java.util.ArrayList;

public interface on_getting_users_startwith_search_listner {
    public static final String TAG="on_getting_users_startwith_search_listner ";
    public void onSuccess_getting_user_byid(ArrayList<user> userlist);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
