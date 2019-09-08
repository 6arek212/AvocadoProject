package com.example.testavocado.Settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testavocado.Home.Fragments.MainFeedFragment;
import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.SavedPost;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

import java.util.List;

public class SavedPostsFragment extends Fragment {
    private static final String TAG = "SavedPostsFragment";


    //widgets
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;



    //vars
    private int current_user_id;
    private Context mContext;
    private String datetime;
    private static boolean loading;
    private SavedPostsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_saved_posts,container,false);

        initWidgets(view);

        return view;
    }



    private void initWidgets(View view) {
        mContext=getContext();
        mRecyclerView=view.findViewById(R.id.recyclerView);
        mSwipe=view.findViewById(R.id.swipe);
        current_user_id= HelpMethods.checkSharedPreferencesForUserId(mContext);
        adapter=new SavedPostsAdapter(getFragmentManager(),mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));
        mRecyclerView.setAdapter(adapter);

        getSavedPosts(0);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSavedPosts(0);
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

                                getSavedPosts(adapter.getItemCount() - 1);
                            }
                    }
                });
    }


    /**
     * using this when detecting if the bottom reached
     */

    public static void setLoaded() {
        loading = false;
    }








    private void getSavedPosts(int offset){
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_endOfPosts = false;
            adapter.clear();
            loading = true;
        }
        adapter.addNull();


        PostMethods.getSavedPosts(current_user_id, datetime, offset, new PostMethods.OnGettingSavedPostListener() {
            @Override
            public void onSuccess(List<SavedPost> savedPosts) {
                adapter.removeProg();
                adapter.addSetOfSavedPosts(savedPosts,adapter.getItemCount());
                mSwipe.setRefreshing(false);
                setLoaded();
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: "+ex);
                adapter.removeProg();
                adapter.is_endOfPosts = true;
                adapter.addNull();
                mSwipe.setRefreshing(false);
            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: "+ex);
                adapter.removeProg();
                setLoaded();
                mSwipe.setRefreshing(false);
                Toast.makeText(mContext, mContext.getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();

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
