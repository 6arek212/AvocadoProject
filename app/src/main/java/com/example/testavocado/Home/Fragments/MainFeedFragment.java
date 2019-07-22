package com.example.testavocado.Home.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Home.adapters.PostsAdapter;
import com.example.testavocado.Models.Post;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainFeedFragment extends Fragment {
    private static final String TAG = "MainFeedFragment";


    //widgets
    public RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout mainLayoutPosts;


    //var
    private Context mContext;
    private int user_id;
    private PostsAdapter adapter;
    private String datetime;
    protected Handler handler;
    private static boolean loading;
    private postsAsyncTask asyncTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_feed2, container, false);

        if (savedInstanceState==null){
            initWidgets(view);
        }



        return view;
    }


    /**
     * init all the widgets
     */

    private void initWidgets(View view) {
        mContext = getContext();

        user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        mainLayoutPosts = view.findViewById(R.id.mainLayoutPosts);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));

        adapter = new PostsAdapter(getFragmentManager(), mContext, user_id, MainFeedFragment.this);
        mRecyclerView.setAdapter(adapter);
        handler = new Handler();

        widgetsEvent();
        recyclerViewBottomDetectionListener();
        getPosts(0, 0);
    }


    /**
     * attaching the widgets with events
     */
    private void widgetsEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (adapter.dontShow) {
                    getPosts(0, adapter.post_type);
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
                        HelpMethods.closeKeyboard(getActivity());
                        if (isRecyclerScrollable())
                            if (!mRecyclerView.canScrollVertically(1) && !loading) {
                                Log.d(TAG, "recyclerViewBottomDetectionListener: bottom");

                                loading = true;

                                getPosts(adapter.getItemCount() - 1, adapter.post_type);
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


    /**
     * getting 20 post from the server
     *
     * @param offset
     */

    Handler handle12r = new Handler();
    List<Post> posts;

    public void getPosts(final int offset, int type) {
        Log.d(TAG, "getPosts: type " + type);
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_endOfPosts = false;
            adapter.clear();
            loading = true;
        }

        adapter.addNull();
        PostMethods.getFriendsPosts(user_id, datetime, offset, type, new PostMethods.OnGettingFriendsPostListener() {
            @Override
            public void OnSuccess(final String json) {

            /*    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(json);
                            posts = new ArrayList<>();


                            for (int k = 0; k < jsonArray.length(); k++) {
                                posts.add(new Gson().fromJson(jsonArray.get(k).toString(), Post.class));
                                posts.get(k).setPost_date_time(TimeMethods.getTimestampDifference(posts.get(k).getPost_date_time()));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        handle12r.post(new Runnable() {
                            @Override
                            public void run() {
                                if (offset != 0) {
                                    adapter.removeProg();
                                    adapter.addSetOfPosts(posts, adapter.getItemCount());

                                } else {
                                    adapter.addSetOfPost2s(posts);
                                }
                                setLoaded();
                            }
                        });
                    }
                }).start();*/


                asyncTask = new postsAsyncTask(MainFeedFragment.this, offset);
                asyncTask.execute(json);
                Log.d(TAG, "OnSuccess: added posts");
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);
                //setLoaded();
                adapter.removeProg();
                adapter.is_endOfPosts = true;
                adapter.addNull();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);
                adapter.removeProg();
                setLoaded();
                swipeRefreshLayout.setRefreshing(false);

                if (getContext() != null)
                    Snackbar.make(mainLayoutPosts, getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();

            }
        });

    }


    /**
     * asynctask for handling the heavy load in the background
     * <p>
     * of the data that came from the server
     */
    private static class postsAsyncTask extends AsyncTask<String, Integer, List<Post>> {
        private WeakReference<MainFeedFragment> mainFeedFragment;
        private int offset;


        postsAsyncTask(MainFeedFragment feedFragment, int offset) {
            mainFeedFragment = new WeakReference<MainFeedFragment>(feedFragment);
            this.offset = offset;
        }


        //background work
        @Override
        protected List<Post> doInBackground(String... strings) {
            JSONArray jsonArray = null;
            List<Post> posts = new ArrayList<>();
            try {
                jsonArray = new JSONArray(strings[0]);


                for (int i = 0; i < jsonArray.length(); i++) {
                    posts.add(new Gson().fromJson(jsonArray.get(i).toString(), Post.class));
                    posts.get(i).setPost_date_time(TimeMethods.getTimestampDifference(posts.get(i).getPost_date_time()));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return posts;
        }


        //result of the background work
        @Override
        protected void onPostExecute(final List<Post> posts) {
            super.onPostExecute(posts);
            final MainFeedFragment fragment = mainFeedFragment.get();
            if (fragment == null || fragment.isRemoving())
                return;


            Log.d(TAG, "onPostExecute: " + offset);
            for (int i = 0; i < posts.size(); i++) {
                Log.d(TAG, "onPostExecute: posts " + posts.get(i));
            }


            fragment.adapter.removeProg();
            fragment.adapter.addSetOfPosts(posts, fragment.adapter.getItemCount());


            fragment.swipeRefreshLayout.setRefreshing(false);
            setLoaded();
        }
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
