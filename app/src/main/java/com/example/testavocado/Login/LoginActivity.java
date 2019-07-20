package com.example.testavocado.Login;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.user_login_register.register_page1_Fragment;
import com.example.testavocado.user_login_register.registeraccount_page;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
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
    EditText mPassword, mEmail;
    Button mLogin;
    RelativeLayout relativeLayout;
    TextView forgotPassword, createAccount;
    ImageView avocado;
    ProgressBar progressBar;


    //var
    Context mContext;


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adjustStatusBarColor();
        HelpMethods.closeKeyboard(LoginActivity.this);
        initWidgets();
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


                case R.id.password:
                    break;

                case R.id.email:
                    break;

                case R.id.login:
                    Log.d(TAG, "onClick: click login");
                    String email, password;
                    password = mPassword.getText().toString().trim();
                    email = mEmail.getText().toString().trim();
                    Validation validation = new Validation(mContext);

                    if (password.isEmpty() || email.isEmpty()) {
                        Snackbar.make(mLogin, getString(R.string.field_required), Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(mContext, "all field are required !", Toast.LENGTH_SHORT).show();
                    } else {
                        if (validation.Password(password) && validation.Email(email)) {
                            progressBar.setVisibility(View.VISIBLE);
                            HelpMethods.closeKeyboard(LoginActivity.this);
                            // loginWithEmailAndPassword(email, password);
                            LoginMethods.loginWithEmailAndPassword(email, password, new LoginMethods.onLogin() {
                                @Override
                                public void onSuccessListener(Setting setting) {
                                    HelpMethods.addSharedPreferences(setting,mContext);
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
                                    Toast.makeText(mContext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                                    mEmail.setText("");
                                    mPassword.setText("");
                                }
                            });
                        }
                    }

                    break;


                case R.id.createAccount:
                    Intent intent = new Intent(mContext, registeraccount_page.class);


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View, String>(mEmail, "edEmail");
                        pairs[1] = new Pair<View, String>(mPassword, "edPassword");
                        pairs[2] = new Pair<View, String>(mLogin, "btn");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);


                        startActivity(intent, options.toBundle());

                    } else {
                        startActivity(intent);

                    }

                    break;

                case R.id.forgotPassword:
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


}
