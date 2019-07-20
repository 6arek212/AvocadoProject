package com.example.testavocado.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultSettings {
    private static SharedPreferences sharedPreferences;

    // create one method that will instantiate sharedPreferecdes
    private static void getSharedPreferencesInstance(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // this is for the checked CheckPreference, please note our default value
    // for the checked preference is true, make your default value below to be true
    // if the default value was false or maybe the checkbox was unchecked, we could have
    // used false as our default value
    public static boolean splashScreenEnabled(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("splash", true);
        //return sharedPreferences.getBoolean(key, defaultValue);
        // this is the key in the settings_ui.xml for a specific preference
    }

    // this is for switchPreference
    public static boolean notificationEnanbled(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("notifications", true);
    }

    // for list preference
    // remember list preference will return entryValues, that means,
    // values are strings
    public static String getListPrefereceValue(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("listPreference", ""); // there was no default value, we can leave default value empty
    }

    // editTextPreferece [password]
    public static String getUserPassword(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("password", "");
    }
}