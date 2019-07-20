package com.example.testavocado.methods.posts_method;

public interface on_create_new_post_listner {
    public static final String TAG = "on_create_new_post_list";
    public void onsuccess_getting_myprofile_posts(String state);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
