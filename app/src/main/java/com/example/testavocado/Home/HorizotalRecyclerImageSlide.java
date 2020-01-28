package com.example.testavocado.Home;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.testavocado.R;

import java.util.ArrayList;
import java.util.List;

public class HorizotalRecyclerImageSlide extends RecyclerView.Adapter<HorizotalRecyclerImageSlide.ViewHolder>{
    private static final String TAG = "HorizotalRecyclerImageSlide";

    public List<Uri> images=new ArrayList<>();
    private Context context;


    public HorizotalRecyclerImageSlide(Context context) {
        this.context = context;
    }


    public void addImage(Uri uri, int i){
        images.add(uri);
        notifyItemInserted(i);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_image_viewpager_post,viewGroup,false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       // viewHolder.mImage.setImageURI(images.get(i));

       // UniversalImageLoader.setImage(images.get(i).toString(),viewHolder.mImage,null,"");


        Glide.with(context)
                .load(images.get(i).toString())
                .centerCrop()
                .error(R.drawable.error)
                .into(viewHolder.mImage);


        viewHolder.deleteImage();
    }




    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mDelete,mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDelete=itemView.findViewById(R.id.deleteImage);
            mImage=itemView.findViewById(R.id.postImage);
        }

        public void deleteImage() {
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        images.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                    catch (IndexOutOfBoundsException e){
                        Log.e(TAG, "onClick: "+e.getMessage() );
                    }
                }
            });
        }
    }
}
