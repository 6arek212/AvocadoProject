package com.example.testavocado.Connection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.Models.UserAdd;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewRequestsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecyclerViewRequestsAda";

    public interface OnLoadMoreItemsListener {
        void onLoadMoreItems();
    }


    private Context mContext;
    private List<UserAdd> requestList = new ArrayList<>();
    private OnLoadMoreItemsListener onLoadMoreItemsListener;
    public boolean is_endOfPosts;
    private int userId;
    private FragmentManager fragmentManager;


    /**
     * adding null to the list to show progress bar
     */
    public void addNull() {
        Log.d(TAG, "addNull: adding null ");
        requestList.add(null);
        notifyItemInserted(requestList.size() - 1);
    }


    /**
     * removing progressbar
     */
    public void removeProg() {
        Log.d(TAG, "removeProg: list size " + requestList.size());
        try{
            if (requestList.get(requestList.size() - 1) == null) {
                requestList.remove(requestList.size() - 1);
                notifyItemRemoved(requestList.size());
            }
        }catch (Exception e){

        }
    }


    public RecyclerViewRequestsAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        userId = HelpMethods.checkSharedPreferencesForUserId(mContext);
        Log.d(TAG, "RecyclerViewRequestsAdapter: " + userId);
        this.fragmentManager = fragmentManager;
    }


    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);


        if (requestList.get(position) != null)
            return 0; //requests
        else if (requestList.get(position) == null && !is_endOfPosts)
            return 1; //reached bottom not the end
        else
            return 2; //end

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        RecyclerView.ViewHolder vh;

        if (i == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_connection_request_item_test, viewGroup, false);
            vh = new RequestsViewHolder(v);
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
            RequestsViewHolder v1 = (RequestsViewHolder) viewHolder;

            v1.mUserFullName.setText(requestList.get(i).getUser_first_name() + " " + requestList.get(i).getUser_last_name());


            if (!requestList.get(i).isIs_accepted()){
                v1.requestsLayout.setVisibility(View.VISIBLE);
                v1.mFriends.setVisibility(View.GONE);
            }else{
                v1.requestsLayout.setVisibility(View.INVISIBLE);
                v1.mFriends.setVisibility(View.VISIBLE);
            }


            Glide.with(mContext)
                    .load(requestList.get(i).getUser_profile_photo())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(v1.mProfileImage);

            v1.accept();
            v1.deny();
            v1.profile();

        } else if (type == 1) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);

        }


    }


    /**
     * checking if bottom reached
     *
     * @param position
     * @return
     */
    private boolean reachedEndOfList(int position) {
        return position == getItemCount() - 1;
    }


    /**
     * loading more requests
     */

    private void loadMoreData() {
        onLoadMoreItemsListener.onLoadMoreItems();
    }


    /**
     * adding requests
     *
     * @param userAdd
     * @param s
     */
    public void addNewRequestList(List<UserAdd> userAdd, int s) {
        requestList.addAll(userAdd);
        notifyItemRangeInserted(s, userAdd.size());
    }


    /**
     * clearing requests list
     */
    public void clearList() {
        requestList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }


    /**
     * ViewHolders
     */

    public class RequestsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mUserFullName, mFriends;
        // RelativeLayout ,requestsLayout,profile;
        ConstraintLayout requestsLayout;
        Button mAccept;
        ImageButton mDeny;

        RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            mUserFullName = itemView.findViewById(R.id.userName);
            mAccept = itemView.findViewById(R.id.btnAcceptRequest);
            mDeny = itemView.findViewById(R.id.btnDeny);
            mFriends = itemView.findViewById(R.id.mFriends);
            requestsLayout = itemView.findViewById(R.id.requestsLayout);
        }


        //accept friend request
        public void accept() {
            mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectionsHandler.acceptFriendRequest(requestList.get(getAdapterPosition()).getRequest_id(), requestList.get(getAdapterPosition()).getUser_id(), userId, TimeMethods.getUTCdatetimeAsString(), new ConnectionsHandler.OnAcceptingFriendRequestListener() {
                        @Override
                        public void onSuccessListener() {
                            mFriends.setVisibility(View.VISIBLE);
                            requestsLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onServer(String ex) {
                            Log.d(TAG, "onServer: " + ex);
                        }

                        @Override
                        public void onFailure(String ex) {
                            Log.d(TAG, "onFailure: " + ex);
                        }
                    });
                }
            });

        }


        //deny friend request
        public void deny() {
            mDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectionsHandler.RemoveFriendRequest(requestList.get(getAdapterPosition()).getRequest_id(), userId,requestList.get(getAdapterPosition()).getUser_id(), new ConnectionsHandler.OnRemovingFriendRequestListener() {
                        @Override
                        public void onSuccessListener() {
                            requestList.remove(getAdapterPosition());
                            Toast.makeText(mContext, "request removed ", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(getAdapterPosition());
                        }

                        @Override
                        public void onServer(String ex) {
                            Log.d(TAG, "onServer: " + ex);
                        }

                        @Override
                        public void onFailure(String ex) {
                            Log.d(TAG, "onFailure: " + ex);
                        }
                    });
                }
            });
        }

        public void profile() {
            mProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment fragment = new ProfileFragment();
                    fragment.is_current_user = false;
                    fragment.incoming_user_id = requestList.get(getAdapterPosition()).getUser_id();
                    FragmentTransaction tr = fragmentManager.beginTransaction();
                    if (mContext instanceof ConnectionsActivity) {
                        tr.replace(R.id.mainLayoutConnection, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
                    } else {
                        tr.replace(R.id.baseLayout, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
                    }
                }
            });

            mUserFullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment fragment = new ProfileFragment();
                    fragment.is_current_user = false;
                    fragment.incoming_user_id = requestList.get(getAdapterPosition()).getUser_id();
                    FragmentTransaction tr = fragmentManager.beginTransaction();
                    if (mContext instanceof ConnectionsActivity) {
                        tr.replace(R.id.mainLayoutConnection, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
                    } else {
                        tr.replace(R.id.baseLayout, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
                    }
                }
            });
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
