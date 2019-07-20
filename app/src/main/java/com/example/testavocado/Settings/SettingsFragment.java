package com.example.testavocado.Settings;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testavocado.R;
import com.example.testavocado.Utils.AccountSettingMethods;
import com.example.testavocado.Utils.HelpMethods;

import static com.example.testavocado.Utils.AccountSettingMethods.LOCATION_TYPE;
import static com.example.testavocado.Utils.AccountSettingMethods.PRIVATE_ACCOUNT_TYPE;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    private static final String ACCOUNT_PRIVATE="privateAccount";
    private static final String LOCATION="location";
    private int user_id;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_ui, rootKey);
        user_id= HelpMethods.checkSharedPreferencesForUserId(getContext());

        findPreference(ACCOUNT_PRIVATE).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(getContext(), (Boolean)newValue+"", Toast.LENGTH_SHORT).show();
                updateSetting((Boolean)newValue,PRIVATE_ACCOUNT_TYPE);
                return true;
            }
        });


        findPreference(LOCATION).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(getContext(), (Boolean)newValue+"", Toast.LENGTH_SHORT).show();
                updateSetting((Boolean)newValue,LOCATION_TYPE);
                return true;
            }
        });




    }


    private void updateSetting(boolean state,int type){
        Log.d(TAG, "updateSetting: user_id "+user_id+"  state "+state+" type "+type);
        AccountSettingMethods.updateAccountSettings(user_id,state, type, new AccountSettingMethods.OnUpdatingAccountSetting() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void serverException(String exception) {
                Log.d(TAG, "serverException: "+exception);
                Toast.makeText(getContext(), getContext().getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: "+exception);
                Toast.makeText(getContext(), getContext().getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
