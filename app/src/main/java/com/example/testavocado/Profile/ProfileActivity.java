package com.example.testavocado.Profile;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testavocado.R;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;


    //vars
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = ProfileActivity.this;


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ProfileFragment fragment=new ProfileFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction tr=fragmentManager.beginTransaction();
        tr.replace(R.id.mainLayout,fragment).commit();
    }



}
