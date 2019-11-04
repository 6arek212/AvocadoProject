package com.example.testavocado.Connection;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.testavocado.Models.UserAdd;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAddConnectionAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecyclerViewAddConnecti";


    public interface loadMoreListener {
        void onLoadMore();
    }


    public static List<UserAdd> userAddList = new ArrayList<>();
    public boolean is_endOfPosts, searchByLocation;
    private Context mContext;
    private int current_user_id;
    private Activity activity;


    public RecyclerViewAddConnectionAdapter(Context mContext, int current_user_id, Activity activity) {
        this.mContext = mContext;
        this.current_user_id = current_user_id;
        userAddList = new ArrayList<>();
        this.activity = activity;
        searchByLocation = false;
        is_endOfPosts = false;
    }


    /**
     * add users
     *
     * @param useList
     * @param s
     */
    public void addNewSetUserToAdd(List<UserAdd> useList, int s) {
        Log.d(TAG, "addNewSetUserToAdd: ");
        userAddList.addAll(useList);
        notifyItemRangeInserted(s, useList.size());
    }


    /**
     * clear list
     */

    public void clearList() {
        userAddList.clear();
        notifyDataSetChanged();
    }


    /**
     * adding null to the list to show progress bar
     */
    public void addNull() {
        userAddList.add(null);
        notifyItemInserted(userAddList.size() - 1);
    }


    /**
     * removing progressbar
     */
    public void removeProg() {
        try {
            if (userAddList.get(userAddList.size() - 1) == null) {
                userAddList.remove(userAddList.size() - 1);
                notifyItemRemoved(userAddList.size());
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "removeProg: " + e.getMessage());
        }
    }


    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);


        if (userAddList.get(position) != null)
            return 0; //requests
        else if (userAddList.get(position) == null && !is_endOfPosts)
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_add_connections_item, viewGroup, false);
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
            UserViewHolder v1 = (UserViewHolder) viewHolder;

            v1.mUserFullName.setText(userAddList.get(i).getUser_first_name() + " " + userAddList.get(i).getUser_last_name());

            Glide.with(mContext)
                    .load(userAddList.get(i).getUser_profile_photo())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(v1.mProfileImage);


            if (!searchByLocation) {
                v1.mDistance.setVisibility(View.GONE);
            } else {
                v1.mDistance.setVisibility(View.VISIBLE);
                v1.mDistance.setText(userAddList.get(i).getDistance() + " KM away");
            }
            // Log.d(TAG, "onBindViewHolder: getIs_friends "+userAddList.get(i).isIs_accepted());

            Log.d(TAG, "onBindViewHolder: request  " + userAddList.get(i));
            //if there is a friend request the current user sent it
            if (userAddList.get(i).getRequest_id() != -1 && !userAddList.get(i).isIs_accepted() && userAddList.get(i).getSender_id() > 0 &&
                    userAddList.get(i).getSender_id() == current_user_id) {
                Log.d(TAG, "onBindViewHolder: there is a friend request " + userAddList.get(i).getRequest_id());
                v1.addingRemovingFriendsLayout.setVisibility(View.VISIBLE);
                v1.requestsLayout.setVisibility(View.GONE);
                v1.mAlredyFriendsLayout.setVisibility(View.GONE);

                v1.mAdd.setVisibility(View.GONE);
                v1.mDelete.setVisibility(View.VISIBLE);
                deleteOnClick(v1.mDelete, v1.mAdd, i);

            }

            //if there is a friend request sent to this current user
            else if (userAddList.get(i).getRequest_id() != -1 && !userAddList.get(i).isIs_accepted() && userAddList.get(i).getSender_id() > 0 &&
                    userAddList.get(i).getSender_id() != current_user_id) {

                v1.requestsLayout.setVisibility(View.VISIBLE);
                v1.addingRemovingFriendsLayout.setVisibility(View.GONE);
                v1.mAlredyFriendsLayout.setVisibility(View.GONE);

                acceptingFriendRequestClick(v1.mAccept,
                        v1.mDelete,
                        v1.mAdd, v1.requestsLayout,
                        v1.addingRemovingFriendsLayout, i);


                denyingFriendRequestClick(v1.mDeny,
                        v1.mDelete, v1.mAdd,
                        v1.requestsLayout,
                        v1.addingRemovingFriendsLayout, i);

            }

            // if they are not friends
            else if (userAddList.get(i).getRequest_id() == -1 && userAddList.get(i).getSender_id() == -1) {
                Log.d(TAG, "onBindViewHolder: not friends " + userAddList.get(i).getRequest_id());
                v1.addingRemovingFriendsLayout.setVisibility(View.VISIBLE);
                v1.requestsLayout.setVisibility(View.GONE);

                v1.mAlredyFriendsLayout.setVisibility(View.GONE);

                v1.mAdd.setVisibility(View.VISIBLE);
                v1.mDelete.setVisibility(View.GONE);
                addOnClick(v1.mAdd, v1.mDelete, i);
            }
            //if they are friends
            else if (userAddList.get(i).getRequest_id() > 0 && userAddList.get(i).isIs_accepted() && userAddList.get(i).getSender_id() > 0) {
                Log.d(TAG, "onBindViewHolder: they are friends " + userAddList.get(i).getRequest_id());
                v1.addingRemovingFriendsLayout.setVisibility(View.VISIBLE);
                v1.requestsLayout.setVisibility(View.GONE);

                v1.mAdd.setVisibility(View.GONE);
                v1.mDelete.setVisibility(View.GONE);
                deleteOnClick(v1.mDelete, v1.mAdd, i);

                v1.mAlredyFriendsLayout.setVisibility(View.VISIBLE);
            }


            attachOnClickProfile(i, v1.profileConnection);

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


    public void attachOnClickProfile(final int i, RelativeLayout relativeLayout) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                fragment.is_current_user = false;
                fragment.incoming_user_id = userAddList.get(i).getUser_id();
                FragmentManager fragmentManager = ((ConnectionsActivity) mContext).getSupportFragmentManager();
                FragmentTransaction tr = fragmentManager.beginTransaction();
                HelpMethods.closeKeyboard(activity);
                tr.replace(R.id.mainLayoutConnection, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
            }
        });

    }


    private void addOnClick(final Button add, final Button delete, final int index) {
        Log.d(TAG, "addOnClick: index " + index);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.onSendingNewFriendRequest(current_user_id, userAddList.get(index).getUser_id(),
                        TimeMethods.getUTCdatetimeAsString(), new ConnectionsHandler.OnStatusRegisterListener() {
                            @Override
                            public void onSuccessListener(int request_id) {
                                Log.d(TAG, "onSuccessListener: " + request_id + "   index " + index);
                                userAddList.get(index).setRequest_id(request_id);
                                add.setVisibility(View.GONE);
                                delete.setVisibility(View.VISIBLE);
                                deleteOnClick(delete, add, index);
                                userAddList.get(index).setRequest_id(request_id);
                            }

                            @Override
                            public void onServerException(String ex) {
                                Log.d(TAG, "onServerException: " + ex);

                            }

                            @Override
                            public void onFailureListener(String ex) {
                                Log.d(TAG, "onFailureListener: " + ex);

                            }
                        });
            }
        });


    }


    public void deleteOnClick(final Button delete, final Button add, final int index) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: request_id  " + userAddList.get(index).getRequest_id() + " index + " + index);

                ConnectionsHandler.RemoveFriendRequest(userAddList.get(index).getRequest_id(), new ConnectionsHandler.OnRemovingFriendRequestListener() {
                    @Override
                    public void onSuccessListener() {
                        Log.d(TAG, "onSuccessListener: ");
                        add.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.GONE);
                        userAddList.get(index).setRequest_id(-1);
                        addOnClick(add, delete, index);
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


    public void acceptingFriendRequestClick(final Button accept, final Button delete, final Button add, final RelativeLayout requestsLayout, final RelativeLayout addingRemovingFriendsLayout, final int index) {

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.acceptFriendRequest(userAddList.get(index).getRequest_id(),userAddList.get(index).getUser_id(),current_user_id, TimeMethods.getUTCdatetimeAsString(), new ConnectionsHandler.OnAcceptingFriendRequestListener() {
                    @Override
                    public void onSuccessListener() {
                        Log.d(TAG, "onSuccessListener: friend request accepted ");
                        requestsLayout.setVisibility(View.GONE);
                        addingRemovingFriendsLayout.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                        add.setVisibility(View.GONE);
                        deleteOnClick(delete, add, index);
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


    public void denyingFriendRequestClick(final ImageButton deny, final Button delete, final Button add, final RelativeLayout requestsLayout,
                                          final RelativeLayout addingRemovingFriendsLayout, final int index) {

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.RemoveFriendRequest(userAddList.get(index).getRequest_id(), new ConnectionsHandler.OnRemovingFriendRequestListener() {
                    @Override
                    public void onSuccessListener() {
                        Log.d(TAG, "onSuccessListener: friend request denied ");
                        requestsLayout.setVisibility(View.GONE);
                        addingRemovingFriendsLayout.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.GONE);
                        add.setVisibility(View.VISIBLE);
                        addOnClick(add, delete, index);
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


    @Override
    public int getItemCount() {
        return userAddList.size();
    }


    /**
     * ViewHolders
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mUserFullName, mDistance;
        Button mAdd, mDelete, mAccept;
        ImageButton mDeny;
        RelativeLayout mainRowLayout, requestsLayout, addingRemovingFriendsLayout, profileConnection, mAlredyFriendsLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            mUserFullName = itemView.findViewById(R.id.userName);
            mAdd = itemView.findViewById(R.id.btnAddConnection);
            mDelete = itemView.findViewById(R.id.btnDeleteConnection);
            mainRowLayout = itemView.findViewById(R.id.mainLayout);
            mAccept = itemView.findViewById(R.id.btnAcceptRequest);
            mDeny = itemView.findViewById(R.id.btnDeny);
            requestsLayout = itemView.findViewById(R.id.requestsLayout);
            addingRemovingFriendsLayout = itemView.findViewById(R.id.addingRemovingFriendsLayout);
            profileConnection = itemView.findViewById(R.id.profileConnection);
            mDistance = itemView.findViewById(R.id.distance);
            mAlredyFriendsLayout = itemView.findViewById(R.id.alredyFriendsLayout);
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
