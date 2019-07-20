package com.example.testavocado.Utils;

import android.util.Log;

import com.example.testavocado.Models.Status;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class LocationMethods {
    private static final String TAG = "LocationMethods";

    public interface OnUpdatingLocationListener {
        @GET("api/Location/updateUserLocation")
        Call<Status> updateUserLocation(@Query("user_id") int user_id, @Query("latitude") double latitude, @Query("longitude") double longitude);

    }


    public interface OnUpdateListener {
        public void onUpdate();
        public void onServerException(String ex);
        public void onFailure(String ex);
    }


    public static void updateLocation(int user_id,double latitude,double  longitude, final OnUpdateListener listener) {
        Log.d(TAG, "updateLocation: attempting to update location latitude "+latitude+"   longitude"+longitude+"   user_id"+user_id);
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        OnUpdatingLocationListener interface1 = retrofit.create(OnUpdatingLocationListener.class);

        final Call<Status> ca = interface1.updateUserLocation(user_id,latitude,longitude);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse:  update location  " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        listener.onUpdate();
                    } else if (status.getState() == 0) {
                        listener.onServerException(status.getException());
                    } else {
                        listener.onServerException(status.getException());
                    }
                }
                else {
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


}
