package com.example.testavocado.post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testavocado.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateHorizotalRecyclerImageSlide extends RecyclerView.Adapter<UpdateHorizotalRecyclerImageSlide.ViewHolder> {
    private static final String TAG = "HorizotalRecyclerImageSlide";

    public List<Object> images = new ArrayList<>();
    private Context context;


    public UpdateHorizotalRecyclerImageSlide(Context context) {
        this.context = context;
    }


    public void addImage(Object ob, int i) {
        images.add(ob);
        notifyItemInserted(i);
    }

    public void addAllImages(List<String> list, int i) {
        images.addAll(i,list);
        notifyItemRangeInserted(i,list.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_image_viewpager_post, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // viewHolder.mImage.setImageURI(images.get(i));

        // UniversalImageLoader.setImage(images.get(i).toString(),viewHolder.mImage,null,"");


        Glide.with(context)
                .asBitmap()
                .load(images.get(i).toString())
                .centerCrop()
                .apply(
                        new RequestOptions()
                                .placeholder(R.drawable.loading_img)
                                .error(R.drawable.error)
                )
                .into(viewHolder.mImage);


    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mDelete, mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDelete = itemView.findViewById(R.id.deleteImage);
            mImage = itemView.findViewById(R.id.postImage);
            mDelete.setVisibility(View.GONE);
        }

    }
}
