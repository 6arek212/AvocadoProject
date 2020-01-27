package com.example.testavocado.Home.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Home.adapters.LikesAdapter;
import com.example.testavocado.Models.Like;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DisLikesFragment extends Fragment {
    private static final String TAG = "DisLikesFragment";



    //widgets
    private RelativeLayout mLayout;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;


    //vars
    private  int user_id;
    private LikesAdapter adapter;
    private String datetime;
    private Context mContext;
    private  boolean loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_likes, container, false);

        initWidgets(view);

        return view;
    }


    private void initWidgets(View view) {
        mContext = getContext();
        user_id=HelpMethods.checkSharedPreferencesForUserId(mContext);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSwipe = view.findViewById(R.id.swipe);
        mLayout=view.findViewById(R.id.likeLayout);
        adapter = new LikesAdapter(getContext(),user_id,getChildFragmentManager());
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));

        mRecyclerView.setAdapter(adapter);
        getdisPostLikes(0);

        recyclerViewBottomDetectionListener();
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdisPostLikes(0);
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
                        HelpMethods.closeKeyboard(getActivity());
                        if (isRecyclerScrollable())
                            if (!mRecyclerView.canScrollVertically(1) && !loading) {
                                Log.d(TAG, "recyclerViewBottomDetectionListener: bottom");

                                loading = true;
                                getdisPostLikes(adapter.getItemCount() - 1);
                            }
                    }
                });
    }


    private void getdisPostLikes(int offset) {
        Log.d(TAG, "getdisPostLikes: postId " + LikesDislikesFragment.post_id);
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_end = false;
            adapter.clear();
            loading = true;
        }

        adapter.addNull();

        PostMethods.getPostDisLikes(LikesDislikesFragment.post_id, datetime, offset, new PostMethods.OnGettingPostsLikesListener() {
            @Override
            public void OnGettingLikes(List<Like> likeList) {
                adapter.removeProg();
                adapter.addLikesSet(likeList, adapter.getItemCount());
                mSwipe.setRefreshing(false);
                loading = false;
            }


            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);
                adapter.removeProg();
                adapter.is_end = true;
                adapter.addNull();
                mSwipe.setRefreshing(false);
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);
                loading = false;
                adapter.removeProg();
                mSwipe.setRefreshing(false);

                if (getContext() != null)
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();

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
