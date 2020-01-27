package com.example.testavocado.Profile;

import android.util.Log;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Models.User;
import com.example.testavocado.Utils.NetworkClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ProfileHandler {
    private static final String TAG = "ProfileHandler";


    public interface ProfileMethods {
        @GET("api/Post/getProfileInfo")
        Call<Status> getProfileInfo(@Query("profile_user_id") int profile_user_id, @Query("current_user_id") int current_user_id);


        @GET("api/Post/getProfilePosts")
        Call<Status> getProfilePosts(@Query("user_id") int user_id, @Query("datetime") String datetime,@Query("incomingUserId") int incomingUserId, @Query("offset") int offset);
    }


    public interface OnGettingProfileInfo {
        void successfullyGettingInfo(User user);

        void serverException(String exception);

        void OnFailure(String exception);
    }


    public static void getProfileInfo(int profile_user_id, int current_user_id, final OnGettingProfileInfo listener) {
        Log.d(TAG, "getProfileInfo: current_user_id " + current_user_id + "   profile_user_id " + profile_user_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ProfileMethods friendsPosts = retrofit.create(ProfileMethods.class);

        final Call<Status> ca = friendsPosts.getProfileInfo(profile_user_id, current_user_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (response.isSuccessful() && status!=null){
                    if (status.getState() == 1) {
                        try {
                            Log.d(TAG, "onResponse: " + status);
                            JSONArray json = new JSONArray(status.getJson_data());

                            User user = new Gson().fromJson(json.get(0).toString().toString(), User.class);
                            listener.successfullyGettingInfo(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.OnFailure(e.getMessage());
                        }
                    } else if (status.getState() == 0) {
                        listener.serverException(0 + "  " + status.getException());

                    } else {
                        listener.serverException(-1 + "  " + status.getException());
                    }
                }else{
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


    public interface OnGettingProfilePostsListener {
        void successfullyGettingPosts(String json);

        void serverException(String exception);

        void OnFailure(String exception);
    }


    /**
     * getting all the profile posts from the DB
     *
     * @param user_id current user id
     */

    public static void getProfilePosts(int user_id, String datetime,int incomingUserId, int offset, final OnGettingProfilePostsListener listener) {
        Log.d(TAG, "getProfilePosts: user_id " + user_id + "   datetime " + datetime + "   offset " + offset);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ProfileMethods friendsPosts = retrofit.create(ProfileMethods.class);

        final Call<Status> ca = friendsPosts.getProfilePosts(user_id, datetime,incomingUserId, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (response.isSuccessful()&&status!=null) {
                    if (status.getState() == 1) {
                        listener.successfullyGettingPosts(status.getJson_data());

                    } else if (status.getState() == 0) {
                        listener.serverException(0 + "  " + status.getException());

                    } else {
                        listener.serverException(-1 + "  " + status.getException());
                    }
                }else {
                    listener.OnFailure(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }


}
