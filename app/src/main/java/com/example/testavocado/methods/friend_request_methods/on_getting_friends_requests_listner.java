package com.example.testavocado.methods.friend_request_methods;
import com.example.testavocado.objects.friends_requests;

import java.util.ArrayList;

public interface on_getting_friends_requests_listner {
    public static final String TAG = "on_getting_friends_requ";

    public void onsuccess_geeting_friends_requests(ArrayList<friends_requests> friends_requests_list);

    public void onserverException(String exception);

    public void onFailure(String exception);
}
