package com.example.testavocado.Profile;

import android.util.Log;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Utils.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Bio_Methods {

    public static final String TAG = "Bio_Methods";

    public interface on_bio_updated {
        void onSuccessListener(int  result);
        void onServerException(String ex);
        void onFailureListener(String ex);
    }

    public interface updatebio {
        @GET("api/Bio/update_bio")
        Call<Status> update_bio(@Query("userid") int userid, @Query("bio") String bio);
    }


    public static void UpdateBio(int userid, String bio, final on_bio_updated listener)
    {
        Retrofit retrofit= NetworkClient.getRetrofitClient();
        updatebio bi=retrofit.create(updatebio.class);

        final Call<Status> sa=bi.update_bio(userid,bio);

        sa.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

                if(status.getState()==1){
                    Log.d(TAG, "onResponse: "+status);
                        listener.onSuccessListener(status.getState());
                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onFailureListener(status.getException());
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
