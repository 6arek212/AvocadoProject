package com.example.testavocado.Home.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.BaseActivity;
import com.example.testavocado.Dialogs.CommentMethodsHandler;
import com.example.testavocado.Dialogs.CommentsDialog;
import com.example.testavocado.Dialogs.ConfirmDialog;
import com.example.testavocado.Home.AddNewPostActivity;
import com.example.testavocado.Home.BottomSheetDialog;
import com.example.testavocado.Home.Fragments.LikesDislikesFragment;
import com.example.testavocado.Home.Fragments.MainFeedFragment;
import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Profile.ProfileFragment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.ImagesViewPagerClick;
import com.example.testavocado.Utils.PostFragment;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.testavocado.Home.PostMethods.FRIENDS_POSTS;
import static com.example.testavocado.Home.PostMethods.PUBLIC_FRIENDS_POSTS;
import static com.example.testavocado.Home.PostMethods.PUBLIC_POSTS;

public class PostsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PostsAdapter";


    public boolean is_endOfPosts;


    public void addNull() {
        postsList.add(null);
        notifyItemInserted(postsList.size());
    }

    public void removeProg() {
        if (postsList.get(postsList.size() - 1) == null) {
            postsList.remove(postsList.size() - 1);
            notifyItemRemoved(postsList.size());
        }
    }


    public List<Post> postsList = new ArrayList();
    FragmentManager fragmentManager;
    Context mContext;
    int user_id;
    boolean firstTime;


    public PostsAdapter(FragmentManager fragmentManager, Context mContext, int user_id, MainFeedFragment fragment) {
        this.fragmentManager = fragmentManager;
        this.mContext = mContext;
        this.user_id = user_id;
        postsList.add(null);
        is_endOfPosts = false;
        feedFragment = fragment;
        dontShow = true;
        firstTime = true;
    }


    public void clear() {
        postsList.clear();
        postsList.add(null);
    }


    public void addSetOfPosts(List<Post> posts, int s) {
        postsList.addAll(posts);
        notifyItemRangeInserted(s, posts.size());
    }




    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + position);

        if (position == 0)
            return 1;
        else if (postsList.get(position) != null)
            return 0;
        else if (postsList.get(position) == null && !is_endOfPosts)
            return 2;
        else
            return 3;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: " + i);
        View view;
        RecyclerView.ViewHolder vh;

        if (i == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toptop, viewGroup, false);
            vh = new TopViewHolder(view);
        } else if (i == 2) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progressbar_item, viewGroup, false);

            vh = new ProgressViewHolder(v);
        } else if (i == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_item, viewGroup, false);
            vh = new PostViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_end_item, viewGroup, false);
            vh = new EndViewHolder(view);
        }

        return vh;
    }


    MainFeedFragment feedFragment;
    public boolean dontShow;
    public int post_type;

    private void handlingCheckBox(CheckBox friends, CheckBox publicPosts) {
        int size = postsList.size();
        dontShow = true;


        if (friends.isChecked() && publicPosts.isChecked()) {
            //get friend and public posts
            feedFragment.getPosts(0, PUBLIC_FRIENDS_POSTS);
            post_type = PUBLIC_FRIENDS_POSTS;
        } else if (friends.isChecked() && !publicPosts.isChecked()) {
            feedFragment.getPosts(0, FRIENDS_POSTS);
            post_type = FRIENDS_POSTS;


        } else if (!friends.isChecked() && publicPosts.isChecked()) {
            //get public posts
            feedFragment.getPosts(0, PUBLIC_POSTS);
            post_type = PUBLIC_POSTS;

        } else {
            Log.d(TAG, "handlingCheckBox: delete posts" + postsList.size());
            postsList.clear();
            addNull();
            notifyItemRangeRemoved(1, size);
            dontShow = false;

        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int index = i;

        if (viewHolder.getItemViewType() == 1) {
            final TopViewHolder v1 = (TopViewHolder) viewHolder;
            v1.mAddPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).startActivity(new Intent(mContext, AddNewPostActivity.class));
                }
            });

            if (firstTime) {
                v1.mFriendsPosts.setChecked(true);
                firstTime = false;
            }
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handlingCheckBox(v1.mFriendsPosts, v1.mPublicPosts);
                }
            };

            v1.mPublicPosts.setOnCheckedChangeListener(onCheckedChangeListener);
            v1.mFriendsPosts.setOnCheckedChangeListener(onCheckedChangeListener);

        } else if (viewHolder.getItemViewType() == 0) {
            final PostViewHolder v1 = (PostViewHolder) viewHolder;

            v1.mPostUserName.setText(postsList.get(i).getUser_name() + " " + postsList.get(i).getUser_last_name());
            v1.mPostText.setText(postsList.get(i).getPost_text());
            v1.mPostLikes.setText(String.valueOf(postsList.get(i).getPost_likes_count()));
            v1.mPostComments.setText(String.valueOf(postsList.get(i).getPost_comments_count()));
            v1.mPostShares.setText(String.valueOf(postsList.get(i).getPost_share_count()));
            v1.mPostTime.setText(postsList.get(i).getPost_date_time());

            Glide.with(mContext)
                    .load(postsList.get(i).getUser_profile_photo())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(v1.mProfileImage);


            if (postsList.get(i).isPost_is_shared()) {
                v1.mSharedPost.setVisibility(View.VISIBLE);
                v1.share.setVisibility(View.GONE);
                attachOriginalPostFragment(v1.mSharedPost, i);
            } else {
                v1.share.setVisibility(View.VISIBLE);
                v1.mSharedPost.setVisibility(View.GONE);
            }


            if (postsList.get(i).getLike_id() != -1) {
                v1.like.setText("Liked");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    v1.like.setTextColor(mContext.getColor(R.color.colorBlue));
                }
            } else {
                v1.like.setText("Like");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    v1.like.setTextColor(mContext.getColor(android.R.color.tab_indicator_text));
                }
            }


            if (postsList.get(i).getDis_like_id() != -1) {
                v1.dislike.setText("Disliked");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    v1.dislike.setTextColor(mContext.getColor(R.color.colorRed));
                }
            } else {
                v1.dislike.setText("Dislike");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    v1.dislike.setTextColor(mContext.getColor(android.R.color.tab_indicator_text));
                }

            }


            v1.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v1.like.getText().equals("Liked")) {
                        removeLike((TextView) v, v1.mPostLikes, index);

                    } else {

                        addLike((TextView) v, v1.mPostLikes, index);

                    }
                }
            });


            v1.dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v1.dislike.getText().equals("Disliked")) {
                        removeDisLike((TextView) v, index);

                    } else {

                        addDisLike((TextView) v, index);
                    }
                }
            });


            if (postsList.get(i).getPost_images_url().isEmpty()) {
                v1.mImageSlider.setVisibility(View.GONE);
                v1.mDots.setVisibility(View.GONE);
                v1.mPhotoLayout.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "onBindViewHolder:  url " + postsList.get(i).getPost_images_url());
                initializeViewPage(v1, i);
            }


            attachOnClickProfileImage(v1.mProfileImage, v1.mPostUserName, i);

            initPostCommentsDialog(v1.commentsLayout, v1.mPostComments, i);

            addComment(v1.mSend, v1.mComment, v1.mPostComments, i);


            attachLikeFragment(v1.likeLayout, i);

            attachShareOnClick(v1.share, i);

            attachBottomSheet(v1.mPostOptions, i);


        } else if (!is_endOfPosts) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }

        if (i == postsList.size() - 1) {
            Log.d(TAG, "onBindViewHolder: bottom ");
        }

    }


    private void initializeViewPage(final PostViewHolder v1, final int position) {
        v1.mPhotoLayout.setVisibility(View.VISIBLE);
        v1.mImageSlider.setVisibility(View.VISIBLE);
        v1.mDots.setVisibility(View.VISIBLE);
        ImagesViewPagerClick adapter = new ImagesViewPagerClick(mContext, postsList.get(position).getPost_images_url(), fragmentManager);
        v1.mImageSlider.setAdapter(adapter);
        v1.mDots.setupWithViewPager(v1.mImageSlider);
    }


    private void attachBottomSheet(ImageView mPostOptions, final int i) {
        mPostOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.adapter = PostsAdapter.this;
                bottomSheetDialog.index = i;
                bottomSheetDialog.show(fragmentManager, "bottomSheetDialog");
            }
        });
    }


    private void attachOriginalPostFragment(TextView mShared, final int i) {
        mShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frt = fragmentManager.beginTransaction();
                frt.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                PostFragment fragment = new PostFragment();
                fragment.post_id = postsList.get(i).getOriginal_post_id();
                frt.replace(R.id.mainLayoutPosts, fragment).addToBackStack(mContext.getString(R.string.post_fragment))
                        .commit();
            }
        });
    }


    private void attachShareOnClick(TextView share, final int i) {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    final ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setTitle("Are you sure you want to share " + postsList.get(i).getUser_name() + " " + postsList.get(i).getUser_last_name() + "  ?");
                    confirmDialog.setOnConfirm(new ConfirmDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            final Post post = new Post();
                            post.setUser_id(user_id);
                            post.setPost_text(postsList.get(i).getPost_text());
                            post.setPost_images_url(postsList.get(i).getPost_images_url());
                            post.setPost_date_time(TimeMethods.getUTCdatetimeAsString());
                            post.setPost_type(postsList.get(i).getPost_type());
                            post.setPost_is_shared(true);
                            post.setOriginal_post_id(postsList.get(i).getPost_id());


                            PostMethods.sharePost(post, new PostMethods.OnSharingPostListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "onSuccess: shared a post :D " + post);
                                    Toast.makeText(mContext, "Post Shared", Toast.LENGTH_SHORT).show();
                                    confirmDialog.dismiss();
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Log.d(TAG, "onServerException: error sharing post " + ex);
                                    Toast.makeText(mContext, mContext.getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                                    confirmDialog.dismiss();

                                }

                                @Override
                                public void onFailure(String ex) {
                                    Log.d(TAG, "onFailure: error while sharing a post" + ex);
                                    Toast.makeText(mContext, mContext.getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                                    confirmDialog.dismiss();

                                }
                            });
                        }
                    });
                    confirmDialog.show(fragmentManager,mContext.getString(R.string.confirm_dialog));

                }
                catch (NullPointerException ex){
                    Log.e(TAG, "onClick: "+ex );
                }

            }
        });
    }


    private void attachLikeFragment(RelativeLayout likeLayout, final int i) {
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikesDislikesFragment fragment = new LikesDislikesFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(mContext.getString(R.string.post_id), postsList.get(i).getPost_id());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
                FragmentTransaction tr = fragmentManager.beginTransaction();
                tr.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                tr.replace(R.id.baseLayout, fragment)
                        .addToBackStack(mContext.getString(R.string.LikesDislikesFragment)).commit();
            }
        });
    }


    private void addComment(FloatingActionButton send, final EditText text, final TextView commentCount, final int i) {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentText = text.getText().toString();

                if (commentText.trim().isEmpty()) {
                    Toast.makeText(mContext, "cant add an empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    CommentMethodsHandler.addNewComment(postsList.get(i).getPost_id(), user_id, commentText, TimeMethods.getUTCdatetimeAsString(),
                            new CommentMethodsHandler.OnAddingNewCommentsListener() {
                                @Override
                                public void onSuccessListener() {
                                    Toast.makeText(mContext, "added", Toast.LENGTH_SHORT).show();
                                    int count = postsList.get(i).getPost_comments_count() + 1;
                                    commentCount.setText(count + "");
                                    postsList.get(i).setPost_comments_count(count);
                                    text.setText("");
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


    private void attachOnClickProfileImage(CircleImageView mProfileImage, TextView name, final int i) {
        mProfileImage.setOnClickListener(new onClickProfile(i));
        name.setOnClickListener(new onClickProfile(i));
    }


    class onClickProfile implements View.OnClickListener {
        private int i;

        public onClickProfile(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            ProfileFragment fragment = new ProfileFragment();

            if (user_id == postsList.get(i).getUser_id())
                fragment.is_current_user = true;
            else
                fragment.is_current_user = false;

            fragment.incoming_user_id = postsList.get(i).getUser_id();

            FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
            FragmentTransaction tr = fragmentManager.beginTransaction();
            tr.replace(R.id.mainLayoutPosts, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
        }
    }


    /**
     * Likes Methods
     *
     * @param btn
     * @param likes_count
     * @param index
     */

    private void addLike(final TextView btn, final TextView likes_count, final int index) {
        if (postsList.get(index).getDis_like_id() == -1) {
            try {
                PostMethods.likeAPost(user_id, postsList.get(index).getPost_id(), TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnLikingPostListener() {
                    @Override
                    public void OnLiked(int like_id) {
                        Log.d(TAG, "OnLiked: post likes " + like_id);

                        postsList.get(index).setLike_id(like_id);
                        btn.setText("Liked");
                        int likeCount = postsList.get(index).getPost_likes_count() + 1;
                        likes_count.setText(likeCount + "");
                        postsList.get(index).setPost_likes_count(likeCount);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btn.setTextColor(mContext.getColor(R.color.colorBlue));
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
            } catch (IndexOutOfBoundsException ex) {

            }
        }

    }


    private void removeLike(final TextView btn, final TextView likes_count, final int index) {
        PostMethods.removeLike(postsList.get(index).getLike_id(), postsList.get(index).getPost_id(), new PostMethods.OnRemovingLikingPostListener() {
            @Override
            public void OnLikeRemoved() {
                Log.d(TAG, "OnLikeRemoved: ");
                postsList.get(index).setLike_id(-1);
                btn.setText("Like");

                int likeCount = postsList.get(index).getPost_likes_count() - 1;
                likes_count.setText(likeCount + "");
                postsList.get(index).setPost_likes_count(likeCount);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    btn.setTextColor(mContext.getColor(android.R.color.tab_indicator_text));
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


    private void addDisLike(final TextView btn, final int index) {
        if (postsList.get(index).getLike_id() == -1) {
            try {
                PostMethods.disLikePost(user_id, postsList.get(index).getPost_id(), TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnDisLikingPostListener() {
                    @Override
                    public void OnDisLiked(int dislike_id) {
                        Log.d(TAG, "OnDisLiked: " + dislike_id);

                        postsList.get(index).setDis_like_id(dislike_id);
                        btn.setText("Disliked");
                        int dislikeCount = postsList.get(index).getPost_dislike_count() + 1;
                        postsList.get(index).setPost_dislike_count(dislikeCount);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btn.setTextColor(mContext.getColor(R.color.colorRed));
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
            } catch (IndexOutOfBoundsException ex) {

            }
        }

    }


    private void removeDisLike(final TextView btn, final int index) {
        PostMethods.removeDisLike(postsList.get(index).getDis_like_id(), postsList.get(index).getPost_id(), new PostMethods.OnRemovingLikingPostListener() {
            @Override
            public void OnLikeRemoved() {
                Log.d(TAG, "OnDisLikeRemoved: ");
                postsList.get(index).setDis_like_id(-1);
                btn.setText("DisLike");

                int likeCount = postsList.get(index).getPost_likes_count() - 1;
                postsList.get(index).setPost_likes_count(likeCount);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    btn.setTextColor(mContext.getColor(android.R.color.tab_indicator_text));
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


    public void initPostCommentsDialog(final RelativeLayout comments, final TextView commentsCount, final int index) {

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsDialog commentsDialog = new CommentsDialog();
                commentsDialog.index = index;
                commentsDialog.post_id = postsList.get(index).getPost_id();
                commentsDialog.post_comments_count = postsList.get(index).getPost_comments_count();
                commentsDialog.postsAdapter = PostsAdapter.this;
                commentsDialog.mCommentCount = commentsCount;
                commentsDialog.post_userId = postsList.get(index).getUser_id();
                commentsDialog.show(fragmentManager, mContext.getString(R.string.commentsDialog));
            }
        });
    }


    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mPostUserName, mPostText, mPostTime, mPostLikes, mPostComments, mPostShares, like, share, dislike, mSharedPost;
        ImageView mPostOptions;
        FloatingActionButton mSend;
        EditText mComment;
        RelativeLayout commentsLayout, likeLayout, mPhotoLayout;
        ViewPager mImageSlider;
        TabLayout mDots;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfileImage = itemView.findViewById(R.id.profileImage);
            mPostUserName = itemView.findViewById(R.id.postUserName);
            mPostText = itemView.findViewById(R.id.postText);
            mPostTime = itemView.findViewById(R.id.postTime);
            mPostLikes = itemView.findViewById(R.id.postLikesCount);
            mPostComments = itemView.findViewById(R.id.postCommentsCount);
            mPostShares = itemView.findViewById(R.id.postSharesCount);
            mPostOptions = itemView.findViewById(R.id.postOptions);
            like = itemView.findViewById(R.id.like);
            share = itemView.findViewById(R.id.share);
            dislike = itemView.findViewById(R.id.dislike);
            mSend = itemView.findViewById(R.id.send);
            mComment = itemView.findViewById(R.id.commentText);
            commentsLayout = itemView.findViewById(R.id.commentsLayout);
            likeLayout = itemView.findViewById(R.id.likeLayout);
            mSharedPost = itemView.findViewById(R.id.sharedPost);
            mImageSlider = itemView.findViewById(R.id.viewPagerImages);
            mDots = itemView.findViewById(R.id.tablayoutDots);
            mPhotoLayout = itemView.findViewById(R.id.relLayout4);
        }
    }


    public class TopViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        RelativeLayout mAddPost;
        CheckBox mFriendsPosts, mPublicPosts;


        public TopViewHolder(@NonNull View itemView) {
            super(itemView);

            mAddPost = itemView.findViewById(R.id.addPost);
            mFriendsPosts = itemView.findViewById(R.id.friendPosts);
            mPublicPosts = itemView.findViewById(R.id.publicPosts);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    public static class EndViewHolder extends RecyclerView.ViewHolder {
        TextView end;

        public EndViewHolder(View v) {
            super(v);
            end = (TextView) v.findViewById(R.id.end);
        }
    }


}

