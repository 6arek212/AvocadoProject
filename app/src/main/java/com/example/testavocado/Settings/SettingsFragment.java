package com.example.testavocado.Settings;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
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

    private int user_id;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_ui);
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user_id= HelpMethods.checkSharedPreferencesForUserId(getContext());

        checkFingerprint();

        findPreference(getString(R.string.private_profile_switch)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateSetting((Boolean)newValue,PRIVATE_ACCOUNT_TYPE);
                return true;
            }
        });


        findPreference(getString(R.string.location_switch)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateSetting((Boolean)newValue,LOCATION_TYPE);
                return true;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }




    private void updateSetting(boolean state, int type){
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



    private void checkFingerprint(){
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) getContext().getSystemService(Context.FINGERPRINT_SERVICE);

            if(fingerprintManager==null) {
                findPreference(getString(R.string.fingerPrint)).setVisible(false);
                return;
            }

            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                findPreference(getString(R.string.fingerPrint)).setVisible(false);

            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                findPreference(getString(R.string.fingerPrint)).setEnabled(false);
                findPreference(getString(R.string.fingerPrint)).setSummary(getString(R.string.you_need_add_firngerprint));

            } else {
                // Everything is ready for fingerprint authentication
                findPreference(getString(R.string.fingerPrint)).setEnabled(true);
            }
        } else {
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(getContext());

            if(fingerprintManagerCompat==null)
            {findPreference(getString(R.string.fingerPrint)).setVisible(false);
                return;
            }

            if (!fingerprintManagerCompat.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                findPreference(getString(R.string.fingerPrint)).setEnabled(false);

            } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                findPreference(getString(R.string.fingerPrint)).setEnabled(false);
                findPreference(getString(R.string.fingerPrint)).setSummary("you need to add you fingerprint to the system");
            } else {
                // Everything is ready for fingerprint authentication
                findPreference(getString(R.string.fingerPrint)).setEnabled(true);
            }
        }
    }

}
