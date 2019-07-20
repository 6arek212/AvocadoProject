package com.example.testavocado.Utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.testavocado.R;


import java.util.List;

public class ImagesViewPagerClick extends PagerAdapter {
    private static final String TAG = "ImagesViewPagerClick";

    private Context context;
    private final List<String> imageUrls;
    private FragmentManager fragmentManager;


    public ImagesViewPagerClick(Context context, List<String> imageUrls, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager=fragmentManager;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);

        Log.d(TAG, "instantiateItem: " + position);



            Log.d(TAG, "instantiateItem: adding image "+position+"   "+imageUrls.get(position));

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();


        Glide.with(context)
                .load(imageUrls.get(position))
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .error(R.drawable.error)
                .into(imageView);




        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction frt = fragmentManager.beginTransaction();
                    frt.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
                    ImageListFragment fragment = new ImageListFragment();
                    fragment.imageUrls=imageUrls;
                    fragment.index=position;
                    frt.replace(R.id.baseLayout, fragment).addToBackStack(context.getString(R.string.image_list_fragment))
                            .commit();
                }
            });


        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
