package com.example.testavocado.methods.posts_method;

import com.example.testavocado.objects.friends_posts;

import java.util.ArrayList;

public interface on_getting_friends_posts_listner {
    public static final String TAG = "on_getting_friends_posts_listner";
    public void onsuccess_getting_personprofile_posts(ArrayList<friends_posts> friends_posts_list1);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
