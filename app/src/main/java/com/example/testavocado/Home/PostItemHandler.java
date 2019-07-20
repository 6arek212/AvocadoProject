package com.example.testavocado.Home;

public class PostItemHandler {
/*
    private static final String TAG = "PostItemHandler";

   private Context mContext;
   private int user_id;

    public PostItemHandler(Context mContext, int user_id) {
        this.mContext = mContext;
        this.user_id = user_id;
    }





    public void attachOriginalPostFragment(TextView mShared,final Post post,final FragmentManager fragmentManager) {
        mShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frt = fragmentManager.beginTransaction();
                frt.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                PostFragment fragment = new PostFragment();
                fragment.post_id = post.getOriginal_post_id();
                frt.replace(R.id.mainLayoutPosts, fragment).addToBackStack(mContext.getString(R.string.post_fragment))
                        .commit();
            }
        });
    }




    public interface OnLikeListener{
        void onLike();
        void onFail();
    }
    public void addLike(Post post, final OnLikeListener onLikeListener) {
                PostMethods.likeAPost(user_id, post.getPost_id(), TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnLikingPostListener() {
                    @Override
                    public void OnLiked(int like_id) {
                        Log.d(TAG, "OnLiked: post likes " + like_id);
                        onLikeListener.onLike();
                    }

                    @Override
                    public void OnServerException(String ex) {
                        Log.d(TAG, "OnServerException: " + ex);
                        onLikeListener.onFail();

                    }

                    @Override
                    public void OnFailure(String ex) {
                        Log.d(TAG, "OnFailure: " + ex);
                        onLikeListener.onFail();

                    }
                });

    }





    public interface OnRemovingLikeListener{
        void onRemove();
        void onFail();
    }

    public void removeLike(Post post, final OnRemovingLikeListener onRemovingLikeListener) {
        PostMethods.removeLike(post.getLike_id(), post.getPost_id(), new PostMethods.OnRemovingLikingPostListener() {
            @Override
            public void OnLikeRemoved() {
                Log.d(TAG, "OnLikeRemoved: ");
                onRemovingLikeListener.onRemove();
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);
                onRemovingLikeListener.onFail();
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);
                onRemovingLikeListener.onFail();
            }
        });
    }














    public interface OnDisLikeListener{
        void onDisLike();
        void onFail();
    }
    public void addDisLike(Post post, final OnDisLikeListener onDisLikeListener) {

                PostMethods.disLikePost(user_id, post.getPost_id(), TimeMethods.getUTCdatetimeAsString(), new PostMethods.OnDisLikingPostListener() {
                    @Override
                    public void OnDisLiked(int dislike_id) {
                        Log.d(TAG, "OnDisLiked: " + dislike_id);
                        onDisLikeListener.onDisLike();
                    }

                    @Override
                    public void OnServerException(String ex) {
                        Log.d(TAG, "OnServerException: " + ex);
                        onDisLikeListener.onFail();
                    }

                    @Override
                    public void OnFailure(String ex) {
                        Log.d(TAG, "OnFailure: " + ex);
                        onDisLikeListener.onFail();
                    }
                });

    }





    public interface OnRemoveDisLikeListener{
        void onDisLikeRemoved();
        void onFail();
    }

    public void removeDisLike(Post post, final OnRemoveDisLikeListener onRemoveDisLikeListener) {
        PostMethods.removeDisLike(post.getDis_like_id(),post.getPost_id(), new PostMethods.OnRemovingLikingPostListener() {
            @Override
            public void OnLikeRemoved() {
                Log.d(TAG, "OnDisLikeRemoved: ");

                onRemoveDisLikeListener.onDisLikeRemoved();
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: " + ex);
                onRemoveDisLikeListener.onFail();
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: " + ex);
                onRemoveDisLikeListener.onFail();
            }
        });
    }










    public void initializeViewPage(PostsAdapter.PostViewHolder v1, List<String> imagesUrl, FragmentManager fragmentManager) {
        if (imagesUrl.isEmpty()) {
            v1.mImageSlider.setVisibility(View.GONE);
            v1.mDots.setVisibility(View.GONE);
            v1.mPhotoLayout.setVisibility(View.GONE);
        } else {
            v1.mPhotoLayout.setVisibility(View.VISIBLE);
            v1.mImageSlider.setVisibility(View.VISIBLE);
            v1.mDots.setVisibility(View.VISIBLE);
            ImagesViewPagerClick adapter = new ImagesViewPagerClick(mContext, imagesUrl,fragmentManager);
            v1.mImageSlider.setAdapter(adapter);
            v1.mDots.setupWithViewPager(v1.mImageSlider);
        }
    }






    public interface OnBottomSheetAction
    {
        void onPostHide();
        void onPostDeleted();
        void onPostReported();
    }


    public void attachBottomSheet(Post post, final FragmentManager fragmentManager, final OnBottomSheetAction onBottomSheetAction) {

                final BottomSheetDialog2 bottomSheetDialog = new BottomSheetDialog2();
                bottomSheetDialog.post_id = post.getPost_id();
                bottomSheetDialog.post_userId = post.getUser_id();
                bottomSheetDialog.show(fragmentManager, "bottomSheetDialog");

                bottomSheetDialog.setOnPostDeleteListener(new BottomSheetDialog2.OnPostDeleteListener() {
                    @Override
                    public void onDelete() {
                       onBottomSheetAction.onPostDeleted();
                    }
                });



                bottomSheetDialog.setOnPostHideListener(new BottomSheetDialog2.OnPostHideListener() {
                    @Override
                    public void onHide() {
                        onBottomSheetAction.onPostHide();
                    }
                });




                bottomSheetDialog.setOnPostReportListener(new BottomSheetDialog2.OnPostReportListener() {
                    @Override
                    public void onReport() {
                        onBottomSheetAction.onPostReported();
                    }
                });
    }








    public void attachShareOnClick(Post post) {
                final Post post2 = new Post();
                post.setUser_id(user_id);
                post.setPost_text(post.getPost_text());
                //TODO post.setPost_image_path(postsList.get(i).getPost_image_path());
                post.setPost_date_time(TimeMethods.getUTCdatetimeAsString());
                post.setPost_type(post.getPost_type());
                post.setPost_is_shared(true);
                post.setOriginal_post_id(post.getPost_id());


                PostMethods.addPost(post, new PostMethods.OnAddingNewPostListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: shared a post :D " + post2);
                        Toast.makeText(mContext, "Post Shared", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServerException(String ex) {
                        Log.d(TAG, "onServerException: error sharing post " + ex);

                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: error while sharing a post" + ex);

                    }
                });
    }








    public void attachLikeFragment(final int post_id) {
                LikesDislikesFragment fragment = new LikesDislikesFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(mContext.getString(R.string.post_id), post_id);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
                FragmentTransaction tr = fragmentManager.beginTransaction();
                tr.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                tr.replace(R.id.baseLayout, fragment)
                        .addToBackStack(mContext.getString(R.string.LikesDislikesFragment)).commit();
    }





    public interface OnAddingComment{
        void onComment();
        void onFail();
    }


    public void addComment(final Post post,EditText text, final OnAddingComment onAddingComment) {
                String commentText = text.getText().toString();

                if (commentText.trim().isEmpty()) {
                    Toast.makeText(mContext, "cant add an empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    CommentMethodsHandler.addNewComment(post.getPost_id(), user_id, commentText, TimeMethods.getUTCdatetimeAsString(),
                            new CommentMethodsHandler.OnAddingNewCommentsListener() {
                                @Override
                                public void onSuccessListener() {
                                    onAddingComment.onComment();
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Log.d(TAG, "onServerException: " + ex);
                                    onAddingComment.onFail();

                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Log.d(TAG, "onFailureListener: " + ex);
                                    onAddingComment.onFail();

                                }
                            });
                }


    }









    public void attachProfileFragment(int id){
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(mContext.getString(R.string.current_user_profile), id);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = ((BaseActivity) mContext).getSupportFragmentManager();
        FragmentTransaction tr = fragmentManager.beginTransaction();
        tr.replace(R.id.mainLayoutPosts, fragment).addToBackStack(mContext.getString(R.string.profile_fragment)).commit();
    }

*/

}
