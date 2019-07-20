package com.example.testavocado.Notification;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testavocado.R;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private static final int ACTIVITY_NUM=3;

    //vars
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext= NotificationActivity.this;
    }

}
