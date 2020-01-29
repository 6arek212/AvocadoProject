package com.example.testavocado.Utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testavocado.R;


public class ImageFragment extends Fragment {
    private static final String TAG = "ImageFragment";

    private ImageView mImage;
    public String imageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        initWidgets(view);
        return view;
    }


    private void initWidgets(View view) {
        Log.d(TAG, "initWidgets: " + imageUrl);
        mImage = view.findViewById(R.id.image);

        if (getContext() != null)
            Glide.with(getContext())
                    .asBitmap()
                    .load(imageUrl)
                    .centerCrop()
                    .apply(
                            new RequestOptions()
                                    .placeholder(R.drawable.loading_img)
                                    .error(R.drawable.error)
                    )
                    .into(mImage);

    }


}
