package com.example.testavocado.XOGame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class XOActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new XOview(this));
    }
}
