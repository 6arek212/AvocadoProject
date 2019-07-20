package com.example.testavocado.Home.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testavocado.R;
import com.example.testavocado.Utils.SectionStatePagerAdapter;
import com.example.testavocado.Utils.TimeMethods;

public class LikesDislikesFragment extends Fragment {

    private TabLayout mtab;
    private ViewPager pager;
    public static int post_id;
    public static  String datetime;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_like_dislike,container,false);

        initWidgets(view);



        return view;
    }



    private void initWidgets(View view) {
        mtab=view.findViewById(R.id.tablayout);
        pager=view.findViewById(R.id.viewPager);
        mtab.setupWithViewPager(pager);
        datetime= TimeMethods.getUTCdatetimeAsString();

        SectionStatePagerAdapter adapter=new SectionStatePagerAdapter(getFragmentManager());
        adapter.addFragment(new LikesFragment());
        adapter.addFragment(new DisLikesFragment());

        pager.setAdapter(adapter);
        mtab.getTabAt(0).setIcon(R.drawable.like);
        mtab.getTabAt(1).setIcon(R.drawable.dislike);

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle bundle=getArguments();
        post_id=bundle.getInt(context.getString(R.string.post_id));

    }
}
