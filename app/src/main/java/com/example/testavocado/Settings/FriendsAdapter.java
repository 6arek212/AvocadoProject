package com.example.testavocado.Settings;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.BaseActivity;
import com.example.testavocado.Connection.ConnectionsHandler;
import com.example.testavocado.Dialogs.ConfirmDialog;
import com.example.testavocado.Models.Friend;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "FriendsAdapter";

    public boolean is_endOfPosts = false;
    private Context mContext;
    private List<Friend> friendList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int current_user_id;


    public FriendsAdapter(Context mContext, FragmentManager fragmentManager, int current_user_id) {
        is_endOfPosts = false;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.current_user_id = current_user_id;
    }


    /**
     * add friend
     *
     * @param s
     */
    public void addSetOfFriends(List<Friend> friends, int s) {
        Log.d(TAG, "addNewSetUserToAdd: ");
        friendList.addAll(friends);
        notifyItemRangeInserted(s, friends.size());
    }


    /**
     * clear list
     */

    public void clearList() {
        friendList.clear();
        notifyDataSetChanged();
    }


    /**
     * adding null to the list to show progress bar
     */
    public void addNull() {
        friendList.add(null);
        notifyItemInserted(friendList.size() - 1);
    }


    /**
     * removing progressbar
     */
    public void removeProg() {
        try {
            if (friendList.get(friendList.size() - 1) == null) {
                friendList.remove(friendList.size() - 1);
                notifyItemRemoved(friendList.size());
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "removeProg: " + e.getMessage());
        }
    }


    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);

        if (friendList.get(position) != null)
            return 0; //requests
        else if (friendList.get(position) == null && !is_endOfPosts)
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_item, viewGroup, false);
            vh = new FriendViewHolder(v);
        } else if (i == 1) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_item, viewGroup, false);
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

            FriendViewHolder v1 = (FriendViewHolder) viewHolder;

            v1.mUserName.setText(friendList.get(i).getUser_name());

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
            circularProgressDrawable.setArrowScale(20);
            circularProgressDrawable.setBackgroundColor(mContext.getResources().getColor(R.color.colorEditTextGrey));
            circularProgressDrawable.start();

            Glide.with(mContext)
                    .load(friendList.get(i).getUser_profile_image())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(v1.mProfileImage);

            v1.deleteFriend();
            v1.profile();

        } else if (type == 1) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }
    }





    @Override
    public int getItemCount() {
        return friendList.size();
    }


    class FriendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mUserName;
        ImageButton mDelete;
        ConstraintLayout mProfileLink;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            mUserName = itemView.findViewById(R.id.userName);
            mDelete = itemView.findViewById(R.id.btnDelete);
            mProfileLink = itemView.findViewById(R.id.profileLink);
        }

        public void deleteFriend() {
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setTitle("are you sure you want to delete " + friendList.get(getAdapterPosition()).getUser_name());
                    confirmDialog.setOnConfirm(new ConfirmDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            ConnectionsHandler.RemoveFriend(friendList.get(getAdapterPosition()).getRequest_id(), friendList.get(getAdapterPosition()).getUser_id(), current_user_id
                                    , new ConnectionsHandler.OnRemovingFriendListener() {
                                        @Override
                                        public void onSuccessListener() {
                                            Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                                            friendList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            confirmDialog.dismiss();
                                        }

                                        @Override
                                        public void onServer(String ex) {
                                            Log.d(TAG, "onServer: " + ex);
                                            confirmDialog.dismiss();
                                        }

                                        @Override
                                        public void onFailure(String ex) {
                                            Log.d(TAG, "onFailure: " + ex);
                                            confirmDialog.dismiss();
                                        }
                                    });
                        }
                    });
                    confirmDialog.show(fragmentManager, mContext.getString(R.string.commentsDialog));
                }
            });
        }

        public void profile() {
            mProfileLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment fragment = new ProfileFragment();
                    fragment.is_current_user = false;
                    fragment.incoming_user_id = friendList.get(getAdapterPosition()).getUser_id();
                    FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction tr = fragmentManager.beginTransaction();
                    tr.replace(R.id.friendLayout, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
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
