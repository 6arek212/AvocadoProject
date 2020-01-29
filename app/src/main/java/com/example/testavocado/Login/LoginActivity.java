package com.example.testavocado.Login;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testavocado.ForgotPasswordActivity;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.Service.BackgroundService;
import com.example.testavocado.ShakeDetector;
import com.example.testavocado.user_login_register.registeraccount_page;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.Validation;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //widgets
    private EditText mPassword, mEmail;
    private Button mLogin;
    private RelativeLayout relativeLayout;
    private TextView forgotPassword, createAccount;
    private ImageView avocado;
    private ProgressBar progressBar;

    //var
    private Context mContext;


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        adjustStatusBarColor();
        HelpMethods.closeKeyboard(LoginActivity.this);
        initWidgets();
        initShakeDetector();


    }

    private void initShakeDetector() {
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                handleShakeEvent(count);
            }
        });
    }

    private void handleShakeEvent(int count) {
        mEmail.setText("");
        mPassword.setText("");

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        try{
            stopService();
        }catch (Exception e){

        }
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void adjustStatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.statusBarLogin));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.statusBarLogin));
            }
        }
    }


    /**
     * Initializing all widgets
     */


    private void initWidgets() {
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        mLogin = findViewById(R.id.login);
        relativeLayout = findViewById(R.id.mainLayout);
        createAccount = findViewById(R.id.createAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
        avocado = findViewById(R.id.avocadoImage);
        progressBar = findViewById(R.id.progressBar);
        mContext = LoginActivity.this;
        progressBar.setVisibility(View.GONE);


        relativeLayout.setOnClickListener(new mClick());
        mLogin.setOnClickListener(new mClick());
        createAccount.setOnClickListener(new mClick());
        forgotPassword.setOnClickListener(new mClick());
    }


    /**
     * Defining The OnClick for all the widgets
     */


    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.mainLayout:
                    HelpMethods.closeKeyboard(LoginActivity.this);
                    break;


                case R.id.login:
                    Log.d(TAG, "onClick: click login");
                    String email, password;
                    password = mPassword.getText().toString().trim();
                    email = mEmail.getText().toString().trim();
                    Validation validation = new Validation(mContext);

                    if (password.isEmpty() || email.isEmpty()) {
                        Snackbar.make(findViewById(android.R.id.content), getString(R.string.field_required), Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(mContext, "all field are required !", Toast.LENGTH_SHORT).show();
                    } else {
                        if (validation.Password(password) && validation.EmailAndPhoneNumber(email)) {
                            progressBar.setVisibility(View.VISIBLE);
                            HelpMethods.closeKeyboard(LoginActivity.this);
                            // loginWithEmailAndPassword(email, password);
                            LoginMethods.loginWithEmailAndPassword(email, password, new LoginMethods.onLogin() {
                                @Override
                                public void onSuccessListener(Setting setting) {
                                    HelpMethods.addSharedPreferences(setting, mContext);
                                    updateUI();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, getString(R.string.LOGIN_SUCCESS), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onServerException(String ex) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, getString(R.string.EMAIL_PASSWORD_CHECK), Toast.LENGTH_SHORT).show();
                                    mEmail.setText("");
                                    mPassword.setText("");
                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                                    mEmail.setText("");
                                    mPassword.setText("");
                                }
                            });
                        }
                    }

                    break;


                case R.id.createAccount:
                    Intent intent = new Intent(mContext, registeraccount_page.class);
                    startActivity(intent);
                    break;

                case R.id.forgotPassword:
                    startActivity(new Intent(mContext, ForgotPasswordActivity.class));
                    break;

            }
        }
    }


    /**
     * update the UI to navigate to the MAIN FEED ACTIVITY
     */

    private void updateUI() {
        Intent intent = new Intent(mContext, BaseActivity.class);
        startActivity(intent);
        finish();
    }


    public void stopService() {
        BackgroundService.stopThis();
        stopService(new Intent(this, BackgroundService.class));
    }


}
