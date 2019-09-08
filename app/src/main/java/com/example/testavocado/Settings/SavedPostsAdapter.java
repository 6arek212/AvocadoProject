package com.example.testavocado.Settings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.SavedPost;
import com.example.testavocado.R;
import com.example.testavocado.Utils.PostFragment;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;

public class SavedPostsAdapter  extends RecyclerView.Adapter {
    private static final String TAG = "SavedPostsAdapter";

    private List<SavedPost> savedPosts=new ArrayList<>();

    private static final int SAVED_POST=1;
    private static  final int END=2;
    private static  final int PROG=3;

    public boolean is_endOfPosts;
    private FragmentManager fragmentManager;
    private Context mContext;

    public SavedPostsAdapter(FragmentManager fragmentManager, Context mContext) {
        this.fragmentManager = fragmentManager;
        this.mContext = mContext;
    }



    public void addNull() {
        savedPosts.add(null);
        notifyItemInserted(savedPosts.size());
    }

    public void removeProg() {
        if (savedPosts.get(savedPosts.size() - 1) == null) {
            savedPosts.remove(savedPosts.size() - 1);
            notifyItemRemoved(savedPosts.size());
        }
    }

    public void clear() {
        savedPosts.clear();
    }


    public void addSetOfSavedPosts(List<SavedPost> savedPostList, int s) {
        savedPosts.addAll(savedPostList);
        notifyItemRangeInserted(s, savedPostList.size());
    }




    @Override
    public int getItemViewType(int position) {
        if (savedPosts.get(position)!=null)
            return SAVED_POST;
        else if (savedPosts.get(position) == null && !is_endOfPosts)
            return PROG;
        else
            return END;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder vh;

       if (viewType == PROG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);
            vh = new ProgressViewHolder(v);
        } else if (viewType == SAVED_POST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_saved_post, parent, false);
            vh = new SavedPostViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_end_item, parent, false);
            vh = new EndViewHolder(view);
        }

        return vh;
    }





    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SavedPostViewHolder){
            SavedPostViewHolder vh=(SavedPostViewHolder)holder;

            vh.mDescription.setText(savedPosts.get(position).getSaved_datetime());
            vh.mDescription.setText(savedPosts.get(position).getDescription());
            deleteSavedPost(position,vh);
            openPostFragment(position,vh);
        }

    }





    @Override
    public int getItemCount() {
        return savedPosts.size();
    }




    private void deleteSavedPost(final int i,SavedPostViewHolder viewHolder){
        viewHolder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostMethods.deleteSavePost(savedPosts.get(i).getSaved_post_id(), new PostMethods.OnDeleteingSavinedPostListener() {
                    @Override
                    public void onDeleted() {
                        Log.d(TAG, "onDeleted:  saved post deleted ");
                        savedPosts.remove(i);
                        notifyItemRemoved(i);
                    }

                    @Override
                    public void onServerException(String ex) {
                        Log.d(TAG, "onServerException: "+ex);

                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: "+ex);

                    }
                });
            }
        });

    }





    private  void openPostFragment(final int i ,SavedPostViewHolder vh){
        vh.frontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment fragment=new PostFragment();
                fragment.post_id=savedPosts.get(i).getPost_id();
                FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.replace(R.id.baseLayout,fragment).addToBackStack(mContext.getString(R.string.post_fragment))
                        .commit();
            }
        });

    }





    class SavedPostViewHolder extends RecyclerView.ViewHolder {
        TextView mDescription,mDatetime;
        FrameLayout delete_layout;
        ConstraintLayout frontLayout;

        public SavedPostViewHolder(@NonNull View itemView) {
            super(itemView);

            mDescription=itemView.findViewById(R.id.description);
            mDatetime=itemView.findViewById(R.id.datetime);
            delete_layout=itemView.findViewById(R.id.delete_layout);
            frontLayout=itemView.findViewById(R.id.front_layout);
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
