package com.example.testavocado.Home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testavocado.Home.adapters.PostsAdapter;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialog";


    public interface OnPostHideListener {
        void onHide();
    }

    public interface OnPostDeleteListener {
        void onDelete();
    }

    public interface OnPostReportListener {
        void onReport();
    }


    public void setOnPostHideListener(OnPostHideListener onPostHideListener) {
        this.onPostHideListener = onPostHideListener;
    }

    public void setOnPostDeleteListener(OnPostDeleteListener onPostDeleteListener) {
        this.onPostDeleteListener = onPostDeleteListener;
    }

    public void setOnPostReportListener(OnPostReportListener onPostReportListener) {
        this.onPostReportListener = onPostReportListener;
    }


    //interface
    OnPostHideListener onPostHideListener;
    OnPostDeleteListener onPostDeleteListener;
    OnPostReportListener onPostReportListener;


    //widgets
    TextView mDeletePost, mReport, mHidePost;

    //vars
    public int index;
    public PostsAdapter adapter;
    private String datetime;
    private int current_user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_post_option_bottomsheet, container, false);

        current_user_id = HelpMethods.checkSharedPreferencesForUserId(getContext());
        int post_userId = adapter.postsList.get(index).getUser_id();
        initWidgets(view);
        datetime = TimeMethods.getUTCdatetimeAsString();


        if (current_user_id == post_userId) {
            mDeletePost.setVisibility(View.VISIBLE);
            mReport.setVisibility(View.GONE);
            mHidePost.setVisibility(View.GONE);
        } else {
            mDeletePost.setVisibility(View.GONE);
            mReport.setVisibility(View.VISIBLE);
            mHidePost.setVisibility(View.VISIBLE);
        }

        return view;
    }


    private void initWidgets(View view) {
        mDeletePost = view.findViewById(R.id.deletePost);
        mReport = view.findViewById(R.id.reportPost);
        mHidePost = view.findViewById(R.id.hidePost);


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id) {
                    case R.id.deletePost:
                        deletePost();
                        break;


                    case R.id.reportPost:
                        reportPost();
                        break;


                    case R.id.hidePost:
                        hidePost();
                        break;

                }
            }
        };


        mDeletePost.setOnClickListener(clickListener);
        mReport.setOnClickListener(clickListener);
        mHidePost.setOnClickListener(clickListener);

    }


    /**
     * hiding a post from my mainFeed
     */
    private void hidePost() {
        PostMethods.hidePost(adapter.postsList.get(index).getPost_id(), current_user_id, TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnHidingPostListener() {
            @Override
            public void OnPostHide() {
                Log.d(TAG, "OnPostHide: post hidden ");
                adapter.postsList.remove(index);
                adapter.notifyItemRemoved(index);

                if (onPostHideListener != null)
                    onPostHideListener.onHide();
                dismiss();
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);

            }
        });

    }


    /**
     * Reporting a post
     */
    private void reportPost() {
        PostMethods.reportPost(adapter.postsList.get(index).getPost_id(), current_user_id, 0,
                TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnReportingPostListener() {
                    @Override
                    public void OnPostReported() {
                        Log.d(TAG, "OnPostReported: post reported ");

                        if (onPostReportListener != null)
                            onPostReportListener.onReport();
                        dismiss();

                    }

                    @Override
                    public void OnServerException(String ex) {
                        Log.d(TAG, "OnServerException: " + ex);
                    }

                    @Override
                    public void OnFailure(String ex) {
                        Log.d(TAG, "OnFailure: " + ex);
                    }
                });
    }


    /**
     * deleting a post
     */
    private void deletePost() {
        PostMethods.deletePost(adapter.postsList.get(index).getPost_id(), new PostMethods.OnDeletingPostListener() {
            @Override
            public void OnDeleted() {
                adapter.postsList.remove(index);
                adapter.notifyItemRemoved(index);
                Log.d(TAG, "OnDeleted: deleted ");

                if (onPostDeleteListener != null)
                    onPostDeleteListener.onDelete();
                dismiss();
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);

            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);

            }
        });
    }


}
