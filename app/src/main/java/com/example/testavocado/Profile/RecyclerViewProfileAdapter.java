package com.example.testavocado.Profile;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.BaseActivity;
import com.example.testavocado.Chat.ChatActivity;
import com.example.testavocado.Connection.ConnectionsHandler;
import com.example.testavocado.Dialogs.CommentMethodsHandler;
import com.example.testavocado.Dialogs.CommentsDialog2;
import com.example.testavocado.Dialogs.ConfirmDialog;
import com.example.testavocado.Home.AddNewPostActivity;
import com.example.testavocado.Home.BottomSheetDialog2;
import com.example.testavocado.Home.Fragments.LikesDislikesFragment;
import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.User;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.ImagesViewPagerClick;
import com.example.testavocado.Utils.PostFragment;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewProfileAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecyclerViewProfileAdap";

    public static final int PROFILE_POSTS = 0;
    public static final int PROFILE_LAYOUT = 1;
    public static final int PROGRESS_BAR = 2;
    public static final int END_LIST = 3;
    public static final int FRIENDS_REQUEST_RECEIVED = 0;
    public static final int FRIENDS_LAYOUT = 1;
    public static final int FRIENDS_REQUEST_SENT = 2;
    public static final int FRIENDS_ADDING = 3;
    public static final int CURRENT_USER_PROFILE = 4;

    private List<Post> postsList = new ArrayList<>();
    private Context mContext;
    private int user_id, incomingUserId;
    private User user;
    private FragmentManager fragmentManager;
    public boolean is_endOfPosts, is_current_user;
    public int addingLayoutType;


    public RecyclerViewProfileAdapter(Context mContext, int user_id, int incomingUserId, User user, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.user_id = user_id;
        this.incomingUserId = incomingUserId;
        this.user = user;
        this.fragmentManager = fragmentManager;
        postsList.add(null);
    }


    public void clear() {
        postsList.clear();
        postsList.add(null);
        notifyDataSetChanged();
    }

    public void removeProg() {
        if (postsList.get(postsList.size() - 1) == null) {
            postsList.remove(postsList.size() - 1);
            notifyItemRemoved(postsList.size());
        }
    }

    public void addNull() {
        postsList.add(null);
        notifyItemInserted(postsList.size());
    }


    public void addSetOfPosts(List<Post> postArrayList, int s) {
        postsList.addAll(postArrayList);
        notifyItemRangeInserted(s, postArrayList.size());
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return PROFILE_LAYOUT;
        else if (postsList.get(position) != null)
            return PROFILE_POSTS;
        else if (postsList.get(position) == null && !is_endOfPosts)
            return PROGRESS_BAR;
        else
            return END_LIST;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        RecyclerView.ViewHolder vh;

        if (i == PROFILE_LAYOUT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_top_profile, viewGroup, false);
            vh = new InfoViewHolder(view);

        } else if (i == PROFILE_POSTS) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_item, viewGroup, false);
            vh = new PostViewHolder(view);
        } else if (i == PROGRESS_BAR) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progressbar_item, viewGroup, false);

            vh = new ProgressViewHolder(v);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_end_item, viewGroup, false);
            vh = new EndViewHolder(view);
        }

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int type = viewHolder.getItemViewType();

        if (type == PROFILE_LAYOUT) {
            InfoViewHolder v1 = (InfoViewHolder) viewHolder;

            v1.mUserName.setText(user.getUser_first_name() + " " + user.getUser_last_name());
            v1.mCountryCity.setText(user.getUser_country() + "," + user.getUser_city());
            v1.mConnectionCount.setText(user.getUser_connection_count() + "");
            v1.mPostsCount.setText(user.getUser_posts_count() + "");
            v1.mbio.setText("test");


            Glide.with(mContext)
                    .load(HelpMethods.get_user_profile_pic_sharedprefernces(mContext))
                    .centerCrop()
                    .error(R.drawable.profile_ic)
                    .into(v1.mProfileImage);

            if (is_current_user) {
                v1.addingLayout.setVisibility(View.GONE);
                v1.btn_add_bio.setVisibility(View.VISIBLE);

            } else {
                v1.btn_add_bio.setVisibility(View.GONE);
                v1.addingLayout.setVisibility(View.VISIBLE);

                  if (addingLayoutType == FRIENDS_LAYOUT) {
                    v1.friendsLayout.setVisibility(View.VISIBLE);
                    v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                    v1.friendRequestSentLayout.setVisibility(View.GONE);
                    v1.AddFriendLayout.setVisibility(View.GONE);

                    setFriendsLayout(v1);
                } else if (addingLayoutType == FRIENDS_REQUEST_SENT) {
                    v1.friendsLayout.setVisibility(View.GONE);
                    v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                    v1.friendRequestSentLayout.setVisibility(View.VISIBLE);
                    v1.AddFriendLayout.setVisibility(View.GONE);

                    setFriendsRequestReceived(v1);
                } else if (addingLayoutType == FRIENDS_REQUEST_RECEIVED) {
                    v1.friendsLayout.setVisibility(View.GONE);
                    v1.friendRequestRecivedLayout.setVisibility(View.VISIBLE);
                    v1.friendRequestSentLayout.setVisibility(View.GONE);
                    v1.AddFriendLayout.setVisibility(View.GONE);

                    setFriendsRequestSendLayout(v1);
                } else {
                    v1.friendsLayout.setVisibility(View.GONE);
                    v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                    v1.friendRequestSentLayout.setVisibility(View.GONE);
                    v1.AddFriendLayout.setVisibility(View.VISIBLE);

                    setAddFriendLayout(v1);

                }
            }


            if (is_current_user) {
                v1.mAddPost.setVisibility(View.VISIBLE);
                v1.mAddPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, AddNewPostActivity.class));
                    }
                });
            } else {
                v1.mAddPost.setVisibility(View.GONE);

            }


        } else if (type == PROFILE_POSTS) {
            final PostViewHolder v1 = (PostViewHolder) viewHolder;


            Glide.with(mContext)
                    .load(postsList.get(i).getUser_profile_photo())
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(v1.mProfileImage);


            v1.mPostUserName.setText(postsList.get(i).getUser_name() + " " + postsList.get(i).getUser_last_name());
            v1.mPostText.setText(postsList.get(i).getPost_text());
            v1.mPostLikes.setText(postsList.get(i).getPost_likes_count() + "");
            v1.mPostComments.setText(postsList.get(i).getPost_comments_count() + "");
            v1.mPostShares.setText(postsList.get(i).getPost_share_count() + "");


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


            likeHandler(v1, i);


            dislikeHandler(v1, i);


            if (postsList.get(i).getPost_images_url().isEmpty()) {
                v1.mImageSlider.setVisibility(View.GONE);
                v1.mDots.setVisibility(View.GONE);
                v1.mPhotoLayout.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "onBindViewHolder:  url " + postsList.get(i).getPost_images_url());
                initializeViewPage(v1, i);
            }


            initPostCommentsDialog(v1.commentsLayout, v1.mPostComments, i);

            addComment(v1.mSend, v1.mComment, v1.mPostComments, i);


            attachLikeFragment(v1.likeLayout, i);

            attachShareOnClick(v1.share, i);

            attachBottomSheet(v1.mPostOptions, i);
        } else if (!is_endOfPosts && type == PROGRESS_BAR) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }

    }


    private void setAddFriendLayout(final InfoViewHolder v1) {
        v1.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.onSendingNewFriendRequest(user_id, incomingUserId, TimeMethods.getUTCdatetimeAsString(), new ConnectionsHandler.OnStatusRegisterListener() {
                    @Override
                    public void onSuccessListener(int request_id) {
                        user.setFriend_request_id(request_id);


                        v1.friendsLayout.setVisibility(View.GONE);
                        v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                        v1.friendRequestSentLayout.setVisibility(View.VISIBLE);
                        v1.AddFriendLayout.setVisibility(View.GONE);

                        setFriendsRequestSendLayout(v1);
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
        });
    }


    private void setFriendsRequestReceived(final InfoViewHolder v1) {
        v1.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.acceptFriendRequest(user.getFriend_request_id(), TimeMethods.getUTCdatetimeAsString(), new ConnectionsHandler.OnAcceptingFriendRequestListener() {
                    @Override
                    public void onSuccessListener() {
                        v1.friendsLayout.setVisibility(View.VISIBLE);
                        v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                        v1.friendRequestSentLayout.setVisibility(View.GONE);
                        v1.AddFriendLayout.setVisibility(View.GONE);

                        setFriendsLayout(v1);

                    }

                    @Override
                    public void onServer(String ex) {
                        Log.d(TAG, "onServer: " + ex);

                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: " + ex);

                    }
                });
            }
        });


        v1.btnDeleteConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.RemoveFriend(user.getFriend_request_id(), user_id, incomingUserId, new ConnectionsHandler.OnRemovingFriendListener() {
                    @Override
                    public void onSuccessListener() {
                        v1.friendsLayout.setVisibility(View.GONE);
                        v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                        v1.friendRequestSentLayout.setVisibility(View.GONE);
                        v1.AddFriendLayout.setVisibility(View.VISIBLE);

                        setAddFriendLayout(v1);
                    }

                    @Override
                    public void onServer(String ex) {
                        Log.d(TAG, "onServer: " + ex);

                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: " + ex);

                    }
                });

            }
        });
    }


    private void setFriendsLayout(final InfoViewHolder v1) {

        v1.mFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setTitle("are you sure you want to remove connection ? ");

                confirmDialog.setOnConfirm(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        ConnectionsHandler.RemoveFriend(user.getFriend_request_id(), user_id, incomingUserId, new ConnectionsHandler.OnRemovingFriendListener() {
                            @Override
                            public void onSuccessListener() {
                                v1.friendsLayout.setVisibility(View.GONE);
                                v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                                v1.friendRequestSentLayout.setVisibility(View.GONE);
                                v1.AddFriendLayout.setVisibility(View.VISIBLE);

                                setAddFriendLayout(v1);

                                confirmDialog.dismiss();
                            }

                            @Override
                            public void onServer(String ex) {
                                Log.d(TAG, "onServer: " + ex);

                            }

                            @Override
                            public void onFailure(String ex) {
                                Log.d(TAG, "onFailure: " + ex);

                            }
                        });
                    }
                });


                confirmDialog.setCancelable(true);
                confirmDialog.show(fragmentManager, mContext.getString(R.string.confirm_dialog));
            }
        });


        v1.mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChatActivity.class));
            }
        });

    }


    private void setFriendsRequestSendLayout(final InfoViewHolder v1) {
        v1.btnRemoveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsHandler.RemoveFriend(user.getFriend_request_id(), user_id, incomingUserId, new ConnectionsHandler.OnRemovingFriendListener() {
                    @Override
                    public void onSuccessListener() {
                        v1.friendsLayout.setVisibility(View.GONE);
                        v1.friendRequestRecivedLayout.setVisibility(View.GONE);
                        v1.friendRequestSentLayout.setVisibility(View.GONE);
                        v1.AddFriendLayout.setVisibility(View.VISIBLE);

                        setAddFriendLayout(v1);
                    }

                    @Override
                    public void onServer(String ex) {
                        Log.d(TAG, "onServer: " + ex);

                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: " + ex);

                    }
                });

            }
        });
    }


    private void dislikeHandler(final PostViewHolder v1, final int i) {
        v1.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v1.dislike.getText().equals("Disliked")) {
                    removeDisLike((TextView) v, i);

                } else {

                    addDisLike((TextView) v, i);
                }
            }
        });
    }


    private void likeHandler(final PostViewHolder v1, final int i) {
        v1.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v1.like.getText().equals("Liked")) {
                    removeLike((TextView) v, v1.mPostLikes, i);

                } else {

                    addLike((TextView) v, v1.mPostLikes, i);

                }
            }
        });
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
                BottomSheetDialog2 bottomSheetDialog = new BottomSheetDialog2();
                bottomSheetDialog.post_userId = postsList.get(i).getUser_id();
                bottomSheetDialog.post_id = postsList.get(i).getPost_id();
                bottomSheetDialog.show(fragmentManager, "bottomSheetDialog");


                bottomSheetDialog.setOnPostReportListener(new BottomSheetDialog2.OnPostReportListener() {
                    @Override
                    public void onReport() {

                    }
                });


                bottomSheetDialog.setOnPostDeleteListener(new BottomSheetDialog2.OnPostDeleteListener() {
                    @Override
                    public void onDelete() {
                        postsList.remove(i);
                        notifyItemRemoved(i);
                    }
                });


                bottomSheetDialog.setOnPostHideListener(new BottomSheetDialog2.OnPostHideListener() {
                    @Override
                    public void onHide() {
                        postsList.remove(i);
                        notifyItemRemoved(i);
                    }
                });
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

                final ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setTitle("Are you sure you want to share this post ?");
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

                confirmDialog.show(fragmentManager, mContext.getString(R.string.confirm_dialog));

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
                CommentsDialog2 commentsDialog = new CommentsDialog2();
                commentsDialog.index = index;
                commentsDialog.post_id = postsList.get(index).getPost_id();
                commentsDialog.post_comments_count = postsList.get(index).getPost_comments_count();
                commentsDialog.posts = postsList;
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


    public class InfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mUserName, mbio, mCountryCity, mConnectionCount, mPostsCount;
        private CircleImageView mProfileImage;
        private LinearLayout friendsLayout, friendRequestSentLayout, friendRequestRecivedLayout, addingLayout, AddFriendLayout;
        private RelativeLayout mAddPost;
        private Button mFriends, mMessage, btnRemoveRequest, btnAccept, btnDeleteConnection, btnAddFriend;
        private Button btn_add_bio;


        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserName = itemView.findViewById(R.id.userName);
            mbio = (TextView) itemView.findViewById(R.id.textv_aboutme_merge_fragment_myprofile_center);
            btn_add_bio = (Button) itemView.findViewById(R.id.btn_add_biograph);
            mCountryCity = itemView.findViewById(R.id.cityCountry);
            mConnectionCount = itemView.findViewById(R.id.connectionsCount);
            mPostsCount = itemView.findViewById(R.id.postsCount);
            mProfileImage = itemView.findViewById(R.id.profileImage);
            friendsLayout = itemView.findViewById(R.id.friendsLayoutProfile);
            friendRequestSentLayout = itemView.findViewById(R.id.friendRequestSentLayout);
            friendRequestRecivedLayout = itemView.findViewById(R.id.friendRequestRecivedLayout);
            mAddPost = itemView.findViewById(R.id.addPost);
            mFriends = itemView.findViewById(R.id.friends);
            mMessage = itemView.findViewById(R.id.message);
            btnRemoveRequest = itemView.findViewById(R.id.btnRemoveRequest);
            btnAccept = itemView.findViewById(R.id.btnAcceptConnection);
            btnDeleteConnection = itemView.findViewById(R.id.btnDeleteConnection);
            addingLayout = itemView.findViewById(R.id.addingLayout);
            AddFriendLayout = itemView.findViewById(R.id.AddFriendLayout);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);

            //on click
            btn_add_bio.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_biograph:
                    Intent myintent = new Intent(mContext, add_bio.class);
                    // open add bio activiy with request code 2
                    ((Activity) mContext).startActivityForResult(myintent, 2);
                    break;
            }
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
