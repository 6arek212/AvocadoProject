package com.example.testavocado.Login;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.Models.Status;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.Utils.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class LoginMethods {
    private static final String TAG = "LoginMethods";

    public interface onLogin {
        void onSuccessListener(Setting setting);

        void onServerException(String ex);

        void onFailureListener(String ex);
    }

    public interface loginUser {
        @GET("api/LoginRegisterInfo/loginWithEmailAndPassword")
        Call<Status> login(@Query("User_Email") String email, @Query("User_password") String password);

        @POST("api/LoginRegisterInfo/userOnline")
        Call<Status> userOnline(@Query("user_id") int user_id, @Query("state") boolean state);


    }

    /**
     * Login with email and password method
     * *
     * *                           function checks the database for a specific email and password
     * *                           and if they are exists the server returns :
     * *
     * *                            State Object :
     * *
     * *                                  1 - the user and email exists and correct
     * *                                  0 - not found
     * *                                  -1 - Server System Exception for Debug
     * *
     */
    public static void loginWithEmailAndPassword(final String email, String password, final onLogin loginActivity) {
        {
            Retrofit retrofit = NetworkClient.getRetrofitClient();
            loginUser login = retrofit.create(loginUser.class);

            final Call<Status> ca = login.login(email, password);
            ca.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Status status = response.body();
                    Log.d(TAG, "onResponse: " + status);

                    if (response.isSuccessful()&&status!=null) {
                        if (status.getState() == 1) {
                            Log.d(TAG, "onResponse: " + status);

                            Setting setting = new Gson().fromJson(status.getJson_data(), Setting.class);
                            loginActivity.onSuccessListener(setting);

                        } else if (status.getState() == 0) {
                            loginActivity.onServerException(status.getException());
                        } else {
                            loginActivity.onFailureListener(status.getException());
                        }
                    } else {
                        loginActivity.onFailureListener(response.message());
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                    loginActivity.onFailureListener(t.getMessage());
                }
            });

        }

    }


    public interface OnUpdateUserOnlineListener {
        void onSuccess();

        void onServerException(String ex);

        void onFailure(String ex);
    }

    public static void updateOnlineState(int user_id, boolean state, final OnUpdateUserOnlineListener listener) {
        {
            Retrofit retrofit = NetworkClient.getRetrofitClient();
            loginUser login = retrofit.create(loginUser.class);

            final Call<Status> ca = login.userOnline(user_id, state);
            ca.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    Status status = response.body();
                    Log.d(TAG, "onResponse: " + status);
                    if (response.isSuccessful()&&status!=null) {

                        if (status.getState() == 1) {
                            listener.onSuccess();

                        } else if (status.getState() == 0) {
                            listener.onServerException(status.getException());
                        } else {
                            listener.onFailure(status.getException());
                        }
                    } else {
                        listener.onFailure(response.message());
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


}