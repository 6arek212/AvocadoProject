package com.example.testavocado.Dialogs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.Home.adapters.CommentsAdapter;
import com.example.testavocado.Home.adapters.PostsAdapter;
import com.example.testavocado.Models.Comment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;


public class CommentsDialog extends DialogFragment {
    private static final String TAG = "CommentsDialog";


    public PostsAdapter postsAdapter;
    public TextView mCommentCount;
    public int post_id, post_comments_count, post_userId;
    public int index;

    //widgets
    private RecyclerView recyclerView;
    private EditText mAddCommentText;
    private FloatingActionButton mAddComment;
    private ImageView mClose;
    private SwipyRefreshLayout swipeRefreshLayout;

    //vars
    private CommentsAdapter adapter;
    private static boolean asyncFlag;
    private static int COMMENT_CHECK = 2000;
    private mAsyncTask asyncTask;
    private Context mContext;
    private int current_userId;
    private String datetime;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_comments_view, container, false);
        mContext = getContext();
        current_userId = HelpMethods.checkSharedPreferencesForUserId(mContext);
        datetime = TimeMethods.getUTCdatetimeAsString();

        initWidgets(view);
        g1(post_id, 0);
        return view;
    }


    /**
     * initializing all the wedgits + attaching onClicks
     * +starting the async task for getting the updated comments
     *
     * @param view
     */

    private void initWidgets(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        mAddCommentText = view.findViewById(R.id.addCommentText);
        mAddComment = view.findViewById(R.id.add);
        mClose = view.findViewById(R.id.close);
        swipeRefreshLayout = view.findViewById(R.id.swipe);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        initCommentAdapter();

        HelpMethods.closeKeyboard(getActivity());

        asyncFlag = true;
        asyncTask = new mAsyncTask();
        asyncTask.execute();

        mAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mAddCommentText.getText().toString();

                if (text.trim().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.COMMENT_TEXT), Toast.LENGTH_SHORT).show();

                } else {

                    int current_user_id = HelpMethods.checkSharedPreferencesForUserId(getContext());
                    if (current_user_id != getContext().getResources().getInteger(R.integer.defaultValue)) {
                        //  addNewComment(post_id, current_user_id, text, TimeMethods.getUTCdatetimeAsString());

                        CommentMethodsHandler.addNewComment(post_id, current_user_id, text, TimeMethods.getUTCdatetimeAsString(), new CommentMethodsHandler.OnAddingNewCommentsListener() {
                            @Override
                            public void onSuccessListener() {
                                Toast.makeText(getContext(), "comment added", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onServerException(String ex) {

                            }

                            @Override
                            public void onFailureListener(String ex) {

                            }
                        });
                        mAddCommentText.setText("");
                        HelpMethods.closeKeyboard2(getView(), getContext());
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.BOTTOM ? "top" : "bottom"));

                if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    //CommentMethodsHandler.getTop10Comments(post_id,0,new );
                    datetime = TimeMethods.getUTCdatetimeAsString();
                    g1(post_id, 0);
                    swipeRefreshLayout.setRefreshing(false);
                } else if (direction == SwipyRefreshLayoutDirection.TOP) {
                    //CommentMethodsHandler.getTop10Comments(adapter.getItemCount());
                    g1(post_id, adapter.getItemCount());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disableAsyncTask();
                dismiss();
            }
        });

    }


    /**
     * initializing the recycler view comments adapter
     */

    private void initCommentAdapter() {
        adapter = new CommentsAdapter(mContext, new CommentsAdapter.OnDeletingCommentListener() {
            @Override
            public void OnDeleteComment() {
                post_comments_count--;
                if (index != -1)
                    postsAdapter.postsList.get(index).setPost_comments_count(post_comments_count);
                mCommentCount.setText(post_comments_count + "");
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.post_id=post_id;
        if (current_userId == post_userId)
            adapter.is_user_Post = true;
        else
            adapter.is_user_Post = false;
    }


    private void g1(int post_id, int offset) {
        Log.d(TAG, "g1: userId= "+current_userId+"  postId= "+post_id+"  comments count "+post_comments_count);
        if (offset == 0) {
            adapter.clearList();
        }

        CommentMethodsHandler.getComments(post_id, offset, datetime, new CommentMethodsHandler.OnGettingPostCommentsListener() {
            @Override
            public void onSuccessListener(List<Comment> comments) {
                adapter.addSetOfNewComment(comments,adapter.getItemCount());
                if(comments.size()>post_comments_count)
                {
                    post_comments_count=comments.size();
                    if (index != -1)
                        postsAdapter.postsList.get(index).setPost_comments_count(post_comments_count);
                    mCommentCount.setText(post_comments_count + "");
                }
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


    private void updatedComments(int post_id) {
        CommentMethodsHandler.getUpdatedComments(post_id, post_comments_count, datetime, new CommentMethodsHandler.OnGettingUpdatedCommentsListener() {
            @Override
            public void onSuccessListener(List<Comment> comments) {
                adapter.addSetOfNewCommentTop(comments);
                post_comments_count += comments.size();

                if (index != -1)
                    postsAdapter.postsList.get(index).setPost_comments_count(post_comments_count);
                mCommentCount.setText(post_comments_count + "");
                // updateListener.onUpdate(post_comments_count, index);
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


    private void disableAsyncTask() {
        asyncFlag = false;
        asyncTask.cancel(true);
    }


    /**
     * on dismiss the dialog the async task
     * will be cancelled
     */

    @Override
    public void onStop() {
        disableAsyncTask();
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    /**
     * the async task for getting the new comments added
     * <p>
     * in Real Time
     */
    public class mAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (asyncFlag) {
                Log.d(TAG, "doInBackground: ");
                publishProgress();

                try {
                    Thread.sleep(COMMENT_CHECK);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate adapter: " + adapter.getItemCount() + "   comment count " + post_comments_count);
            // getUpdatedComments(post_id, post_comments_count);
            updatedComments(post_id);

        }
    }


}
