package com.example.testavocado.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.testavocado.Home.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.BaseActivity;
import com.example.testavocado.Dialogs.CommentMethodsHandler;
import com.example.testavocado.Dialogs.CommentsDialog;
import com.example.testavocado.Home.Fragments.LikesDislikesFragment;
import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostFragment extends Fragment {
    private static final String TAG = "PostFragment";


    //Widgets
    private CircleImageView mProfileImage;
    private TextView mPostUserName, mPostTime, mPostLikes, mPostCommentsCount, mPostShares, like, share, dislike, mSharedPost, mPostRemoved,expand;
    private ImageView mPostOptions;
    private TabLayout mDots;
    private ViewPager mImageSlider;
    private FloatingActionButton mSend;
    private EditText mComment;
    private ConstraintLayout commentsLayout, likeLayout, mPhotoLayout;
    private ScrollView mPostLayout;
    private SwipeRefreshLayout mSwipe;
    private CoordinatorLayout mainLayout;
    private ExpandableTextView mPostText;


    //vsrs
    private int user_id;
    private Post post;
    public int post_id;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        Log.d(TAG, "onCreateView: post_id :  " + post_id);

        initWidgets(view);


        return view;
    }


    /**
     * initializing all the widgets and getting the post
     *
     * @param view
     */
    private void initWidgets(View view) {
        mContext = getContext();
        mProfileImage = view.findViewById(R.id.profileImage);
        mPostUserName = view.findViewById(R.id.postUserName);
        mPostText = view.findViewById(R.id.expandableTextView);
        mPostTime = view.findViewById(R.id.postTime);
        mPostLikes = view.findViewById(R.id.postLikesCount);
        mPostRemoved = view.findViewById(R.id.postRemoved);
        mPostCommentsCount = view.findViewById(R.id.postCommentsCount);
        mPostShares = view.findViewById(R.id.postSharesCount);
        mPostOptions = view.findViewById(R.id.postOptions);
        mImageSlider = view.findViewById(R.id.viewPagerImages);
        mDots = view.findViewById(R.id.tablayoutDots);
        mSwipe = view.findViewById(R.id.swipe);
        mPhotoLayout = view.findViewById(R.id.mPhotoLayout);
        like = view.findViewById(R.id.like);
        share = view.findViewById(R.id.share);
        mainLayout=view.findViewById(R.id.mainLayout);
        dislike = view.findViewById(R.id.dislike);
        mSend = view.findViewById(R.id.send);
        mPostLayout = view.findViewById(R.id.layoutPost);
        mComment = view.findViewById(R.id.commentText);
        commentsLayout = view.findViewById(R.id.commentsLayout);
        likeLayout = view.findViewById(R.id.likeLayout);
        mSharedPost = view.findViewById(R.id.sharedPost);
        user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        mPostRemoved.setVisibility(View.GONE);
        mPostLayout.setVisibility(View.GONE);
        expand=view.findViewById(R.id.button_toggle);
        mPostText.setInterpolator(new OvershootInterpolator());
        getPost();

        mPostOptions.setVisibility(View.GONE);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPost();
            }
        });
    }


    /**
     * filling the widgets with the post Data
     */
    private void publishPost() {
        mPostUserName.setText(post.getUser_name() + " " + post.getUser_last_name());
        mPostText.setText(post.getPost_text());


        expand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (mPostText.isExpanded())
                {
                    mPostText.collapse();
                    expand.setBackground(mContext.getDrawable(R.drawable.ic_ex));
                }
                else
                {
                    mPostText.expand();
                    expand.setBackground(mContext.getDrawable(R.drawable.ic_col));
                }
            }
        });
        mPostLikes.setText(String.valueOf(post.getPost_likes_count()));
        mPostCommentsCount.setText(String.valueOf(post.getPost_comments_count()));
        mPostShares.setText(String.valueOf(post.getPost_share_count()));
        mPostTime.setText(post.getPost_date_time());

        Glide.with(mContext)
                .load(post.getUser_profile_photo())
                .centerCrop()
                .error(R.drawable.error)
                .into(mProfileImage);


        if (post.getPost_images_url().isEmpty()) {
            mImageSlider.setVisibility(View.GONE);
            mDots.setVisibility(View.GONE);
            mPhotoLayout.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "onBindViewHolder:  url " + post.getPost_images_url());
            initializeViewPage();
        }


        if (post.getLike_id() != -1) {
            like.setText("Liked");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                like.setTextColor(mContext.getColor(R.color.colorBlue));
            }
        } else
            like.setText("Like");


        Log.d(TAG, "onBindViewHolder: dislike " + post.getDis_like_id());
        if (post.getDis_like_id() != -1) {
            dislike.setText("Disliked");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dislike.setTextColor(mContext.getColor(R.color.colorRed));
            }
        } else
            dislike.setText("Dislike");

    }


    private void initializeViewPage() {
        mPhotoLayout.setVisibility(View.VISIBLE);
        mImageSlider.setVisibility(View.VISIBLE);
        mDots.setVisibility(View.VISIBLE);
        ImagesViewPagerClick adapter = new ImagesViewPagerClick(mContext, post.getPost_images_url(), getFragmentManager());
        mImageSlider.setAdapter(adapter);
        mDots.setupWithViewPager(mImageSlider);
    }


    /**
     * getting the post data
     */
    private void getPost() {
        PostMethods.getPostById(post_id, user_id, new PostMethods.OnGettingPostByIdListener() {
            @Override
            public void onSuccess(String json) {
                Log.d(TAG, "onSuccess: " + json);
                new getPostAsyncTsat(PostFragment.this).execute(json);

            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
                mPostRemoved.setVisibility(View.VISIBLE);
                mPostLayout.setVisibility(View.GONE);
                mSwipe.setRefreshing(false);

            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: " + ex);
                mSwipe.setRefreshing(false);
                mPostRemoved.setVisibility(View.GONE);
                mPostLayout.setVisibility(View.GONE);
                if (getActivity()!=null)
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * initializing the post data that came from the server
     */
    private static class getPostAsyncTsat extends AsyncTask<String, Void, Post> {
        WeakReference<PostFragment> postFragment;

        getPostAsyncTsat(PostFragment fragment) {
            postFragment = new WeakReference<PostFragment>(fragment);
        }

        @Override
        protected Post doInBackground(String... strings) {
            Post post1 = null;
            try {
                JSONArray jsonArray = new JSONArray(strings[0]);
                post1 = new Gson().fromJson(jsonArray.get(0).toString(), Post.class);
                post1.setPost_date_time(TimeMethods.getTimestampDifference(post1.getPost_date_time()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return post1;
        }

        @Override
        protected void onPostExecute(Post post1) {
            super.onPostExecute(post1);
            Log.d(TAG, "onPostExecute: " + post1);
            final PostFragment fragment = postFragment.get();
            if (fragment == null || fragment.isRemoving())
                return;

            if (post1 != null) {
                fragment.post = post1;
                fragment.publishPost();
                fragment.attachShareOnClick();
                fragment.attachLikeFragment();
                fragment.addComment();
                fragment.attachOnClickProfileImage();
                fragment.initPostCommentsDialog();
                fragment.likeDislikeHandler();
                fragment.attachBottomSheet();

                fragment.mPostRemoved.setVisibility(View.GONE);
                fragment.mPostLayout.setVisibility(View.VISIBLE);
                fragment.mSwipe.setRefreshing(false);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    /**
     * Functions to attach the events for the widgets
     */
    private void attachShareOnClick() {
        if (post.isPost_is_shared()) {
            share.setVisibility(View.GONE);
            mSharedPost.setVisibility(View.VISIBLE);
        } else {
            mSharedPost.setVisibility(View.GONE);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelpMethods.alertDialog(post,user_id,mContext);
                }
            });
        }
    }







    private void attachBottomSheet() {
        mPostOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.post_id =post_id;
                bottomSheetDialog.post_saved = post.getSaved_post_id();
                bottomSheetDialog.post_userId = post.getUser_id();

                bottomSheetDialog.setOnActionListener(new BottomSheetDialog.OnActionListener() {
                    @Override
                    public void onHide() {
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onDelete() {
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onReport() {

                    }

                    @Override
                    public void onSave(int saved_id) {
                        post.setSaved_post_id(saved_id);
                    }

                    @Override
                    public void onDeleteSavedPost() {
                        post.setSaved_post_id(0);
                    }
                });


                bottomSheetDialog.show(getFragmentManager(), "bottomSheetDialog");


            }
        });
    }













    private void attachLikeFragment() {
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikesDislikesFragment fragment = new LikesDislikesFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(mContext.getString(R.string.post_id), post.getPost_id());
                fragment.setArguments(bundle);
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                if(getActivity() instanceof BaseActivity){
                    tr.replace(R.id.baseLayout, fragment) .addToBackStack(mContext.getString(R.string.LikesDislikesFragment)).commit();
                }
                else{
                    tr.replace(R.id.mainLayoutConnection, fragment) .addToBackStack(mContext.getString(R.string.LikesDislikesFragment)).commit();
                }

            }
        });
    }


    private void addComment() {
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentText = mComment.getText().toString();

                if (commentText.trim().isEmpty()) {
                    Toast.makeText(mContext, "cant add an empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    CommentMethodsHandler.addNewComment(post.getPost_id(), user_id, commentText, TimeMethods.getUTCdatetimeAsString(),
                            new CommentMethodsHandler.OnAddingNewCommentsListener() {
                                @Override
                                public void onSuccessListener() {
                                    Toast.makeText(mContext, "added", Toast.LENGTH_SHORT).show();
                                    int count = post.getPost_comments_count() + 1;
                                    mPostCommentsCount.setText(count + "");
                                    post.setPost_comments_count(count);
                                    mComment.setText("");
                                    HelpMethods.closeKeyboard((BaseActivity) mContext);
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

            }
        });
    }


    private void attachOnClickProfileImage() {
        mProfileImage.setOnClickListener(new onClickProfile());
        mPostUserName.setOnClickListener(new onClickProfile());
    }


    class onClickProfile implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ProfileFragment fragment = new ProfileFragment();

            if (user_id == post.getUser_id()) {
                fragment.is_current_user = true;
            } else {
                fragment.is_current_user = false;
            }

            fragment.incoming_user_id = post.getUser_id();

            FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
            FragmentTransaction tr = fragmentManager.beginTransaction();


            if(getActivity() instanceof BaseActivity){
                tr.replace(R.id.baseLayout, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
            }
            else{
                tr.replace(R.id.mainLayoutConnection, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();

            }
        }
    }


    private void likeDislikeHandler() {

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like.getText().equals("Liked")) {
                    removeLike();

                } else {

                    addLike();

                }
            }
        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dislike.getText().equals("Disliked")) {
                    removeDisLike();

                } else {

                    addDisLike();
                }
            }
        });

    }


    private void addLike() {
        if (post.getDis_like_id() == -1) {
            try {
                PostMethods.likeAPost(user_id, post.getPost_id(), TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnLikingPostListener() {
                    @Override
                    public void OnLiked(int like_id) {
                        Log.d(TAG, "OnLiked: post likes " + like_id);

                        post.setLike_id(like_id);
                        like.setText("Liked");
                        int likeCount = post.getPost_likes_count() + 1;
                        mPostLikes.setText(likeCount + "");
                        post.setPost_likes_count(likeCount);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            like.setTextColor(mContext.getColor(R.color.colorBlue));
                        }
                    }

                    @Override
                    public void OnServerException(String ex) {
                        Log.d(TAG, "OnServerException: " + ex);
                        Toast.makeText(mContext, "You already liked or disliked the post", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void OnFailure(String ex) {
                        Log.d(TAG, "OnFailure: " + ex);
                    }
                });
            } catch (IndexOutOfBoundsException ex) {

            }
        }else{
            Toast.makeText(mContext, "You already disliked the post", Toast.LENGTH_SHORT).show();
        }

    }

    private void removeLike() {
        PostMethods.removeLike(post.getLike_id(), post_id, new PostMethods.OnRemovingLikingPostListener() {
            @Override
            public void OnLikeRemoved() {
                Log.d(TAG, "OnLikeRemoved: ");
                post.setLike_id(-1);
                like.setText("Like");

                int likeCount = post.getPost_likes_count() - 1;
                mPostLikes.setText(likeCount + "");
                post.setPost_likes_count(likeCount);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    like.setTextColor(mContext.getColor(android.R.color.tab_indicator_text));
                }
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

    private void addDisLike() {
        if (post.getLike_id() == -1) {
            try {
                PostMethods.disLikePost(user_id, post.getPost_id(), TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnDisLikingPostListener() {
                    @Override
                    public void OnDisLiked(int dislike_id) {
                        Log.d(TAG, "OnDisLiked: " + dislike_id);

                        post.setDis_like_id(dislike_id);
                        dislike.setText("Disliked");
                        int dislikeCount = post.getPost_dislike_count() + 1;
                        post.setPost_dislike_count(dislikeCount);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dislike.setTextColor(mContext.getColor(R.color.colorRed));
                        }
                    }

                    @Override
                    public void OnServerException(String ex) {
                        Log.d(TAG, "OnServerException: " + ex);
                        Toast.makeText(mContext, "You already liked or disliked the post", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnFailure(String ex) {
                        Log.d(TAG, "OnFailure: " + ex);
                    }
                });
            } catch (IndexOutOfBoundsException ex) {

            }
        }else{
            Toast.makeText(mContext, "You already liked the post", Toast.LENGTH_SHORT).show();

        }

    }

    private void removeDisLike() {
        PostMethods.removeDisLike(post.getDis_like_id(), post_id, new PostMethods.OnRemovingLikingPostListener() {
            @Override
            public void OnLikeRemoved() {
                Log.d(TAG, "OnDisLikeRemoved: ");
                post.setDis_like_id(-1);
                dislike.setText("DisLike");

                int likeCount = post.getPost_dislike_count() - 1;
                post.setPost_likes_count(likeCount);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dislike.setTextColor(mContext.getColor(android.R.color.tab_indicator_text));
                }
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


    public void initPostCommentsDialog() {
        commentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsDialog commentsDialog = new CommentsDialog();
                commentsDialog.index = -1;
                commentsDialog.post_id = post_id;
                commentsDialog.post_comments_count = post.getPost_comments_count();
                commentsDialog.mCommentCount = mPostCommentsCount;
                commentsDialog.post_userId = post.getUser_id();
                commentsDialog.show(getFragmentManager(), getString(R.string.commentsDialog));
            }
        });
    }


}
