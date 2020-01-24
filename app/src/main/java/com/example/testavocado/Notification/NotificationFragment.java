package com.example.testavocado.Notification;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import com.example.testavocado.Models.Notification;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.example.testavocado.Utils.WrapContentLinearLayoutManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private static final String TAG = "NotificationFragment";


    //widgets
    public RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private RelativeLayout mainLayoutNotification;


    //vars
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private static boolean loading;

    private String datetime;
    private int current_user_id;
    private Handler handler;
    public NotificationAdapter adapter;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mContext = getContext();
        datetime = TimeMethods.getUTCdatetimeAsString();
        current_user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);

        initWidgets(view);
        return view;
    }


    /**
     * initializing all the widgets and getting the first 20 notification from the server
     *
     * @param view Layout XML
     */
    private void initWidgets(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSwipe = view.findViewById(R.id.swipe);
        adapter = new NotificationAdapter(getFragmentManager(), mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));
        mRecyclerView.setAdapter(adapter);
        mainLayoutNotification = view.findViewById(R.id.mainLayoutNotification);


        recyclerViewBottomDetectionListener();
        getNotification(0);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotification(0);
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
                                getNotification(adapter.getItemCount());
                            }
                    }
                });
    }


    /**
     * getting notification from the server
     *
     * @param offset num
     */
    private void getNotification(final int offset) {
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_endOfPosts = false;
            adapter.clear();
            loading = true;
        }

        adapter.addNull();

        NotificationMethods.getNotification(current_user_id, datetime, offset, new NotificationMethods.OnGettingNotification() {
            @Override
            public void successfullyGettingNotification(String json) {
                Log.d(TAG, "successfullyGettingNotification: ");

                new NotificationsAsyncTask(NotificationFragment.this, offset)
                        .execute(json);
            }

            @Override
            public void serverException(String exception) {
                Log.d(TAG, "serverException: " + exception);
                adapter.removeProg();
                mSwipe.setRefreshing(false);

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }
            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: " + exception);
                if (getContext() != null)
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
     * asynctask for handling the heavy load in the background
     * <p>
     * of the data that came from the server
     */
    private static class NotificationsAsyncTask extends AsyncTask<String, Integer, List<Notification>> {
        private WeakReference<NotificationFragment> notificationFragment;
        private int offset;


        NotificationsAsyncTask(NotificationFragment feedFragment, int offset) {
            notificationFragment = new WeakReference<NotificationFragment>(feedFragment);
            this.offset = offset;
        }


        @Override
        protected List<Notification> doInBackground(String... strings) {
            List<Notification> notifications = null;

            try {
                JSONArray jsonArray = new JSONArray(strings[0]);
                notifications = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    notifications.add(new Gson().fromJson(jsonArray.get(i).toString(), Notification.class));
                    notifications.get(i).setNotification_datetime(TimeMethods.convertDateTimeFormat2(notifications.get(i).getNotification_datetime()));
                    Log.d(TAG, "doInBackground: notifications " + notifications.get(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return notifications;
        }

        @Override
        protected void onPostExecute(List<Notification> notificationList) {
            super.onPostExecute(notificationList);

            final NotificationFragment fragment = notificationFragment.get();
            if (fragment == null || fragment.isRemoving())
                return;

            fragment.adapter.removeProg();
            fragment.adapter.addSetOfNotifications(notificationList, fragment.adapter.getItemCount());
            fragment.mSwipe.setRefreshing(false);
            loading = false;
        }
    }


}
