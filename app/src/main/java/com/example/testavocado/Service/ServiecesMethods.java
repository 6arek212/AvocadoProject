package com.example.testavocado.Service;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.Status;
import com.example.testavocado.Utils.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ServiecesMethods {
    private static final String TAG = "ServiecesMethods";




    public interface ServicesMethos {
        @GET("api/Post/")
        Call<Status> getPosts(@Query("User_id") int id, @Query("datetime") String datetime, @Query("offset") int offset);
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
    public static void getFriendsPosts(int user_id, String datetime, int offset, Context context, final PostMethods.OnGettingFriendsPostListener listener) {
        Log.d(TAG, "getFriendsPosts: user_id  " + user_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ServicesMethos friendsPosts = retrofit.create(ServicesMethos.class);

        final Call<Status> ca = friendsPosts.getPosts(user_id, datetime, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (status.getState() == 1) {

                    Log.d(TAG, "onResponse: " + status);

                    listener.OnSuccess(status.getJson_data());


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

}
