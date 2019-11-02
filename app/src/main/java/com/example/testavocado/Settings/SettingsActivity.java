package com.example.testavocado.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testavocado.Dialogs.ConfirmDialog;
import com.example.testavocado.Dialogs.ConfirmDialogEditeText;
import com.example.testavocado.Login.LoginActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.AccountSettingMethods;
import com.example.testavocado.Utils.HelpMethods;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";


    private Button mDeleteAccount;
    private ProgressBar progressBar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        initWidgets();
    }


    private void initWidgets() {
        mDeleteAccount = findViewById(R.id.deleteAccount);
        progressBar = findViewById(R.id.progressBar);
        mContext = this;
        progressBar.setVisibility(View.GONE);

        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }


    private void deleteAccount() {

        final int user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);



        final ConfirmDialogEditeText confirmDialog = new ConfirmDialogEditeText();
        confirmDialog.setTitle(getString(R.string.are_you_sure_remove_account));
        confirmDialog.setHind(getString(R.string.password));
        confirmDialog.setType(true);
        confirmDialog.setOnConfirm(new ConfirmDialogEditeText.OnConfirmListener() {
            @Override
            public void onConfirm(String text) {
                progressBar.setVisibility(View.VISIBLE);

                AccountSettingMethods.deleteAccount(user_id,text, new AccountSettingMethods.OnDeletingAccount() {
                    @Override
                    public void onSuccess() {
                        HelpMethods.deleteUserIdSharedPreferences(mContext, SettingsActivity.this);
                        updateUI();
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void serverException(String exception) {
                        Log.d(TAG, "serverException: " + exception);
                        //password incorrect
                        Toast.makeText(mContext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void OnFailure(String exception) {
                        Log.d(TAG, "OnFailure: " + exception);
                        Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                        confirmDialog.dismiss();
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });

        confirmDialog.show(getSupportFragmentManager(), mContext.getString(R.string.confirm_dialog));
    }


    /**
     * updating the ui to BaseProfileActivity
     */

    private void updateUI() {

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }
}
