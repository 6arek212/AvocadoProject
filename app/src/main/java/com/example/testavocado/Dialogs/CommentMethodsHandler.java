package com.example.testavocado.Dialogs;

import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.Models.Comment;
import com.example.testavocado.Models.Status;
import com.example.testavocado.R;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.Utils.ServerConnection;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class CommentMethodsHandler {
    private static final String TAG = "CommentMethodsHandler";

    public interface CommentsInterface {
        @GET("api/Comments/gettingUpdatedComments")
        Call<Status> gettingUpdatedComments(@Query("Post_id") int post_id, @Query("Comments_incomming_count") int comments_count, @Query("datetime") String datetime);


        @GET("api/Comments/requestPostComments")
        Call<Status> getPostComments(@Query("Post_id") int post_id, @Query("Offset") int offset, @Query("datetime") String datetime);

        @GET("api/Comments/addNewCommentToPost")
        Call<Status> addNewComment(@Query("Post_id") int post_id, @Query("User_id") int user_id, @Query("Comment_text") String comment_text, @Query("Comment_date_time") String datetime);


        @POST("api/Comments/deleteComment")
        Call<Status> deleteComment(@Query("comment_id") int comment_id,@Query("post_id") int post_id);
    }


    public interface OnAddingNewCommentsListener {
        public void onSuccessListener();

        public void onServerException(String ex);

        public void onFailureListener(String ex);
    }


    public static void addNewComment(final int post_id, int user_id, String text, String comment_date_time, final OnAddingNewCommentsListener listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        CommentsInterface interface1 = retrofit.create(CommentsInterface.class);

        final Call<Status> ca = interface1.addNewComment(post_id, user_id, text, comment_date_time);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse:  add new comment  " + status);

                if (status.getState() == 1) {
                    listener.onSuccessListener();

                } else if (status.getState() == 0) {
                    listener.onServerException(status.getException());
                } else {
                    listener.onServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });

    }


    public interface OnGettingUpdatedCommentsListener {
        public void onSuccessListener(List<Comment> comments);

        public void onServerException(String ex);

        public void onFailureListener(String ex);
    }

    public static void getUpdatedComments(int post_id, int comments_incomming_count, String datetime, final OnGettingUpdatedCommentsListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        CommentsInterface interface1 = retrofit.create(CommentsInterface.class);

        final Call<Status> ca = interface1.gettingUpdatedComments(post_id, comments_incomming_count, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse:  getting updated comments  " + status);

                if (status.getState() == 1) {
                    try {
                        JSONArray jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<Comment> comments = new ArrayList<>();
                        JSONObject json;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            json = jsonArray.getJSONObject(i);
                            comments.add(new Gson().fromJson(json.toString(), Comment.class));
                        }

                        listener.onSuccessListener(comments);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status.getState() == 0) {
                    listener.onServerException(status.getException());
                } else {
                    listener.onServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });


    }


    public interface OnGettingPostCommentsListener {
        public void onSuccessListener(List<Comment> comments);

        public void onServerException(String ex);

        public void onFailureListener(String ex);
    }


    /**
     * getting 10 comments from the server
     * with an offset
     *
     * @param offset
     */

    public static void getComments(int post_id, final int offset, String datetime, final OnGettingPostCommentsListener listener) {
        Log.d(TAG, "getTop10Comments: offset " + offset + "  post_id " + post_id);


        Retrofit retrofit = NetworkClient.getRetrofitClient();
        CommentsInterface interface1 = retrofit.create(CommentsInterface.class);

        final Call<Status> ca = interface1.getPostComments(post_id, offset, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse:  get comments  " + status);

                if (status.getState() == 1) {
                    try {
                        JSONArray jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<Comment> comments = new ArrayList<>();
                        JSONObject json;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            json = jsonArray.getJSONObject(i);
                            comments.add(new Gson().fromJson(json.toString(), Comment.class));
                        }

                        listener.onSuccessListener(comments);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status.getState() == 0) {
                    listener.onServerException(status.getException());
                } else {
                    listener.onServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }


    public interface OnDeletingCommentListener {
        public void OnDeleted();

        public void onServerException(String ex);

        public void onFailureListener(String ex);
    }


    /**
     * deleting a comment request to the server
     *
     * @param listener
     */
    public static void deleteComment(int comment_id,int post_id, final OnDeletingCommentListener listener) {


        Retrofit retrofit = NetworkClient.getRetrofitClient();
        CommentsInterface interface1 = retrofit.create(CommentsInterface.class);

        final Call<Status> ca = interface1.deleteComment(comment_id,post_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status = response.body();
                    Log.d(TAG, "onResponse:  delete comment  " + status);

                    if (status.getState() == 1) {
                        listener.OnDeleted();
                    } else if (status.getState() == 0) {
                        listener.onServerException(status.getException());
                    } else {
                        listener.onServerException(status.getException());
                    }
                } else {
                    listener.onFailureListener(response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }


}
