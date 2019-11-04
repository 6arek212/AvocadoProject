package com.example.testavocado.Connection;

import android.content.Context;
import androidx.annotation.NonNull;
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

    /**
     *      adding null to the list to show progress bar
     *
     */
    public void addNull() {
        Log.d(TAG, "addNull: adding null ");
        requestList.add(null);
        notifyItemInserted(requestList.size() - 1);
    }


    /**
     *      removing progressbar
     *
     */
    public void removeProg() {
        Log.d(TAG, "removeProg: list size "+requestList.size());
        if (requestList.get(requestList.size() - 1) == null) {
            requestList.remove(requestList.size() - 1);
            notifyItemRemoved(requestList.size());
        }
    }





    public RecyclerViewRequestsAdapter(Context context) {
        mContext = context;
        userId=HelpMethods.checkSharedPreferencesForUserId(mContext);
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_connection_request_item, viewGroup, false);
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
            v1.mFriends.setVisibility(View.GONE);
            acceptOnClick(v1.mAccept, v1, i);
            denyClick(v1.mDeny, i);
            attachOnClickProfile(i,v1.profile);


        } else if (type == 1) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);

        }


    }





    /**
     *              accepting request
     *
     * @param button
     * @param viewHolder
     * @param index
     */
    private void acceptOnClick(Button button, final RequestsViewHolder viewHolder, final int index) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.acceptFriendRequest(requestList.get(index).getRequest_id(),requestList.get(index).getUser_id() ,userId,TimeMethods.getUTCdatetimeAsString(), new ConnectionsHandler.OnAcceptingFriendRequestListener() {
                    @Override
                    public void onSuccessListener() {
                        viewHolder.mFriends.setVisibility(View.VISIBLE);
                        viewHolder.requestsLayout.setVisibility(View.GONE);
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






    /**
     *
     *      denying Request
     *
     * @param deny
     * @param index
     */

    private void denyClick(ImageButton deny, final int index) {
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.RemoveFriendRequest(requestList.get(index).getRequest_id(), new ConnectionsHandler.OnRemovingFriendRequestListener() {
                    @Override
                    public void onSuccessListener() {
                        requestList.remove(index);
                        Toast.makeText(mContext, "request removed ", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(index);
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





    /**
     *      checking if bottom reached
     *
     * @param position
     * @return
     */
    private boolean reachedEndOfList(int position) {
        return position == getItemCount() - 1;
    }





    /**
     *
     *      loading more requests
     */

    private void loadMoreData() {
        onLoadMoreItemsListener.onLoadMoreItems();
    }


    /**
     *      adding requests
     *
     * @param userAdd
     * @param s
     */
    public void addNewRequestList(List<UserAdd> userAdd, int s) {
        requestList .addAll(userAdd);
        notifyItemRangeInserted(s, userAdd.size());
    }




    /**
     *
     *  clearing requests list
     *
     */
    public void clearList() {
        requestList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }




    public void attachOnClickProfile(final int i, RelativeLayout relativeLayout) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                fragment.is_current_user = false;
                fragment.incoming_user_id = requestList.get(i).getUser_id();
                FragmentManager fragmentManager = ((ConnectionsActivity) mContext).getSupportFragmentManager();
                FragmentTransaction tr = fragmentManager.beginTransaction();
                tr.replace(R.id.mainLayoutConnection, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
            }
        });

    }



    /**
     *      ViewHolders
     *
     */

    public class RequestsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mUserFullName;
        RelativeLayout mFriends,requestsLayout,profile;
        Button mAccept;
        ImageButton mDeny;

         RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            mUserFullName = itemView.findViewById(R.id.userName);
            mAccept = itemView.findViewById(R.id.btnAcceptRequest);
            mDeny = itemView.findViewById(R.id.btnDeny);
            mFriends = itemView.findViewById(R.id.alredyFriendsLayout);
            requestsLayout=itemView.findViewById(R.id.requestsLayout);
            profile=itemView.findViewById(R.id.profile);
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
