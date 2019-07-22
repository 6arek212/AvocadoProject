package com.example.testavocado.SplashScreen;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;


import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.Login.LoginActivity;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    //widgets
    private ImageView mAvocadoLogo, imgvword;
    private Context mContext;

    //var
    private static int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initWidgets();
// Check if we're running on Android 6.0 (M) or higher

        Setting setting=HelpMethods.getSharedPreferences(this);

        if(setting.getUser_id()!=-1&&setting.isFingerprint()){
            Log.d(TAG, "onCreate: fingerprint setting "+true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Fingerprint API only available on from Android 6.0 (M)
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    setTimer();
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    setTimer();
                } else {
                    // Everything is ready for fingerprint authentication
                    checkFingerPrint();
                }
            } else {
                FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);

                if (!fingerprintManagerCompat.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    setTimer();
                } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    setTimer();
                } else {
                    // Everything is ready for fingerprint authentication
                    checkFingerPrint();
                }
            }
        }
        else{

            setTimer();
        }
    }






    /**
     * setting up the status bar color so it match the primary color of this activity
     */
    private void adjustStatusBarColor() {
        Log.d(TAG, "adjustStatusBarColor: adjusting status bar color");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.greenSplash));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.greenSplash));
            }
        }
    }




    /**
     * setting up time for the splash screen
     * -updating the ui
     */
    private void setTimer() {
        final Thread timer = new Thread() {

            public void run() {

                try {
                    sleep(SPLASH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    updateUI();
                    finish();
                }

            }

        };
        timer.start();
    }


    /**
     * updating the ui according to the shred preferences
     */


    private void updateUI() {
        int user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        Log.d(TAG, "updateUI: updating the ui id:  " + user_id);

        if (user_id != getResources().getInteger(R.integer.defaultValue)) {
            startActivity(new Intent(mContext, BaseActivity.class));
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
        finish();
    }


    /**
     * initializing all the widgets
     */

    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        mAvocadoLogo = findViewById(R.id.imgv_shape_mainactivity);
        imgvword = findViewById(R.id.imgv_wordlogo_mainactivity);
        mContext = SplashActivity.this;
        imgvword.setAnimation(myanim);
        mAvocadoLogo.setAnimation(myanim);
    }


    private void checkFingerPrint() {

        Executor newExecutor = Executors.newSingleThreadExecutor();


        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(this, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override

//onAuthenticationError is called when a fatal error occurrs//

            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                } else {
//Print a message to Logcat//
                    Log.d(TAG, "An unrecoverable error occurred");
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
                finish();
            }

//onAuthenticationSucceeded is called when a fingerprint is matched successfully//

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

//Print a message to Logcat//

                Log.d(TAG, "Fingerprint recognised successfully");
                setTimer();
            }

//onAuthenticationFailed is called when the fingerprint doesn’t match//

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

//Print a message to Logcat//

                Log.d(TAG, "Fingerprint not recognised");
            }
        });


        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint authentication ")
                .setSubtitle("Security")
                .setDescription("put your finger")
                .setNegativeButtonText("Cancel")
                .build();

        myBiometricPrompt.authenticate(promptInfo);
    }


}
