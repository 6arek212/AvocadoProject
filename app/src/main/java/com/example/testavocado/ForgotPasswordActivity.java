package com.example.testavocado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testavocado.Utils.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    //widgets
    private EditText mEmail;
    private Button mSend;
    private ProgressBar mProgressBar;


    //vars
    private Context mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initWidgets();
    }


    /**
     *          init all the widget and attaching a click listener to the send button
     *          and handling the password recovery
     *
     */

    private void initWidgets() {
        mEmail=findViewById(R.id.email);
        mSend=findViewById(R.id.send);
        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mContext=this;




        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation=new Validation(mContext);
                String email=mEmail.getText().toString();

                if(validation.Email(email)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    PasswordMethods.sendEmailForPasswordRecovery(email, new PasswordMethods.OnSendingEmailListener() {
                        @Override
                        public void onSent() {
                            Toast.makeText(mContext, "email has been sent", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onServerError(String exception) {
                            Log.d(TAG, "onServerError: "+exception);
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(String exception) {
                            Log.d(TAG, "onFailure: "+exception);
                            Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }
}
