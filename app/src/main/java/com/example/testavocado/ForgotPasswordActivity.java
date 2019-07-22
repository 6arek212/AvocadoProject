package com.example.testavocado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.Login.RegisterActivity;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.Validation;
import com.example.testavocado.user_login_register.registeraccount_page;
import com.google.android.material.snackbar.Snackbar;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    //widgets
    private EditText mEmail;
    private Button mSend;
    private ProgressBar mProgressBar;
    private TextView mRegister;
    private ConstraintLayout constraintLayout;

    //vars
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initWidgets();
        adjustStatusBarColor();
    }


    /**
     * setting up the status bar color so it match the primary color of this activity
     */
    private void adjustStatusBarColor() {
        Log.d(TAG, "adjustStatusBarColor: adjusting status bar color");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.darktheme));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.darktheme));
            }
        }
    }


    /**
     * init all the widget and attaching a click listener to the send button
     * and handling the password recovery
     */

    private void initWidgets() {
        mEmail = findViewById(R.id.email);
        mSend = findViewById(R.id.send);
        constraintLayout = findViewById(R.id.forgotPasswordLayout);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mRegister = findViewById(R.id.register);
        mContext = this;

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpMethods.closeKeyboard(ForgotPasswordActivity.this);
            }
        });


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation = new Validation(mContext);
                String email = mEmail.getText().toString();

                if (!email.trim().isEmpty()) {
                    if (validation.Email(email)) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mSend.setEnabled(false);
                        PasswordMethods.sendEmailForPasswordRecovery(email, new PasswordMethods.OnSendingEmailListener() {
                            @Override
                            public void onSent() {
                                Toast.makeText(mContext, "email has been sent", Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                                mEmail.setText("");
                                mSend.setEnabled(true);
                            }

                            @Override
                            public void onServerError(String exception) {
                                Log.d(TAG, "onServerError: " + exception);
                                mProgressBar.setVisibility(View.GONE);
                                mSend.setEnabled(true);
                                Toast.makeText(mContext, exception, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(String exception) {
                                Log.d(TAG, "onFailure: " + exception);
                                Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                                mSend.setEnabled(true);
                            }
                        });
                    }
                } else {
                    Snackbar.make(constraintLayout, getString(R.string.field_required), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, registeraccount_page.class));
                finish();
            }
        });
    }
}
