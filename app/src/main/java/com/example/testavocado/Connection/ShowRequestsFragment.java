package com.example.testavocado.Connection;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testavocado.Utils.HelpMethods;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.testavocado.Models.UserAdd;
import com.example.testavocado.R;
import com.example.testavocado.Utils.TimeMethods;

import java.util.List;

public class ShowRequestsFragment extends Fragment {
    private static final String TAG = "ShowRequestsFragment";

    //widgets
    private RecyclerView mRecyclerView;
    private RelativeLayout mRequestLayout;
    private SwipeRefreshLayout mSwipe;

    //vars
    private Context mContext;
    private RecyclerViewRequestsAdapter adapter;
    private String datetime;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private static boolean loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_connection_requests, container, false);

        initWidgets(view);

        return view;
    }


    /**
     * init all the widgets
     *
     * @param view
     */


    private void initWidgets(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRequestLayout = view.findViewById(R.id.requestLayout);
        mSwipe = view.findViewById(R.id.swipe);
        mContext = getContext();

        adapter = new RecyclerViewRequestsAdapter(mContext);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));

        mRecyclerView.setAdapter(adapter);
        recyclerViewBottomDetectionListener();

        getRequests(0);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequests(0);
            }
        });
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
                        HelpMethods.closeKeyboard(getActivity());
                        if (isRecyclerScrollable())
                            if (!mRecyclerView.canScrollVertically(1) && !loading) {
                                Log.d(TAG, "recyclerViewBottomDetectionListener: bottom");

                                loading = true;

                                getRequests(adapter.getItemCount() - 1);
                            }
                    }
                });
    }


    /**
     * using this when detecting if the bottom reached
     */



    public boolean isRecyclerScrollable() {
        return mRecyclerView.computeVerticalScrollRange() > mRecyclerView.getHeight();
    }



    /**
     * getting requests from the server
     *
     * @param offset
     */
    private void getRequests(int offset) {
        Log.d(TAG, "getRequests: getting request ");
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.clearList();
            adapter.is_endOfPosts = false;
            loading = true;
        }
        adapter.addNull();


        ConnectionsHandler.getConnectionsRequest(ConnectionsActivity.user_current_id, offset, datetime,
                mContext, new ConnectionsHandler.OnGettingConnectionsRequestsListener() {
                    @Override
                    public void OnSuccessfullyGettingRequests(List<UserAdd> userAddList) {
                        adapter.removeProg();

                        adapter.addNewRequestList(userAddList, adapter.getItemCount());
                        Log.d(TAG, "OnSuccessfullyGettingRequests: added new requests ");
                        mSwipe.setRefreshing(false);
                        loading = false;
                    }

                    @Override
                    public void OnServerException(String exception) {
                        Log.d(TAG, "OnServerException: error in server exception " + exception);
                        adapter.removeProg();
                        mSwipe.setRefreshing(false);

                        if (!adapter.is_endOfPosts) {
                            adapter.is_endOfPosts = true;
                            adapter.addNull();
                        }
                    }

                    @Override
                    public void OnFailure(String exception) {
                        Log.d(TAG, "OnFailure: failure exception " + exception);
                        adapter.clearList();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();
                        adapter.removeProg();
                        mSwipe.setRefreshing(false);

                        if (!adapter.is_endOfPosts) {
                            adapter.is_endOfPosts = true;
                            adapter.addNull();
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
