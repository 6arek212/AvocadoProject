package com.example.testavocado.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.testavocado.Models.Setting;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.testavocado.EditeInfo.BaseProfileEditActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.example.testavocado.Utils.Validation;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";


    //widgets
    Button mRegister;
    EditText mEmail, mPassword, mFirstName;
    RelativeLayout rel1, rel2;
    Context mContext;
    ProgressBar progressBar;


    //vars
    Boolean registerBtnIsClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:  stating");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        HelpMethods.closeKeyboard(RegisterActivity.this);
        adjustStatusBarColor();
        initWidgets();
    }





    /**
     *                  setting up the status bar color so it match the primary color of this activity
     *
     */
    private void adjustStatusBarColor() {
        Log.d(TAG, "adjustStatusBarColor: adjusting status bar color");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getColor(R.color.colorBlue));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.colorBlue));
            }
        }
    }






    /**
     *              initializing all the widgets
     *
     *        - attaching the widgets with XML
     *        - set the onClick Listeners
     *
     */

    private void initWidgets() {
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        mFirstName = findViewById(R.id.userFirstName);
        mRegister = findViewById(R.id.register);
        rel1 = findViewById(R.id.relLayout1);
        rel2 = findViewById(R.id.relLayout2);
        progressBar = findViewById(R.id.progressBar);
        mContext = RegisterActivity.this;
        progressBar.setVisibility(View.GONE);


        rel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpMethods.closeKeyboard(RegisterActivity.this);
            }
        });

        rel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpMethods.closeKeyboard(RegisterActivity.this);
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: button register clicked");

                if (!registerBtnIsClicked) {
                   final String email, password, name;

                    email = mEmail.getText().toString();
                    password = mPassword.getText().toString();
                    name = mFirstName.getText().toString();

                    if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                        Snackbar.make(findViewById(android.R.id.content),getString(R.string.field_required),Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(mContext, getString(R.string.field_required), Toast.LENGTH_SHORT).show();
                    }
                    else if (checkingDataValidation(name, email, password)) {
                        registerBtnIsClicked = true;
                        progressBar.setVisibility(View.VISIBLE);
                        //addNewUserToDB(name,email , password);
                        RegisterMethods.onRegisteringNewUser(name,"", email, password, TimeMethods.getUTCdatetimeAsString(), mContext, new RegisterMethods.onLoginRegister() {
                            @Override
                            public void onSuccessListener(int user_id) {
                                Log.d(TAG, "onSuccessListener: "+user_id);

                                registerBtnIsClicked = false;
                                progressBar.setVisibility(View.GONE);

                                BaseProfileEditActivity.setting=new Setting();
                                BaseProfileEditActivity.setting.setUser_first_name(name);
                                BaseProfileEditActivity.setting.setUser_id(user_id);

                                Toast.makeText(RegisterActivity.this, getString(R.string.ACCOUNT_CREATED), Toast.LENGTH_SHORT).show();

                                updateUI();
                            }

                            @Override
                            public void onServerException(String ex) {
                                Log.d(TAG, "onServerException: "+ex);
                                Toast.makeText(mContext, ex, Toast.LENGTH_SHORT).show();
                                registerBtnIsClicked = false;
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailureListener(String ex) {
                                Log.d(TAG, "onFailureListener: "+ex);
                                Toast.makeText(mContext, "falied", Toast.LENGTH_SHORT).show();
                                registerBtnIsClicked = false;
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    }
                }
            }
        });

    }




    /**
     *
     *          updating the ui to BaseProfileActivity
     *
     */

    private void updateUI() {

        Intent intent = new Intent(mContext, BaseProfileEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }





    /**
     *                              checking the validation of the data
     *
     *                             - email
     *                             - name
     *                             - password
     *
     * @param name
     * @param email
     * @param password
     * @return
     */

    private boolean checkingDataValidation(String name, String email, String password) {
        Log.d(TAG, "checkingDataValidation: checking validation of the data ");

        Validation validation = new Validation(mContext);

        if (!validation.Email(email))
            return false;

        if (!validation.Name(name))
            return false;

        if (!validation.Password(password))
            return false;
        return true;
    }


}
