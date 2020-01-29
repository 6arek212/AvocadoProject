package com.example.testavocado.Notification;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testavocado.Chat.ChatActivity;
import com.example.testavocado.Connection.ConnectionsActivity;
import com.example.testavocado.Connection.ShowRequestsFragment;
import com.example.testavocado.Models.Notification;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.PostFragment;
import com.example.testavocado.Utils.TimeMethods;
import com.example.testavocado.ccc.Chat3;
import com.example.testavocado.ccc.MessageFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NotificationAdapter";


    public static final int NOTIFICATION = 0;
    public static final int PROGRESS_BAR = 1;
    public static final int END_LIST = 2;

    private List<Notification> notifications = new ArrayList<>();
    private FragmentManager fragmentManager;
    private FragmentTransaction frt;
    private Context mContext;
    public boolean is_endOfPosts;


    public NotificationAdapter(FragmentManager fragmentManager, Context mContext) {
        this.fragmentManager = fragmentManager;
        this.mContext = mContext;
        this.is_endOfPosts = false;
    }


    public void addNull() {
        notifications.add(null);
        notifyItemInserted(notifications.size()-1);
    }


    public void removeProg() {
        if (notifications.get(notifications.size() - 1) == null) {
            notifications.remove(notifications.size() - 1);
            notifyItemRemoved(notifications.size());
        }
    }


    public void clear() {
        notifications.clear();
    }


    public void addSetOfNotifications(List<Notification> notificationList, int s) {
        notifications.addAll(notificationList);
        notifyItemRangeChanged(s, notificationList.size());
    }


    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);

        if (notifications.get(position) != null)
            return NOTIFICATION;
        else if (notifications.get(position) == null && !is_endOfPosts)
            return PROGRESS_BAR;
        else
            return END_LIST;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: " + i);
        View view;
        RecyclerView.ViewHolder vh;

        if (i == NOTIFICATION) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_notification_item, viewGroup, false);
            vh = new NotificationViewHolder(view);

        } else if (i == PROGRESS_BAR) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_item, viewGroup, false);
            vh = new ProgressViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_end_item, viewGroup, false);
            vh = new EndViewHolder(view);
        }

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == NOTIFICATION) {
            NotificationViewHolder v1 = (NotificationViewHolder) viewHolder;

            v1.mName.setText(notifications.get(i).getUser_sent_name());
            v1.mTime.setText(notifications.get(i).getNotification_datetime());
            v1.mNotificationType.setText(notifications.get(i).getType_txt());

            Glide.with(mContext)
                    .asBitmap()
                    .load(notifications.get(i).getUser_sent_profile_image())
                    .centerCrop()
                    .apply(
                            new RequestOptions()
                                    .placeholder(R.drawable.loading_img)
                                    .error(R.drawable.error)
                    )
                    .into(v1.mProfileImage);


            v1.link();

        } else if (viewHolder.getItemViewType() == PROGRESS_BAR && !is_endOfPosts) {
            ((ProgressViewHolder)viewHolder).progressBar.setIndeterminate(true);
        }
    }





    @Override
    public int getItemCount() {
        return notifications.size();
    }


     class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mTime, mNotificationType;
        CircleImageView mProfileImage;
        RelativeLayout link;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfileImage = itemView.findViewById(R.id.profileImage);
            mName = itemView.findViewById(R.id.userName);
            mNotificationType = itemView.findViewById(R.id.notificationType);
            mTime = itemView.findViewById(R.id.time);
            link = itemView.findViewById(R.id.notification_layout);
        }

        public void link() {
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to Post fragment

                    if (notifications.get(getAdapterPosition()).getNotification_type() < 5) {
                        Log.d(TAG, "onClick: " + notifications.get(getAdapterPosition()));
                        frt = fragmentManager.beginTransaction();
                        PostFragment fragment = new PostFragment();
                        fragment.post_id = notifications.get(getAdapterPosition()).getPost_id();
                        frt.replace(R.id.mainLayoutNotification, fragment).addToBackStack(mContext.getString(R.string.post_fragment))
                                .commit();
                    } else if (notifications.get(getAdapterPosition()).getNotification_type() == 5) {
                        // GO to connectionRequest
                        Fragment fragment= new ShowRequestsFragment();

                        fragmentManager.beginTransaction()
                                .replace(R.id.baseLayout,fragment)
                                .addToBackStack(mContext.getString(R.string.showRequests_fragment))
                                .commit();

                      //  mContext.startActivity(new Intent(mContext, ConnectionsActivity.class));

                    } else {
                        mContext.startActivity(new Intent(mContext, ChatActivity.class));

                        MessageFragment fragment=new MessageFragment();
                        Bundle bundle=new Bundle();


                        //fragment.setArguments();
                        fragmentManager.beginTransaction()
                                .replace(R.id.baseLayout,fragment)
                                .addToBackStack(mContext.getString(R.string.notification_fragment))
                                .commit();

                    }
                }
            });
        }
    }

    public  class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    public  class EndViewHolder extends RecyclerView.ViewHolder {
        TextView end;

        public EndViewHolder(View v) {
            super(v);
            end = (TextView) v.findViewById(R.id.end);
        }
    }
}
