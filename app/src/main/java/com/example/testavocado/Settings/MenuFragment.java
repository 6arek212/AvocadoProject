package com.example.testavocado.Settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.testavocado.Account_settings1;
import com.example.testavocado.BaseActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testavocado.Dialogs.ConfirmDialog;
import com.example.testavocado.Login.LoginActivity;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuFragment extends Fragment {
    public static final String TAG = "MenuFragment";

    private NavigationView navigationView;
    private LinearLayout viewProfileLayout;
    private Context mContext;
    private TextView txtv_first_lastn_name;
    private CircleImageView profile_pic;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mContext = getContext();
        navigationView = view.findViewById(R.id.navigationView);
        viewProfileLayout = view.findViewById(R.id.viewProfileLayout);
        txtv_first_lastn_name=(TextView)view.findViewById(R.id.userName);
        profile_pic=(CircleImageView)view.findViewById(R.id.profileImage) ;
        //getting name from sharedprefernces
        txtv_first_lastn_name.setText(HelpMethods.get_user_name_sharedprefernces(mContext));
        //setting profile pic in menu
        String profile_pic_path=HelpMethods.get_user_profile_pic_sharedprefernces(mContext);
        Glide.with(mContext)
                .load(profile_pic_path).
                centerCrop().
                error(R.drawable.profile_ic)
                .into(profile_pic);

        Log.d(TAG, "onCreateView:path= "+profile_pic_path);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                Fragment fragment;


                switch (id) {

                    case R.id.friends:
                        fragment = new FriendsFragment();
                        transaction.replace(R.id.settingsLayout, fragment)
                                .addToBackStack(getString(R.string.profile_fragment)).commit();
                        break;



                    case R.id.settings:
                        mContext.startActivity(new Intent(mContext, Account_settings1.class));

                        break;


                    case R.id.games:
                        fragment = new GamesFragment();
                        transaction.replace(R.id.settingsLayout, fragment)
                                .addToBackStack(getString(R.string.games_fragment)).commit();

                        break;


                    case R.id.logout:
                        ConfirmDialog confirmDialog = new ConfirmDialog();
                        confirmDialog.setTitle("Are you sure you want to logout ?");


                        confirmDialog.setOnConfirm(new ConfirmDialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                HelpMethods.deleteUserIdSharedPreferences(mContext,getActivity());
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });
                        confirmDialog.show(getFragmentManager(), getString(R.string.confirm_dialog));

                        break;
                }


                return false;
            }
        });


        viewProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  FragmentTransaction transaction = getFragmentManager().beginTransaction();

               // ProfileFragment fragment = new ProfileFragment();
              //  Bundle bundle = new Bundle();
              //  bundle.putInt(getString(R.string.current_user_profile), HelpMethods.checkSharedPreferencesForUserId(mContext));
             //   fragment.setArguments(bundle);

             //   transaction.replace(R.id.settingsLayout, fragment)
               //         .addToBackStack(getString(R.string.profile_fragment)).commit();
                BaseActivity.mViewPager.setCurrentItem(1);
            }
        });


        return view;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

}
