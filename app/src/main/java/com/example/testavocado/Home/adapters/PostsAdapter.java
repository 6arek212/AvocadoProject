package com.example.testavocado.Home.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.testavocado.Home.BottomSheetDialog;
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
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
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

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;


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
        Show = true;
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
    public boolean Show;
    public int post_type;


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

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


            feedFragment.handleCheckBOx(v1.mPublicPosts, v1.mFriendsPosts);
            feedFragment.setProfileImage(v1.mProfileImage);

        } else if (viewHolder.getItemViewType() == 0) {
            final PostViewHolder v1 = (PostViewHolder) viewHolder;

            v1.mPostUserName.setText(postsList.get(i).getUser_name() + " " + postsList.get(i).getUser_last_name());

            v1.mPostText.setText(postsList.get(i).getPost_text());


            v1.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (v1.mPostText.isExpanded()) {
                        v1.mPostText.collapse();
                        v1.expand.setBackground(mContext.getDrawable(R.drawable.ic_ex));
                    } else {
                        v1.mPostText.expand();
                        v1.expand.setBackground(mContext.getDrawable(R.drawable.ic_col));
                    }
                }
            });


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
                v1.originalPost();
                v1.mShareLayout.setVisibility(View.GONE);

            } else {
                v1.share.setVisibility(View.VISIBLE);
                v1.mSharedPost.setVisibility(View.GONE);
                v1.mShareLayout.setVisibility(View.VISIBLE);
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



            if (postsList.get(i).getPost_images_url().isEmpty()) {
                v1.mImageSlider.setVisibility(View.GONE);
                v1.mDots.setVisibility(View.GONE);
                v1.mPhotoLayout.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "onBindViewHolder:  url " + postsList.get(i).getPost_images_url());
                v1.viewPager();
            }


            v1.initLike();
            v1.profileImage();
            v1.postCommentDialog();
            v1.comment();
            v1.like();
            v1.share();
            v1.bottomSheet();

        } else if (!is_endOfPosts) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }

        if (i == postsList.size() - 1) {
            Log.d(TAG, "onBindViewHolder: bottom ");
        }

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




    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mPostUserName, mPostTime, mPostLikes, mPostComments, mPostShares, like, share, dislike, mSharedPost, expand;
        ImageView mPostOptions;
        FloatingActionButton mSend;
        EditText mComment;
        RelativeLayout commentsLayout, likeLayout, mPhotoLayout, mShareLayout;
        ViewPager mImageSlider;
        TabLayout mDots;
        ExpandableTextView mPostText;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfileImage = itemView.findViewById(R.id.profileImage);
            mPostUserName = itemView.findViewById(R.id.postUserName);
            mPostText = itemView.findViewById(R.id.expandableTextView);
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
            mShareLayout = itemView.findViewById(R.id.shareLayout);
            expand = itemView.findViewById(R.id.button_toggle);


            mPostText.setInterpolator(new OvershootInterpolator());
        }

        public void share(){
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        final ConfirmDialog confirmDialog = new ConfirmDialog();
                        confirmDialog.setTitle("Are you sure you want to share " + postsList.get(getAdapterPosition()).getUser_name() + " " + postsList.get(getAdapterPosition()).getUser_last_name() + "  ?");
                        confirmDialog.setOnConfirm(new ConfirmDialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                final Post post = new Post();
                                post.setUser_id(user_id);
                                post.setPost_text(postsList.get(getAdapterPosition()).getPost_text());
                                post.setPost_images_url(postsList.get(getAdapterPosition()).getPost_images_url());
                                post.setPost_date_time(TimeMethods.getUTCdatetimeAsString());
                                post.setPost_type(postsList.get(getAdapterPosition()).getPost_type());
                                post.setPost_is_shared(true);
                                post.setOriginal_post_id(postsList.get(getAdapterPosition()).getPost_id());


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
                        confirmDialog.show(fragmentManager, mContext.getString(R.string.confirm_dialog));

                    } catch (NullPointerException ex) {
                        Log.e(TAG, "onClick: " + ex);
                    }
                }
            });
        }


        public void bottomSheet() {
            mPostOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                    bottomSheetDialog.post_id = postsList.get(getAdapterPosition()).getPost_id();
                    bottomSheetDialog.post_saved = postsList.get(getAdapterPosition()).getSaved_post_id();
                    bottomSheetDialog.post_userId = postsList.get(getAdapterPosition()).getUser_id();

                    Log.d(TAG, "onClick: index " + getAdapterPosition() + "  " + postsList.size());

                    bottomSheetDialog.setOnActionListener(new BottomSheetDialog.OnActionListener() {
                        @Override
                        public void onHide() {
                            try{
                                postsList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onDelete() {
                            try{
                                postsList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onReport() {

                        }

                        @Override
                        public void onSave(int saved_id) {
                            postsList.get(getAdapterPosition()).setSaved_post_id(saved_id);
                            Toast.makeText(mContext, mContext.getString(R.string.post_saved), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDeleteSavedPost() {
                            postsList.get(getAdapterPosition()).setSaved_post_id(0);
                        }
                    });


                    bottomSheetDialog.show(fragmentManager, "bottomSheetDialog");
                }
            });

        }

        public void like() {
            likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LikesDislikesFragment fragment = new LikesDislikesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(mContext.getString(R.string.post_id), postsList.get(getAdapterPosition()).getPost_id());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction tr = fragmentManager.beginTransaction();
                    tr.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                    tr.replace(R.id.baseLayout, fragment)
                            .addToBackStack(mContext.getString(R.string.LikesDislikesFragment)).commit();
                }
            });
        }


        public void comment() {
            mSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String commentText = mComment.getText().toString();

                    if (commentText.trim().isEmpty()) {
                        Toast.makeText(mContext, "cant add an empty comment", Toast.LENGTH_SHORT).show();
                    } else {
                        CommentMethodsHandler.addNewComment(postsList.get(getAdapterPosition()).getPost_id(), user_id, commentText, TimeMethods.getUTCdatetimeAsString(),
                                new CommentMethodsHandler.OnAddingNewCommentsListener() {
                                    @Override
                                    public void onSuccessListener() {
                                        Toast.makeText(mContext, "added", Toast.LENGTH_SHORT).show();
                                        int count = postsList.get(getAdapterPosition()).getPost_comments_count() + 1;
                                        mPostComments.setText(count + "");
                                        postsList.get(getAdapterPosition()).setPost_comments_count(count);
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

        public void postCommentDialog() {
            commentsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentsDialog commentsDialog = new CommentsDialog();
                    commentsDialog.index = getAdapterPosition();
                    commentsDialog.post_id = postsList.get(getAdapterPosition()).getPost_id();
                    commentsDialog.post_comments_count = postsList.get(getAdapterPosition()).getPost_comments_count();
                    commentsDialog.postsAdapter = PostsAdapter.this;
                    commentsDialog.mCommentCount = mPostComments;
                    commentsDialog.post_userId = postsList.get(getAdapterPosition()).getUser_id();
                    commentsDialog.show(fragmentManager, mContext.getString(R.string.commentsDialog));
                }
            });

        }

        public void profileImage() {
            mProfileImage.setOnClickListener(new onClickProfile(getAdapterPosition()));
            mPostUserName.setOnClickListener(new onClickProfile(getAdapterPosition()));
        }

        public void initLike() {
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (like.getText().equals("Liked")) {
                        removeLike((TextView) v, mPostLikes, getAdapterPosition());

                    } else {

                        addLike((TextView) v, mPostLikes, getAdapterPosition());

                    }
                }
            });


            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dislike.getText().equals("Disliked")) {
                        removeDisLike((TextView) v, getAdapterPosition());

                    } else {

                        addDisLike((TextView) v, getAdapterPosition());
                    }
                }
            });
        }

        public void originalPost() {

            mSharedPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction frt = fragmentManager.beginTransaction();
                    frt.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                    PostFragment fragment = new PostFragment();
                    fragment.post_id = postsList.get(getAdapterPosition()).getOriginal_post_id();
                    frt.replace(R.id.mainLayoutPosts, fragment).addToBackStack(mContext.getString(R.string.post_fragment))
                            .commit();
                }
            });

        }

        public void viewPager() {
            mPhotoLayout.setVisibility(View.VISIBLE);
            mImageSlider.setVisibility(View.VISIBLE);
            mDots.setVisibility(View.VISIBLE);
            ImagesViewPagerClick adapter = new ImagesViewPagerClick(mContext, postsList.get(getAdapterPosition()).getPost_images_url(), fragmentManager);
            mImageSlider.setAdapter(adapter);
            mDots.setupWithViewPager(mImageSlider);
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
            mProfileImage = itemView.findViewById(R.id.profileImage);
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

