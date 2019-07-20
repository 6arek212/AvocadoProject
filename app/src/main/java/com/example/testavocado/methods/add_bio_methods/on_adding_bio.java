package com.example.testavocado.methods.add_bio_methods;

public interface on_adding_bio {
    public static final String TAG = "on_adding_bio";

    public void onsuccess_adding_bio(String result);
    public void onserverException(String exception);
    public void onFailure(String exception);
}
