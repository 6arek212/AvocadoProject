package com.example.testavocado.Notification;

import android.util.Log;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Utils.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NotificationMethods {
    private static final String TAG = "NotificationMethods";

    public interface NotificationMethod {
        @GET("api/Notification/getUserNotification")
        Call<Status> getUserNotification(@Query("user_id") int user_id, @Query("datetime") String datetime, @Query("offset") int offset);


        @GET("api/Notification/getUserNotificationService")
        Call<Status> getUserNotificationService(@Query("user_id") int user_id, @Query("datetime") String datetime, @Query("offset") int offset);
    }


    public interface OnGettingNotification {
        void successfullyGettingNotification(String json);
        void serverException(String exception);
        void OnFailure(String exception);
    }


    public static void getNotification(int user_id, String datetime,int offest, final OnGettingNotification listener) {
        Log.d(TAG, "getProfileInfo: current_user_id " + user_id + "   datetime " + datetime);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        NotificationMethod method = retrofit.create(NotificationMethod.class);

        final Call<Status> ca = method.getUserNotification(user_id, datetime,offest);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (response.isSuccessful()&&status!=null){
                    if (status.getState() == 1) {
                        listener.successfullyGettingNotification(status.getJson_data());

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






    public static void getNotificationService(int user_id, String datetime,int offest, final OnGettingNotification listener) {
        Log.d(TAG, "getProfileInfo: current_user_id " + user_id + "   datetime " + datetime);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        NotificationMethod method = retrofit.create(NotificationMethod.class);

        final Call<Status> ca = method.getUserNotificationService(user_id, datetime,offest);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if(response.isSuccessful()&&status!=null){
                if (status.getState() == 1) {
                    listener.successfullyGettingNotification(status.getJson_data());

                } else if (status.getState() == 0) {
                    listener.serverException(0 + "  " + status.getException());

                } else {
                    listener.serverException(-1 + "  " + status.getException());
                }}
                else {
                    listener.serverException(response.errorBody().toString());
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
