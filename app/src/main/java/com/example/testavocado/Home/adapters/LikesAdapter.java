package com.example.testavocado.Home.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testavocado.BaseActivity;
import com.example.testavocado.Models.Like;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikesAdapter extends RecyclerView.Adapter {
    private static final String TAG = "LikesRecyclerViewAdapte";

    public static final int LIKE_TYPE = 1;
    public static final int PROGRESSBAR = 2;
    public static final int END = 3;

    List<Like> likes = new ArrayList<>();

    private Context mContext;
    private int user_id;
    public boolean is_end;
    private FragmentManager fragmentManager;


    public LikesAdapter(Context mContext, int user_id,FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.user_id = user_id;
        this.fragmentManager=fragmentManager;
    }

    public void clear() {
        likes.clear();
        notifyDataSetChanged();
    }


    public void addNull() {
        likes.add(null);
        notifyItemInserted(likes.size());
    }

    public void removeProg() {
        if (likes.get(likes.size() - 1) == null) {
            likes.remove(likes.size() - 1);
            notifyItemRemoved(likes.size());
        }
    }





    public void addLikesSet(List<Like> likeList, int s) {
        likes.addAll(likeList);
        notifyItemRangeInserted(s, likeList.size());
    }


    @Override
    public int getItemViewType(int position) {
        if (likes.get(position) != null)
            return LIKE_TYPE;
        else if (likes.get(position) == null && !is_end)
            return PROGRESSBAR;
        else
            return END;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        RecyclerView.ViewHolder vh;

        Log.d(TAG, "onCreateViewHolder: a " + i);

        if (i == LIKE_TYPE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_like, viewGroup, false);
            vh = new LikeViewHolder(view);
        }
        else if (i == PROGRESSBAR) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progressbar_item, viewGroup, false);

            vh = new ProgressViewHolder(view);

        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_end_item, viewGroup, false);
            vh = new EndViewHolder(view);
        }

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + holder.getItemViewType() + "  " + (holder instanceof LikeViewHolder) + "  " + (holder instanceof ProgressViewHolder) + "  " + (holder instanceof EndViewHolder));


        if (holder.getItemViewType() == LIKE_TYPE) {
            LikeViewHolder vh = (LikeViewHolder) holder;

            vh.mUserName.setText(likes.get(position).getUser_name());
            vh.mTime.setText(TimeMethods.convertDateTimeFormatDateOnly(likes.get(position).getTime()));


            Glide.with(mContext)
                    .load(likes.get(position).getProfile_image())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(vh.mProfileImage);

            attachProfileOnClick(vh.profileLink, position);
        }
    }






    private void attachProfileOnClick(RelativeLayout profileLink, final int i) {
        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();

                if (user_id == likes.get(i).getUser_id())
                    fragment.is_current_user = true;
                else
                    fragment.is_current_user = false;

                fragment.incoming_user_id = likes.get(i).getUser_id();
                FragmentTransaction tr = fragmentManager.beginTransaction();
                tr.replace(R.id.likeDislikeLayout, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();

            }
        });
    }


    @Override
    public int getItemCount() {
        return likes.size();
    }


    public static class LikeViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mTime, mUserName;
        RelativeLayout profileLink;

        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.userName);
            mTime = itemView.findViewById(R.id.time);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            profileLink = itemView.findViewById(R.id.profileLink);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    public static class EndViewHolder extends RecyclerView.ViewHolder {
        TextView end;

        public EndViewHolder(View v) {
            super(v);
            end = (TextView) v.findViewById(R.id.end);
        }
    }
}
