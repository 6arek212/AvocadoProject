package com.example.testavocado.methods.posts_method;


import com.example.testavocado.objects.user_post;

public interface on_getting_posts_listner {
    public static final String TAG = "on_getting_posts_listne";

    public void onsuccess_getting_myprofile(user_post post);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
