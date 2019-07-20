package com.example.testavocado.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.User;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.testavocado.Profile.RecyclerViewProfileAdapter.FRIENDS_ADDING;
import static com.example.testavocado.Profile.RecyclerViewProfileAdapter.FRIENDS_LAYOUT;
import static com.example.testavocado.Profile.RecyclerViewProfileAdapter.FRIENDS_REQUEST_RECEIVED;
import static com.example.testavocado.Profile.RecyclerViewProfileAdapter.FRIENDS_REQUEST_SENT;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //Widgets
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;


    //vars
    private Context mContext;
    public int incoming_user_id;
    private int current_user_id;
    public boolean is_current_user;
    private RecyclerViewProfileAdapter adapter;
    private String datetime;
    private boolean loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: is current user "+is_current_user+"  incoming_user_id "+incoming_user_id);
        initWidgets(view);
        getProfileInfo();

        return view;
    }


    private void initWidgets(View view) {
        mContext = getContext();
        mSwipe = view.findViewById(R.id.swipe);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        current_user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProfileInfo();
            }
        });
    }


    private void initRecyclerView(User user) {
        adapter = new RecyclerViewProfileAdapter(mContext, current_user_id, incoming_user_id, user, getFragmentManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


            if (!user.isUser_is_private()) {

                if (user.getFriend_request_id() != -1) {
                    //there is a friendship request

                    if (!user.isIs_accepted() && user.getSender_id() == current_user_id) {
                        // 1-if the current user sent a friend request he can remove it
                        adapter.addingLayoutType = FRIENDS_REQUEST_SENT;

                    } else if (!user.isIs_accepted() && user.getSender_id() != current_user_id) {
                        //2- if the current user has a friend request he can accept it
                        adapter.addingLayoutType = FRIENDS_REQUEST_RECEIVED;
                    } else if (user.isIs_accepted()) {
                        //3- they are friends he can send him message or remove friendship
                        adapter.addingLayoutType = FRIENDS_LAYOUT;
                    }
                } else {
                    adapter.addingLayoutType = FRIENDS_ADDING;
                }

                adapter.is_current_user = is_current_user;
                getPosts(0);
            } else {
                //TODO private account adding layout
                Toast.makeText(mContext, "account is private !", Toast.LENGTH_SHORT).show();
            }

        mRecyclerView.setAdapter(adapter);
        recyclerViewBottomDetectionListener();
    }


    private void getProfileInfo() {
        ProfileHandler.getProfileInfo(incoming_user_id, current_user_id, new ProfileHandler.OnGettingProfileInfo() {
            @Override
            public void successfullyGettingInfo(User user) {
                Log.d(TAG, "successfullyGettingInfo: " + user);
                initRecyclerView(user);
            }

            @Override
            public void serverException(String exception) {
                Log.d(TAG, "serverException: " + exception);

            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: " + exception);

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
                                getPosts(adapter.getItemCount() - 1);
                            }
                    }
                });
    }


    Handler handler = new Handler();
    List<Post> posts;

    public void getPosts(int offset) {
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_endOfPosts = false;
            adapter.clear();
            loading = true;
        }

        adapter.addNull();

        ProfileHandler.getProfilePosts(incoming_user_id, datetime, offset, new ProfileHandler.OnGettingProfilePostsListener() {
            @Override
            public void successfullyGettingPosts(final String json) {
                Log.d(TAG, "successfullyGettingPosts: incoming_user_id= " + incoming_user_id + "   " + datetime + "  " + json);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            Log.d(TAG, "run: json array posts" + jsonArray.length());
                            posts = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                posts.add(new Gson().fromJson(jsonArray.get(i).toString(), Post.class));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.removeProg();
                                adapter.addSetOfPosts(posts, adapter.getItemCount());
                                mSwipe.setRefreshing(false);
                                loading = false;
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void serverException(String exception) {
                Log.d(TAG, "serverException: " + exception);
                mSwipe.setRefreshing(false);
                adapter.removeProg();
                adapter.is_endOfPosts = true;
                adapter.addNull();
            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: " + exception);
                mSwipe.setRefreshing(false);
                adapter.removeProg();
                loading = false;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        Toast.makeText(mContext, "nothing", Toast.LENGTH_SHORT).show();

        if (requestCode == 2) {
            if (resultCode == getActivity().RESULT_OK) {

            } else {
                Toast.makeText(mContext, "nothing", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
