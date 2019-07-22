package com.example.testavocado.DamkaGame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testavocado.R;

public class DamkaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new myGame(this));
    }
}
