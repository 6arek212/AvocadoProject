package com.example.testavocado.Utils;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testavocado.R;

import java.util.List;

public class ImageListFragment extends Fragment {
    private static final String TAG = "ImageFragment";

    public List<String> imageUrls;
    public int index;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        initWidgets(view);
        return view;
    }





    private void initWidgets(View view) {
        Log.d(TAG, "initWidgets: "+imageUrls);
        viewPager = view.findViewById(R.id.viewPager);

        ImagesViewPagerNoClick adapter=new ImagesViewPagerNoClick(getContext(),imageUrls,getFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(index);
    }

}
