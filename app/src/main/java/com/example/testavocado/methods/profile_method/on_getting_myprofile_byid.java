package com.example.testavocado.methods.profile_method;
import com.example.testavocado.objects.my_profile;

public interface on_getting_myprofile_byid {
    public static final String TAG="on_getting_myprofile_byid";

    public void onsuccess_getting_myprofile_byid(my_profile profile);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
