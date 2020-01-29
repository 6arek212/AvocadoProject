package com.example.testavocado.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.testavocado.Account_settings1;
import com.example.testavocado.BaseActivity;
import com.example.testavocado.Chat.SQLiteMethods;
import com.example.testavocado.Home.BottomSheetDialog;
import com.example.testavocado.ccc.ClearData;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testavocado.Dialogs.ConfirmDialog;
import com.example.testavocado.Login.LoginActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.testavocado.Chat.ChatActivity.SQL_VER;

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
        //navigation set color to icons
        navigationView.setItemIconTintList(null);
        viewProfileLayout = view.findViewById(R.id.viewProfileLayout);
        txtv_first_lastn_name=(TextView)view.findViewById(R.id.userName);
        profile_pic=(CircleImageView)view.findViewById(R.id.profileImage) ;
        //getting name from sharedprefernces
        txtv_first_lastn_name.setText(HelpMethods.get_user_name_sharedprefernces(mContext));
        //setting profile pic in menu
        String profile_pic_path=HelpMethods.get_user_profile_pic_sharedprefernces(mContext);
        Glide.with(mContext)
                .load(profile_pic_path).
                centerCrop()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.profile_ic)
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

                        HelpMethods.showAlert("Are you sure you want to logout ?", mContext,new HelpMethods.OnClickDialog() {
                            @Override
                            public void onClick() {
                                HelpMethods.deleteUserIdSharedPreferences(mContext,getActivity());
                                startActivity(new Intent(getActivity(), LoginActivity.class));


                                ClearData cd=new ClearData(requireActivity().getApplication());
                                cd.clearDb();

                                requireActivity().finish();
                            }
                        });
                        break;



                    case R.id.savedPosts:
                        fragment = new SavedPostsFragment();
                        transaction.replace(R.id.baseLayout, fragment)
                                .addToBackStack(getString(R.string.saved_posts_fragment)).commit();

                        break;
                }


                return false;
            }
        });



        viewProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
