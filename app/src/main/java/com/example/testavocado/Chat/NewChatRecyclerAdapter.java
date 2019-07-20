package com.example.testavocado.Chat;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testavocado.Models.ChatUser;
import com.example.testavocado.R;

import java.util.ArrayList;
import java.util.List;

public class NewChatRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NewChatRecyclerAdapter";

    private List<ChatUser> users = new ArrayList<ChatUser>();
    private Context context;
    public boolean is_endOfPosts;


    public NewChatRecyclerAdapter(Context context) {
        this.context = context;
        is_endOfPosts = false;
    }


    /**
     * adding null to the list to show progress bar
     */
    public void addNull() {
        users.add(null);
        notifyItemInserted(users.size() - 1);
    }


    /**
     * removing progressbar
     */
    public void removeProg() {
        try {
            if (users.get(users.size() - 1) == null) {
                users.remove(users.size() - 1);
                notifyItemRemoved(users.size());
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "removeProg: " + e.getMessage());
        }
    }


    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);
        if (users.get(position) != null)
            return 0; //requests
        else if (users.get(position) == null && !is_endOfPosts)
            return 1; //reached bottom not the end progressBar
        else
            return 2; //end

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        RecyclerView.ViewHolder vh;

        if (i == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friends_item, viewGroup, false);
            vh = new UserViewHolder(v);
        } else if (i == 1) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progressbar_item, viewGroup, false);
            vh = new ProgressViewHolder(v);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_end_item, viewGroup, false);
            vh = new EndViewHolder(view);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int type = viewHolder.getItemViewType();

        if (type == 0) {
            UserViewHolder v1=(UserViewHolder)viewHolder;
            ChatUser chatUser = users.get(i);

            v1.mUserName.setText(chatUser.getUser_first_name() + " " + chatUser.getUser_last_name());

            Glide.with(context)
                    .load(chatUser.getProfile_photo())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(v1.mProfileImage);

            onClick(v1.mContainer, i);
        }
        else if (type==1) {
            Log.d(TAG, "onBindViewHolder: bottom ");
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }
    }


    public void addUsers(List<ChatUser> chatUsers, int s) {
        users.addAll(chatUsers);
        notifyItemRangeInserted(s, chatUsers.size());
    }


    public void clearList() {
        users.clear();
        notifyDataSetChanged();
    }


    /**
     * navigating to the chat conversation fragment
     *
     * @param relativeLayout
     * @param index
     */
    private void onClick(RelativeLayout relativeLayout, final int index) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();

                ConversationFragment fragment = new ConversationFragment();
                fragment.chatUser = users.get(index);
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ;
                transaction.replace(R.id.mainLayoutChatActivity, fragment);
                transaction.addToBackStack(context.getString(R.string.conversation_fragment));
                transaction.commit();

            }
        });

    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView mProfileImage;
        TextView mUserName;
        RelativeLayout mContainer;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            mUserName = itemView.findViewById(R.id.userName);
            mContainer = itemView.findViewById(R.id.mainLayout);
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
