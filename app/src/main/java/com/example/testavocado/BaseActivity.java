package com.example.testavocado;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.Chat.ChatActivity;
import com.example.testavocado.Connection.ConnectionsActivity;

import com.example.testavocado.Home.Fragments.MainFeedFragment;
import com.example.testavocado.Login.LoginMethods;
import com.example.testavocado.Notification.NotificationFragment;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.Service.ExampleJobService;
import com.example.testavocado.Settings.MenuFragment;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.LocationMethods;
import com.example.testavocado.Utils.PostFragment;
import com.example.testavocado.Utils.SectionStatePagerAdapter;
import com.example.testavocado.Utils.TimeMethods;



public class BaseActivity extends AppCompatActivity  {
    private static final String TAG = "BaseActivity";

    public static final boolean ONLINE_STATE=true;
    public static final boolean OFFLINE_STATE=false;


    //widgets
    public static ViewPager mViewPager;
    private ImageView mChat;
    private EditText mSearch;

    //fragments
    private MenuFragment menuFragment;
    private MainFeedFragment mainFeedFragment;
    private ProfileFragment profileFragment;
    private NotificationFragment notificationFragment;


    //vars
    private LocationManager manager;
    private TabLayout mtab;
    private SectionStatePagerAdapter adapter;
    private int user_id;
    private Context mContext;

    //service vars
    public static String datetime;
    public static int offset;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if(savedInstanceState==null) {
            user_id = HelpMethods.checkSharedPreferencesForUserId(this);
            mContext = this;
            initWidgets();
            setViewpager();
            serviceIntents();
        }
    }





    //handling service intents
    private void serviceIntents(){
        Intent intent = getIntent();
        FragmentTransaction frt = getSupportFragmentManager().beginTransaction();

        Bundle intentBundle = intent.getExtras();
        if (intentBundle != null) {
            if (intentBundle.get(getString(R.string.post_fragment)) != null) {
                mViewPager.setCurrentItem(0);
                int post_id = intentBundle.getInt(getString(R.string.post_fragment));
                PostFragment fragment = new PostFragment();
                fragment.post_id = post_id;
                frt.replace(R.id.mainLayoutPosts, fragment).addToBackStack(getString(R.string.post_fragment))
                        .commit();

            } else if (intentBundle.get(getString(R.string.connection_requests_fragment)) != null) {
                mViewPager.setCurrentItem(2);

            } else if (intentBundle.get(getString(R.string.conversation_fragment)) != null) {
                mViewPager.setCurrentItem(1);
            }
        }
    }







    /**
     *          init all widgets
     *
     */
    private void initWidgets(){
        mChat=findViewById(R.id.chat);
        mViewPager = findViewById(R.id.mainViewPager);
        mtab = findViewById(R.id.tablayout);
        mSearch=findViewById(R.id.search);


        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ConnectionsActivity.class));
            }
        });


        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ChatActivity.class));
            }
        });
    }





    /**
     * set viewpager and tablayout
     */

    public void setViewpager() {
        Log.d(TAG, "setviewpager: Set viewpager");
        adapter = new SectionStatePagerAdapter(getSupportFragmentManager());

        mainFeedFragment = new MainFeedFragment();
        notificationFragment = new NotificationFragment();
        menuFragment = new MenuFragment();
        profileFragment = new ProfileFragment();
        profileFragment.incoming_user_id=user_id;
        profileFragment.is_current_user=true;

        adapter.addFragment(mainFeedFragment);
        adapter.addFragment(profileFragment);
        adapter.addFragment(notificationFragment);
        adapter.addFragment(menuFragment);


        mViewPager.setAdapter(adapter);
        mtab.setupWithViewPager(mViewPager);
        mtab.getTabAt(0).setIcon(R.drawable.mainpage_ic);
        mtab.getTabAt(1).setIcon(R.drawable.myprofile_ic);
        mtab.getTabAt(2).setIcon(R.drawable.notification_ic);
        mtab.getTabAt(3).setIcon(R.mipmap.menu_icon_gray);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: "+position+"  "+mtab.getSelectedTabPosition());

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        mtab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected: ");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int i=tab.getPosition();
                Log.d(TAG, "onTabReselected: "+i+"  "+mViewPager.getCurrentItem());


                if(getSupportFragmentManager().getBackStackEntryCount()>0){
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                else{
                    if(i==0&&mViewPager.getCurrentItem()==0){
                        scrollToTheTop(mainFeedFragment.mRecyclerView);
                    }
                    else if(i==1&&mViewPager.getCurrentItem()==1){

                        scrollToTheTop(profileFragment.mRecyclerView);
                    }
                    else if(i==2&&mViewPager.getCurrentItem()==2){

                        scrollToTheTop(notificationFragment.mRecyclerView);
                    }
                }
            }
        });

        //mViewPager.setOffscreenPageLimit(0);
    }





    private void scrollToTheTop(RecyclerView recyclerView){
        recyclerView.smoothScrollToPosition(0);
    }






    /**
     * starting a background Service
     */
    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(1234, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(1234);
        Log.d(TAG, "Job cancelled");
    }


    /**
     *
     *          lifecycle  aware
     *
     *          starting background service
     *          handling user online state
     *
     */

    @Override
    protected void onResume() {
        super.onResume();
        cancelJob();
        getLocation();
        updateOnlineState(ONLINE_STATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduleJob();
    }


    @Override
    protected void onStop() {
        super.onStop();
        datetime = TimeMethods.getUTCdatetimeAsString();
        offset = 0;
        updateOnlineState(OFFLINE_STATE);
    }






    /**
     *              updating user online state
     *
     * @param state
     */
    private void updateOnlineState(boolean state){
        LoginMethods.updateOnlineState(user_id, state, new LoginMethods.OnUpdateUserOnlineListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: user is online");
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: "+ex);
            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: "+ex);
            }
        });
    }








    /**
     *          getting current location
     *
     */
    private void getLocation() {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location l1 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location l2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location l3 = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


        Log.d(TAG, "updateLocation12: " + l1);
        if (l1 != null) {
            Log.d(TAG, "updateLocation12: got it l1");
            updateLocationInServer(l1.getLatitude(), l1.getLongitude());
        } else if (l2 != null) {
            Log.d(TAG, "updateLocation12: got it l2 " + l2);
            updateLocationInServer(l2.getLatitude(), l2.getLongitude());
        } else if (l3 != null) {
            Log.d(TAG, "updateLocation12: got it l3 " + l3);
            updateLocationInServer(l3.getLatitude(), l3.getLongitude());
        }

    }







    /**
     *
     *          updating the user current location
     *
     * @param lat
     * @param longitude
     */
    public void updateLocationInServer(double lat, double longitude) {
        LocationMethods.updateLocation(user_id, lat, longitude, new LocationMethods.OnUpdateListener() {
            @Override
            public void onUpdate() {
                Log.d(TAG, "onUpdate: ");
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: " + ex);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView txtv_bio=(TextView)findViewById(R.id.textv_aboutme_merge_fragment_myprofile_center);
        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
                String bio_text = data.getStringExtra("bio");
                txtv_bio.setText(bio_text);
            }
            else
            {

            }
        }
    }
}
