package com.example.testavocado.EditeInfo;

import android.content.Intent;

import com.example.testavocado.Models.Setting;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;


import com.example.testavocado.BaseActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.SectionPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseProfileEditActivity extends AppCompatActivity implements onClickNextListener {
    private static final String TAG = "BaseProfileEditActivity";


    @Override
    public void onClickArrow() {
        int p;
        p = viewPager.getCurrentItem();
        if(p>0){
            position--;
            viewPager.setCurrentItem(position);
        }

    }

    @Override
    public void onClickNext() {

        int p;
        p = viewPager.getCurrentItem();
        if (p < adapter.getCount() - 1) {
            position++;
            viewPager.setCurrentItem(position);
        }
        else {
            Log.d(TAG, "onClickNext: navigating to ");

            // if he came from profile activity
            // if he came from register activity
            HelpMethods.addSharedPreferences(setting,BaseProfileEditActivity.this);
            startActivity(new Intent(BaseProfileEditActivity.this, BaseActivity.class));
            finish();
        }
    }




    //widgets
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private SectionPagerAdapter adapter;
    public static int position = 0;
    public static int USER_ID=-1;
    public static Setting setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_profile_edit);

        USER_ID=HelpMethods.checkSharedPreferencesForUserId(this);

        initWidgets();



    }




    private void initWidgets() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new SectionPagerAdapter(getSupportFragmentManager());
        getFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.clearOnTabSelectedListeners();







        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                position=i;
                Log.d(TAG, "onPageSelected: "+i +"  position "+position);
                FragmentLifecycle fragmentToShow = (FragmentLifecycle)adapter.getItem(i);

                if (position==adapter.getCount()-1)
                {
                    fragmentToShow.onLastPageSelected();
                }
                if (position==0)
                    fragmentToShow.onFirstPage();
                else if(position !=0)
                    fragmentToShow.showArrow();


            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d(TAG, "onPageScrollStateChanged: "+i);
            }
        });

    }


    private List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<>();
        adapter.addFragment(new UserInfoFragment());
        adapter.addFragment(new ProfilePhotoUploadFragment());
        adapter.addFragment(new ProfileLocationFragment());
        adapter.addFragment(new GetStartedFragment());

        return fragments;
    }

}
