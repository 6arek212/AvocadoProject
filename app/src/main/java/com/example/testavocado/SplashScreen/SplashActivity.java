package com.example.testavocado.SplashScreen;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.Login.LoginActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    //widgets
    private ImageView mAvocadoLogo,imgvword;
    private Context mContext;

    //var
    private static int SPLASH_TIME=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initWidgets();
        setTimer();

    }

    /**
     *          setting up time for the splash screen
     *          -updating the ui
     *
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
     *
     *          updating the ui according to the shred preferences
     *
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
     *              initializing all the widgets
     *
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
}
