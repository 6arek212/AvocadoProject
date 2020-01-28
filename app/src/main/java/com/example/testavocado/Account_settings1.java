package com.example.testavocado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testavocado.Settings.SettingsActivity;

public class Account_settings1 extends AppCompatActivity {
    public static final String TAG = "Account_settings1";
    private Context mcontext=Account_settings1.this;
    private LinearLayout linearLayout_personal_iformation,linearLayout_privacy_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings1);
        load_widgets();
    }

    public void load_widgets()
    {

        linearLayout_personal_iformation=(LinearLayout)findViewById(R.id.linearlayout_personal_information);
        linearLayout_privacy_settings=(LinearLayout)findViewById(R.id.linearlayout_privacy_settings);
        //set on click
        linearLayout_privacy_settings.setOnClickListener(new onclick());
        linearLayout_personal_iformation.setOnClickListener(new onclick());
    }

    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.linearlayout_personal_information:
                    mcontext.startActivity(new Intent(mcontext, Personal_information.class));
                    break;

                case R.id.linearlayout_privacy_settings:
                    mcontext.startActivity(new Intent(mcontext, SettingsActivity.class));
                    break;
            }
        }
    }
}
