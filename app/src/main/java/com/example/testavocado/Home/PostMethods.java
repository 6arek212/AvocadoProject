package com.example.testavocado.Home;

import android.util.Log;

import com.example.testavocado.Models.Like;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.Status;

import com.example.testavocado.Utils.NetworkClient;

import com.google.gson.Gson;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class PostMethods {
    private static final String TAG = "PostMethods";

    public static final int FRIENDS_POSTS = 0;
    public static final int PUBLIC_POSTS = 1;
    public static final int PUBLIC_FRIENDS_POSTS = 2;


    public interface PostsMethods {
        @GET("api/Post/GetFriendPosts2")
        Call<Status> getPosts(@Query("User_id") int id, @Query("datetime") String datetime, @Query("offset") int offset, @Query("type") int type);


        @POST("api/Post/likePost")
        Call<Status> addLike(@Query("User_id") int id, @Query("Post_id") int post_id, @Query("datetime") String datetime);


        @POST("api/Post/removeLike")
        Call<Status> removeLike(@Query("like_id") int like_id, @Query("post_id") int post_id);


        @GET("api/Post/getPostDisLikes")
        Call<Status> getPostDisLikes(@Query("Post_id") int post_id, @Query("datetime") String datetime, @Query("offset") int offset);


        @GET("api/Post/getPostLikes")
        Call<Status> getPostLikes(@Query("Post_id") int post_id, @Query("datetime") String datetime, @Query("offset") int offset);


        @POST("api/Post/dislikePost")
        Call<Status> dislikePost(@Query("User_id") int id, @Query("Post_id") int post_id, @Query("datetime") String datetime);


        @POST("api/Post/removeDisLike")
        Call<Status> removeDisLike(@Query("dis_like_id") int dis_like_id, @Query("post_id") int post_id);


        @POST("api/Post/shareAPost")
        Call<Status> shareAPost(@Body Post post);

        @POST("api/Post/addNewPost")
        Call<Status> addPost(@Body Post post);


        @GET("api/Post/deletePost")
        Call<Status> deletePost(@Query("post_id") int post_id);

        @GET("api/Post/getPostById")
        Call<Status> getPostById(@Query("post_id") int post_id, @Query("user_id") int user_id);


        @POST("api/Post/hidePost")
        Call<Status> hidePost(@Query("post_id") int post_id, @Query("user_id") int user_id, @Query("datetime") String datetime);

        @POST("api/Post/removeHidePost")
        Call<Status> removeHidePost(@Query("post_hidden_id") int post_hidden_id);

        @POST("api/Post/reportPost")
        Call<Status> reportPost(@Query("post_id") int post_id, @Query("user_id") int user_id, @Query("report_type") int report_type, @Query("report_datetime") String datetime);


        @POST("api/Post/savePost")
        Call<Status> savePost(@Query("post_id") int post_id, @Query("user_id") int user_id, @Query("datetime") String datetime);

        @POST("api/Post/removeSavedPost")
        Call<Status> removeSavedPost(@Query("saved_post_id") int saved_post_id);

    }


    public interface OnGettingFriendsPostListener {
        void OnSuccess(String json);

        void OnServerException(String ex);

        void OnFailure(String ex);
    }

    /**
     * getting all the friends posts from the DB
     *
     * @param user_id
     */
    public static void getFriendsPosts(int user_id, String datetime, int offset, int type, final OnGettingFriendsPostListener listener) {
        Log.d(TAG, "getFriendsPosts: user_id  " + user_id + "  datetime " + datetime + "  offset " + offset + "  type " + type);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods friendsPosts = retrofit.create(PostsMethods.class);

        final Call<Status> ca = friendsPosts.getPosts(user_id, datetime, offset, type);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(@NotNull Call<Status> call, @NotNull Response<Status> response) {
                Status status = response.body();

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + status);

                    if (status != null) {
                        if (status.getState() == 1) {

                           /* JSONArray jsonArray = new JSONArray(status.getJson_data());
                            List<Post> posts = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d(TAG, "onResponse:  1 post " + jsonArray.get(i).toString());
                                posts.add(new Gson().fromJson(jsonArray.get(i).toString(), Post.class));

                                // Log.d(TAG, "onResponse: "+posts.get(i));
                            }*/


                            listener.OnSuccess(status.getJson_data());


                        } else if (status.getState() == 0) {
                            listener.OnServerException(status.getException());

                        } else {
                            listener.OnServerException(status.getException());
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Status> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });
    }


    public interface OnLikingPostListener {
        void OnLiked(int like_id);

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void likeAPost(int user_id, int post_id, String datetime, final OnLikingPostListener listener) {
        Log.d(TAG, "likeAPost: user_id " + user_id + "   post_id " + post_id + " datetime " + datetime);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods like = retrofit.create(PostsMethods.class);

        final Call<Status> ca = like.addLike(user_id, post_id, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (status.getState() == 1) {
                    try {
                        Log.d(TAG, "onResponse: " + status);
                        JSONObject json = new JSONObject(status.getJson_data());
                        listener.OnLiked(json.getInt("like_id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.OnServerException(e.getMessage());
                    }
                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());

                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public interface OnDeletingPostListener {
        void OnDeleted();

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void deletePost(int post_id, final OnDeletingPostListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        final PostsMethods like = retrofit.create(PostsMethods.class);

        final Call<Status> ca = like.deletePost(post_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status = response.body();

                    if (status.getState() == 1) {
                        listener.OnDeleted();
                    } else if (status.getState() == 0) {
                        listener.OnServerException(status.getException());

                    } else {
                        listener.OnServerException(status.getException());
                    }
                } else {
                    listener.OnFailure(response.message());

                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public interface OnReportingPostListener {
        void OnPostReported();

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void reportPost(int post_id, int user_id, int reportType, String datetime, final OnReportingPostListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        final PostsMethods methods = retrofit.create(PostsMethods.class);

        final Call<Status> ca = methods.reportPost(post_id, user_id, reportType, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status = response.body();

                    if (status.getState() == 1) {
                        listener.OnPostReported();
                    } else if (status.getState() == 0) {
                        listener.OnServerException(status.getException());

                    } else {
                        listener.OnServerException(status.getException());
                    }
                } else {
                    listener.OnFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public interface OnHidingPostListener {
        void OnPostHide();

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void hidePost(int post_id, int user_id, String datetime, final OnHidingPostListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        final PostsMethods methods = retrofit.create(PostsMethods.class);

        final Call<Status> ca = methods.hidePost(post_id, user_id, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status = response.body();

                    if (status.getState() == 1) {
                        listener.OnPostHide();
                    } else if (status.getState() == 0) {
                        listener.OnServerException(status.getException());

                    } else {
                        listener.OnServerException(status.getException());
                    }
                } else {
                    listener.OnFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public interface OnRemovingLikingPostListener {
        void OnLikeRemoved();

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void removeLike(int like_id, int post_id, final OnRemovingLikingPostListener listener) {
        Log.d(TAG, "likeAPost:" + post_id + "+post_id" + post_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods relike = retrofit.create(PostsMethods.class);

        final Call<Status> ca = relike.removeLike(like_id, post_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (status.getState() == 1) {
                    listener.OnLikeRemoved();

                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());

                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public interface OnDisLikingPostListener {
        void OnDisLiked(int dislike_id);

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void disLikePost(int user_id, int post_id, String datetime, final OnDisLikingPostListener listener) {
        Log.d(TAG, "likeAPost: user_id " + user_id + "   post_id " + post_id + " datetime " + datetime);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods like = retrofit.create(PostsMethods.class);

        final Call<Status> ca = like.dislikePost(user_id, post_id, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (status.getState() == 1) {
                    try {
                        Log.d(TAG, "onResponse: " + status);
                        JSONObject json = new JSONObject(status.getJson_data());
                        listener.OnDisLiked(json.getInt("like_id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.OnServerException(e.getMessage());
                    }
                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());

                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public static void removeDisLike(int dislike_id, int post_id, final OnRemovingLikingPostListener listener) {
        Log.d(TAG, "remove dislike: " + dislike_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods relike = retrofit.create(PostsMethods.class);

        final Call<Status> ca = relike.removeDisLike(dislike_id, post_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: " + status);

                if (status.getState() == 1) {
                    listener.OnLikeRemoved();

                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());

                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


    public interface OnGettingSharesListener {
        void OnGettingLikes(List<Like> likeList);

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void getPostShares(int post_id, String datetime, int offset, final OnGettingSharesListener listener) {
        Log.d(TAG, "getPostLikes: post_id " + post_id + " datetime " + datetime + "  offset " + offset);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods relike = retrofit.create(PostsMethods.class);

        final Call<Status> ca = relike.getPostLikes(post_id, datetime, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: " + status);

                if (status.getState() == 1) {
                    try {
                        JSONArray array = new JSONArray(status.getJson_data());
                        List<Like> likes = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            likes.add(new Gson().fromJson(array.get(i).toString(), Like.class));
                        }
                        listener.OnGettingLikes(likes);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.OnServerException(e.getMessage());
                    }


                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());

                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });


    }


    public interface OnGettingPostsLikesListener {
        void OnGettingLikes(List<Like> likeList);

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void getPostDisLikes(int post_id, String datetime, int offset, final OnGettingPostsLikesListener listener) {
        Log.d(TAG, "getPostDisLikes: post_id " + post_id + " datetime " + datetime + "  offset " + offset);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods relike = retrofit.create(PostsMethods.class);

        final Call<Status> ca = relike.getPostDisLikes(post_id, datetime, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: " + status);
                if (response.isSuccessful()) {


                    if (status.getState() == 1) {
                        try {
                            JSONArray array = new JSONArray(status.getJson_data());
                            List<Like> likes = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                likes.add(new Gson().fromJson(array.get(i).toString(), Like.class));
                            }
                            listener.OnGettingLikes(likes);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.OnServerException(e.getMessage());
                        }
                    } else if (status.getState() == 0) {
                        listener.OnServerException(status.getException());
                    } else {
                        listener.OnServerException(status.getException());
                    }

                } else {
                    listener.OnFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });


    }


    public interface OnGettingPostLikesListener {
        void OnGettingLikes(List<Like> likeList);

        void OnServerException(String ex);

        void OnFailure(String ex);
    }


    public static void getPostLikes(int post_id, String datetime, int offset,
                                    final OnGettingPostLikesListener listener) {
        Log.d(TAG, "getPostLikes: post_id " + post_id + " datetime " + datetime + "  offset " + offset);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods relike = retrofit.create(PostsMethods.class);

        final Call<Status> ca = relike.getPostLikes(post_id, datetime, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        try {
                            JSONArray array = new JSONArray(status.getJson_data());
                            List<Like> likes = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                likes.add(new Gson().fromJson(array.get(i).toString(), Like.class));
                            }
                            listener.OnGettingLikes(likes);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.OnServerException(e.getMessage());
                        }


                    } else if (status.getState() == 0) {
                        listener.OnServerException(status.getException());

                    } else {
                        listener.OnServerException(status.getException());
                    }
                } else {
                    listener.OnFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });


    }


    public interface OnAddingNewPostListener {
        public void onSuccess();

        public void onServerException(String ex);

        public void onFailure(String ex);
    }

    public static void addPost(Post post, final OnAddingNewPostListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods newPost = retrofit.create(PostsMethods.class);

        final Call<Status> ca = newPost.addPost(post);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: adding post  " + status + "  " + response);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        Log.d(TAG, "onResponse: " + status);
                        listener.onSuccess();
                    } else if (status.getState() == 0) {
                        listener.onFailure(status.getException());
                    } else {
                        listener.onFailure(status.getException());
                    }
                } else {
                    listener.onFailure(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });
    }


    public interface OnSharingPostListener {
        public void onSuccess();

        public void onServerException(String ex);

        public void onFailure(String ex);
    }

    public static void sharePost(Post post, final OnSharingPostListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods newPost = retrofit.create(PostsMethods.class);

        final Call<Status> ca = newPost.shareAPost(post);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status = response.body();
                    Log.d(TAG, "onResponse: adding post  " + status);

                    if (status.getState() == 1) {
                        Log.d(TAG, "onResponse: " + status);
                        listener.onSuccess();
                    } else if (status.getState() == 0) {
                        listener.onFailure(status.getException());
                    } else {
                        listener.onFailure(status.getException());
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });
    }


    public interface OnGettingPostByIdListener {
        public void onSuccess(String json);

        public void onServerException(String ex);

        public void onFailure(String ex);
    }

    public static void getPostById(int post_id, int user_id,
                                   final OnGettingPostByIdListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PostsMethods newPost = retrofit.create(PostsMethods.class);

        final Call<Status> ca = newPost.getPostById(post_id, user_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: adding post  " + status);

                if (status.getState() == 1) {
                    listener.onSuccess(status.getJson_data());
                } else if (status.getState() == 0) {
                    listener.onServerException(status.getException());
                } else {
                    listener.onServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });
    }

}
