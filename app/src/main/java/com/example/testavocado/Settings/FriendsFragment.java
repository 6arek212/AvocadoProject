package com.example.testavocado.Settings;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testavocado.Connection.ConnectionsHandler;
import com.example.testavocado.Models.Friend;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

import java.util.List;

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    //widgets
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private CoordinatorLayout friendLayout;

    //vars
    private Context mContext;
    private FriendsAdapter adapter;
    private int user_id;
    private String datetime;


    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private static boolean loading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        mContext = getContext();
        initWidgets(view);

        return view;
    }

    private void initWidgets(View view) {
        user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        friendLayout = view.findViewById(R.id.friendLayout);
        mSwipe = view.findViewById(R.id.swipe);
        adapter = new FriendsAdapter(mContext,getFragmentManager(),user_id);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));
        mRecyclerView.setAdapter(adapter);
        recyclerViewBottomDetectionListener();

        getFriends(0);


        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriends(0);
            }
        });
    }


    private void getFriends(int offset) {
        Log.d(TAG, "getFriends: getting user friends ");
        if (offset == 0) {
            adapter.is_endOfPosts = false;
            adapter.clearList();
            datetime = TimeMethods.getUTCdatetimeAsString();
            loading = true;
        }

        adapter.addNull();

        ConnectionsHandler.getUserFriends(user_id, offset, datetime, new ConnectionsHandler.OnUserFriendsListener() {
            @Override
            public void OnSuccessfullyGettingFriends(List<Friend> friends) {
                adapter.removeProg();
                adapter.clearList();
                mSwipe.setRefreshing(false);
                adapter.addSetOfFriends(friends, adapter.getItemCount());
                loading = false;
            }

            @Override
            public void OnServerException(String e) {
                Log.d(TAG, "OnServerException: " + e);

                adapter.removeProg();
                mSwipe.setRefreshing(false);

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }
            }

            @Override
            public void OnFailure(String e) {
                Log.d(TAG, "OnFailure: " + e);
                Snackbar.make(friendLayout, getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();

                adapter.removeProg();
                mSwipe.setRefreshing(false);

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }
            }
        });
    }







    public boolean isRecyclerScrollable() {
        return mRecyclerView.computeVerticalScrollRange() > mRecyclerView.getHeight();
    }


    /**
     * Attaching a listener to detect when the list reached the bottom
     */
    private void recyclerViewBottomDetectionListener() {
        mRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (isRecyclerScrollable())
                            if (!mRecyclerView.canScrollVertically(1) && !loading) {
                                loading = true;
                                getFriends(adapter.getItemCount());
                            }
                    }
                });
    }







    /**
     * RecyclerView LinearLayout Manager
     */
    public class WrapContentLinearLayoutManager extends LinearLayoutManager {


        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

}
