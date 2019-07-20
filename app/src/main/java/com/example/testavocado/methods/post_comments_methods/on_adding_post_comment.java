package com.example.testavocado.methods.post_comments_methods;

public interface on_adding_post_comment {
    public static final String TAG = "on_adding_post_comment";

    public void onsuccess_adding_post_comment(String State);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
