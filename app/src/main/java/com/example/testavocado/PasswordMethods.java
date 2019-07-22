package com.example.testavocado;

import android.util.Log;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Utils.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class PasswordMethods {
    private static final String TAG = "PasswordMethods";

    public interface PasswordMe {
        @POST("api/Messege/forgotPasswordSendEmail")
        Call<Status> forgotPasswordSendEmail(@Query("email") String email);
    }


    public interface OnSendingEmailListener {
        void onSent();

        void onServerError(String exception);

        void onFailure(String exception);
    }


    public static void sendEmailForPasswordRecovery(String email, final OnSendingEmailListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        PasswordMe methods = retrofit.create(PasswordMe.class);

        final Call<Status> ca = methods.forgotPasswordSendEmail(email);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: email sent "+status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        listener.onSent();


                    } else if (status.getState() == 0) {
                        listener.onServerError(status.getException());

                    } else {
                        listener.onServerError( status.getException());
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
