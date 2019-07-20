package com.example.testavocado.Connection;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testavocado.R;
import com.example.testavocado.Utils.SectionStatePagerAdapter;

public class ConnectionsFragment extends Fragment {
    private static final String TAG = "ConnectionsFragment";
    //widgets
    private ViewPager viewPager;
    private TabLayout tableLayout;


    //vars
    private Context mContext;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.activity_add_connections,container,false);

        mContext=getContext();
        initViewPager(view);

        return view;
    }



    private void initViewPager(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tableLayout =view.findViewById(R.id.tablayout);

        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getFragmentManager());

        adapter.addFragment(new SearchConnectionFragment());
        adapter.addFragment(new ShowRequestsFragment());

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onStart() {
        super.onStart();
        initViewPager(view);
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }



    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }


}
