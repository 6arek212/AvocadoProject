package com.example.testavocado.Connection;

import android.content.Context;

import com.example.testavocado.Service.BackgroundService;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.SectionStatePagerAdapter;

public class ConnectionsActivity extends AppCompatActivity {
    //widgets
    private ViewPager viewPager;
    private TabLayout tableLayout;
    public static int user_current_id;


    //vars
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        mContext=this;
        user_current_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        initViewPager();
    }


    private void initViewPager( ) {
        viewPager = findViewById(R.id.viewPager);
        tableLayout =findViewById(R.id.tablayout);

        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new SearchConnectionFragment());
        adapter.addFragment(new ShowRequestsFragment());

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try{
            stopService();

        }catch (Exception e){

        }    }

    public void stopService() {
        BackgroundService.stopThis();
        stopService(new Intent(this, BackgroundService.class));
    }
}
