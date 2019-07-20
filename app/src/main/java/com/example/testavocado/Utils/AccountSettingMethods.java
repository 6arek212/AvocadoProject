package com.example.testavocado.Utils;

import android.util.Log;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Models.User;
import com.example.testavocado.Profile.ProfileHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class AccountSettingMethods {
    private static final String TAG = "AccountSettingMethods";

    public interface AccountSettingsMethods {
        @POST("api/LoginRegisterInfo/locationSettings")
        Call<Status> settings(@Query("user_id") int user_id, @Query("state") boolean state, @Query("type") int type);


        @POST("api/LoginRegisterInfo/deleteAccount")
        Call<Status> deleteAccount(@Query("user_id") int user_id);
    }


    public static final int PRIVATE_ACCOUNT_TYPE = 1;
    public static final int LOCATION_TYPE = 2;


    public interface OnUpdatingAccountSetting {
        void onSuccess();

        void serverException(String exception);

        void OnFailure(String exception);
    }


    /**
     * handling account settings
     * <p>
     * 1- PRIVATE ACCOUNT
     * 2-LOCATION
     *
     * @param user_id  current user id
     * @param state    true/false
     * @param type     the type of the setting
     * @param listener return
     */
    public static void updateAccountSettings(int user_id, boolean state, int type, final OnUpdatingAccountSetting listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        AccountSettingsMethods accountSettingsMethods = retrofit.create(AccountSettingsMethods.class);

        final Call<Status> ca = accountSettingsMethods.settings(user_id, state, type);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        listener.onSuccess();

                    } else if (status.getState() == 0) {
                        listener.serverException(0 + "  " + status.getException());

                    } else {
                        listener.serverException(-1 + "  " + status.getException());
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













    public interface OnDeletingAccount {
        void onSuccess();
        void serverException(String exception);
        void OnFailure(String exception);
    }

    public static void deleteAccount(int user_id, final OnDeletingAccount listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        AccountSettingsMethods accountSettingsMethods = retrofit.create(AccountSettingsMethods.class);

        final Call<Status> ca = accountSettingsMethods.deleteAccount(user_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        listener.onSuccess();

                    } else if (status.getState() == 0) {
                        listener.serverException(0 + "  " + status.getException());

                    } else {
                        listener.serverException(-1 + "  " + status.getException());
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




}
