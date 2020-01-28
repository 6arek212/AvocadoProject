package com.example.testavocado.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.testavocado.Dialogs.ConfirmDialogEditeText;
import com.example.testavocado.ccc.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialog";


    public interface OnActionListener {
        void onHide();

        void onDelete();

        void onReport();

        void onSave(int saved_id);

        void onDeleteSavedPost();
    }


    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    //interface
    OnActionListener onActionListener;

    //widgets
    ConstraintLayout mDeleteLayout, mHideLayout, mSaveLayout;
    TextView mDeletePost, mReport, mHidePost, mSavedPost;
    //vars
    private Context mContext;
    public int post_userId, post_id, post_saved;
    private int current_user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_post_option_bottomsheet, container, false);

        current_user_id = HelpMethods.checkSharedPreferencesForUserId(getContext());
        initWidgets(view);


        if (current_user_id == post_userId) {
            mDeleteLayout.setVisibility(View.VISIBLE);
            mReport.setVisibility(View.GONE);
            mHideLayout.setVisibility(View.GONE);
            mSaveLayout.setVisibility(View.GONE);
        } else {
            mDeleteLayout.setVisibility(View.GONE);
            mReport.setVisibility(View.VISIBLE);
            mHideLayout.setVisibility(View.VISIBLE);
            mSaveLayout.setVisibility(View.VISIBLE);
            if (post_saved != 0) {
                mSaveLayout.setVisibility(View.VISIBLE);
                mSavedPost.setText(mContext.getString(R.string.delete_from_savedposts));
            }
        }

        return view;
    }


    private void initWidgets(View view) {
        mDeletePost = view.findViewById(R.id.deletePost);
        mReport = view.findViewById(R.id.reportPost);
        mHidePost = view.findViewById(R.id.hidePost);
        mSavedPost = view.findViewById(R.id.savePost);
        mDeleteLayout = view.findViewById(R.id.deletePostLayout);
        mSaveLayout = view.findViewById(R.id.savePostLayout);
        mHideLayout = view.findViewById(R.id.hidePostLayout);
        mContext = getContext();


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id) {
                    case R.id.deletePostLayout:
                        showAlert("Are you sure for deleting this post ?", new OnClickDialog() {
                            @Override
                            public void onClick() {
                                deletePost();
                            }
                        });
                        break;


                    case R.id.reportPost:
                        reportPost();
                        break;


                    case R.id.hidePostLayout:
                        showAlert("Are you sure for hiding this post ?", new OnClickDialog() {
                            @Override
                            public void onClick() {
                                hidePost();
                            }
                        });
                        break;

                    case R.id.savePostLayout:

                        if (post_saved != 0) {
                            deleteSavedPost();
                        } else {
                            final ConfirmDialogEditeText confirmDialogEditeText = new ConfirmDialogEditeText();
                            confirmDialogEditeText.setType(false);
                            confirmDialogEditeText.setTitle("enter description : ");
                            confirmDialogEditeText.setOnConfirm(new ConfirmDialogEditeText.OnConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    savePost(text);
                                    confirmDialogEditeText.dismiss();

                                }
                            });
                            if (getFragmentManager() != null)
                                confirmDialogEditeText.show(getFragmentManager(), "cc");
                        }
                        break;
                }
            }
        };


        mDeleteLayout.setOnClickListener(clickListener);
        mReport.setOnClickListener(clickListener);
        mHideLayout.setOnClickListener(clickListener);
        mSaveLayout.setOnClickListener(clickListener);
    }

    interface OnClickDialog {
        void onClick();
    }

    private void showAlert(String title, final OnClickDialog click) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle2);
        builder.setTitle(title);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                click.onClick();
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteSavedPost() {
        PostMethods.deleteSavePost(post_saved, new PostMethods.OnDeleteingSavinedPostListener() {
            @Override
            public void onDeleted() {
                Log.d(TAG, "onDeleted: post deleted from saved posts ");
                if (onActionListener != null)
                    onActionListener.onDeleteSavedPost();
                dismiss();
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);

            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: " + ex);
                Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void savePost(String description) {
        PostMethods.savePost(current_user_id, post_id, TimeMethods.getUTCdatetimeAsString(), description, new PostMethods.OnSavingPostListener() {
            @Override
            public void onSaved(int saved_id) {
                Log.d(TAG, "onSaved: post saved ");
                if (onActionListener != null)
                    onActionListener.onSave(saved_id);
                mSavedPost.setText(mContext.getString(R.string.delete_from_savedposts));
                dismiss();
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: " + ex);
                Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();

            }
        });
    }


    /**
     * hiding a post from my mainFeed
     */
    private void hidePost() {
        PostMethods.hidePost(post_id, current_user_id, TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnHidingPostListener() {
            @Override
            public void OnPostHide() {
                Log.d(TAG, "OnPostHide: post hidden ");
                if (onActionListener != null)
                    onActionListener.onHide();
                dismiss();
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);
                Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();

            }
        });

    }


    /**
     * Reporting a post
     */
    private void reportPost() {
        PostMethods.reportPost(post_id, current_user_id, 0,
                TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnReportingPostListener() {
                    @Override
                    public void OnPostReported() {
                        Log.d(TAG, "OnPostReported: post reported ");

                        if (onActionListener != null)
                            onActionListener.onReport();

                        dismiss();
                    }

                    @Override
                    public void OnServerException(String ex) {
                        Log.d(TAG, "OnServerException: " + ex);
                    }

                    @Override
                    public void OnFailure(String ex) {
                        Log.d(TAG, "OnFailure: " + ex);
                        Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    /**
     * deleting a post
     */
    private void deletePost() {
        PostMethods.deletePost(post_id, new PostMethods.OnDeletingPostListener() {
            @Override
            public void OnDeleted() {
                Log.d(TAG, "OnDeleted: deleted ");

                if (onActionListener != null)
                    onActionListener.onDelete();

                dismiss();
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);

            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);
                Toast.makeText(mContext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
