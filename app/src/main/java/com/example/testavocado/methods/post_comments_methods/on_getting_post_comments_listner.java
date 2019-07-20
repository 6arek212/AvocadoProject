package com.example.testavocado.methods.post_comments_methods;
import com.example.testavocado.objects.post_comment;

import java.util.ArrayList;

public interface on_getting_post_comments_listner {

    public static final String TAG = "on_getting_post_comment";
    public void onsuccess_getting_post_comments(ArrayList<post_comment> post_comments_list);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
