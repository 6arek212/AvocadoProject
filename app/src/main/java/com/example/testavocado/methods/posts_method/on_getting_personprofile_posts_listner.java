package com.example.testavocado.methods.posts_method;

import com.example.testavocado.objects.user_post;

import java.util.ArrayList;

public interface on_getting_personprofile_posts_listner {

    public static final String TAG = "on_getting_personprofil";
    public void onsuccess_getting_personprofile_posts(ArrayList<user_post> posts_list);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
