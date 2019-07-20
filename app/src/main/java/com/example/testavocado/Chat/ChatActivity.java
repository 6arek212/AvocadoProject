package com.example.testavocado.Chat;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testavocado.R;

public class ChatActivity extends AppCompatActivity  {
    private static final String TAG = "ChatActivity";
    private static final int ACTIVITY_NUM = 1;
    public static final int SQL_VER=1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        ChatFragment fragment=new ChatFragment();
        transaction.replace(R.id.mainLayoutChatActivity,fragment).commit();

    }



}
