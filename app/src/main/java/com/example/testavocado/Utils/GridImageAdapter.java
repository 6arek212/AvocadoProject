package com.example.testavocado.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.testavocado.R;


import java.util.ArrayList;

public class GridImageAdapter extends ArrayAdapter<String> {
    private static final String TAG = "GridImageAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context context, int layoutResource, String mAppend, ArrayList<String> imgURLs) {
        super(context, layoutResource,imgURLs);
        this.mContext = context;
        mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.mAppend = mAppend;
        this.imgURLs = imgURLs;
    }


    private static class  ViewHolder{
        SquareImageView image;
        ProgressBar progressBar;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        final ViewHolder viewHolder;


        // Viewholder build pattern (Similar to recyclerview)

        if(convertView==null){
            convertView=mInflater.inflate(layoutResource,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.image=convertView.findViewById(R.id.gridImageView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        String imgURL=getItem(position);


        Log.d(TAG, "getView: "+mAppend + imgURL);




        Glide.with(getContext())
                .load(imgURL)
                .centerCrop()
                .error(R.drawable.error)
                .placeholder(R.drawable.loading_img)
                .into(viewHolder.image);


        return convertView;
    }
}
